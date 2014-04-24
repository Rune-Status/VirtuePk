package org.virtue.network.protocol.codec.login;

import java.math.BigInteger;
import java.net.ProtocolException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.Constants;
import org.virtue.cache.Cache;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Password;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.network.io.IOHub;
import org.virtue.network.protocol.messages.LoginResponse;
import org.virtue.utility.Base37Utils;
import org.virtue.utility.BufferUtils;
import org.virtue.utility.DisplayMode;
import org.virtue.utility.MachineData;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;
import org.virtue.utility.XTEA;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 5, 2014
 */
public class RS3LoginDecoder extends FrameDecoder implements ChannelHandler {


	/**
	 * An enumeration used for storing the possible states of login.
	 */
	public enum LoginState {
		DECODE_HEADER, CONNECTION_TYPE, CLIENT_DETAILS, PLAYER_DATA, LOBBY_PAYLOAD
	};
        
        public static final int LOBBY_LOGIN = 19;
        public static final int GAME_LOGIN = 16;
        public static final int GAME_RECONNECT = 18;
        public static final int GAME_DATA = 26;
	
	/**
	 * The default login state.
	 */
	private LoginState state = LoginState.CONNECTION_TYPE;
	
	private Account account = null;
	
	@SuppressWarnings("unused")
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buf) throws Exception {
		LoginType type;
		if (!buf.readable()) {
			throw new ProtocolException("Bad buffer state.");
		}
		int loginOpcode = buf.readUnsignedByte();
		switch (loginOpcode) {
		    case GAME_DATA:
		        return fetchPlayerData(ctx, buf);
		    case LOBBY_LOGIN:
		    	type = LoginType.LOBBY;
		    	break;
		    case GAME_LOGIN:
		    case GAME_RECONNECT:
		    	type = LoginType.WORLD_PART_1;
		    	break;
		    default:
		    	throw new ProtocolException("Invalid connecton: " + loginOpcode);
		}
		int loginSize = buf.readShort() & 0xFFFF;
		if (buf.readableBytes() < loginSize) {	
			return null;
		}
		int version = buf.readInt();
		int subVersion = buf.readInt();
		if (version != Cache.SERVER_REVISION && subVersion != Cache.SUB_BUILD) {
			return new LoginResponse(LoginResponse.GAME_UPDATED);
		}
		if (type.equals(LoginType.WORLD_PART_1)) {
			buf.readByte();
		}
		int secureBufferSize = buf.readShort() & 0xFFFF;
		if (buf.readableBytes() < secureBufferSize) {
			return new LoginResponse(LoginResponse.BAD_LOGIN_PACKET);
		}
		byte[] secureBytes = new byte[secureBufferSize];
		buf.readBytes(secureBytes);
		ChannelBuffer secureBuffer = ChannelBuffers.wrappedBuffer(new BigInteger(secureBytes).modPow(Constants.JS5PrivateKey, Constants.JS5ModulusKey).toByteArray());
		int blockOpcode = secureBuffer.readUnsignedByte();

		if (blockOpcode != 10) {
			return new LoginResponse(LoginResponse.BAD_LOGIN_PACKET);
		}

		int[] xteaKey = new int[4];
		for (int key = 0; key < xteaKey.length; key++) {
			xteaKey[key] = secureBuffer.readInt();
		}

		long vHash = secureBuffer.readLong();
		if (vHash != 0L) {
			return new LoginResponse(LoginResponse.BAD_LOGIN_PACKET);
		}
		String password = BufferUtils.readString(secureBuffer);
		long clientSessionKey = secureBuffer.readLong();
		long serverSessionKey = secureBuffer.readLong();
		
		byte[] xteaBlock = new byte[buf.readableBytes()];
		buf.readBytes(xteaBlock);
		ChannelBuffer xteaBuffer = ChannelBuffers.wrappedBuffer(XTEA.decipher(xteaKey, xteaBlock));
		
		boolean decodeAsString = (xteaBuffer.readByte() == 1);
		String username = decodeAsString ? BufferUtils.readString(xteaBuffer)
				: Base37Utils.decodeBase37(xteaBuffer.readLong());
		if (type.equals(LoginType.LOBBY)) {
			xteaBuffer.readByte();//Game type
			xteaBuffer.readByte();//Language
		}
		DisplayMode displayMode = DisplayMode.forId(xteaBuffer.readByte());
		int screenWidth = xteaBuffer.readUnsignedShort();
		int screenHeight = xteaBuffer.readUnsignedShort();
		int unknown2 = xteaBuffer.readUnsignedByte();
		
		xteaBuffer.skipBytes(24);
		
		String clientSettings = BufferUtils.readString(xteaBuffer);
		if (type.equals(LoginType.WORLD_PART_1)) {
			xteaBuffer.readInt();
		}
		int indexFiles = xteaBuffer.readUnsignedByte();
		int[] crcValues = new int[indexFiles];
		int crcCount = 0;
		if (type.equals(LoginType.WORLD_PART_1)) {
			crcCount = xteaBuffer.readUnsignedByte();
		}
		for (int i = 0; i < crcValues.length; i++) {
			crcValues[i] = xteaBuffer.readUnsignedByte();
		}
		
		MachineData data = new MachineData(xteaBuffer);
		
		if (type.equals(LoginType.LOBBY)) {
			xteaBuffer.readInt();// Packet receive count
			BufferUtils.readString(xteaBuffer);// Some param string (empty)
			xteaBuffer.readInt();// Another param (0)
			xteaBuffer.readInt();// Yet another param (2036537831)

			String serverToken = BufferUtils.readString(xteaBuffer);
			if (!serverToken.equals(Constants.SERVER_TOKEN)) {
				System.err.println("Expected token: "+Constants.SERVER_TOKEN+", found: "+serverToken);
				return new LoginResponse(LoginResponse.BAD_SESSION);
			}
			xteaBuffer.readByte();// Final param (2424)
		} else if (type.equals(LoginType.WORLD_PART_1)) {
			xteaBuffer.readInt();// Packet receive count
			xteaBuffer.readInt();//Unknown
			xteaBuffer.readInt();//Unknown
			BufferUtils.readString(xteaBuffer);// Some param string (empty)
			boolean hasAditionalInformation = xteaBuffer.readUnsignedByte() == 1;
			if (hasAditionalInformation) {
				BufferUtils.readString(xteaBuffer); // aditionalInformation
			}
			boolean hasJagtheora = xteaBuffer.readUnsignedByte() == 1;		
			boolean js = xteaBuffer.readUnsignedByte() == 1;
			boolean hc = xteaBuffer.readUnsignedByte() == 1;
			//int unknown4 = xteaBuffer.readByte();
			int unknown5 = xteaBuffer.readInt();
			
			String serverToken = BufferUtils.readString(xteaBuffer);
			if (!serverToken.equals(Constants.SERVER_TOKEN)) {//TODO: Figure out why this isn't decoding correctly on some computers...
				//System.err.println("Expected token: "+Constants.SERVER_TOKEN+", found: "+serverToken);
				//eturn new LoginResponse(LoginResponse.BAD_SESSION);
			}
			boolean unknown7 = xteaBuffer.readUnsignedByte() == 1;
			
			for (int index = 0; index < crcCount; index++) {
				//int crc = CacheManager.STORE.getIndexes()[index] == null ? -1011863738 : CacheManager.STORE
				//		.getIndexes()[index].getCRC();
				int receivedCRC = xteaBuffer.readInt();
				/*if (crc != receivedCRC && index < 32) {
					Logger.log(this,
					 "Invalid CRC at index: "+index+", "+receivedCRC+", "+crc);
					session.getLoginPackets().sendClientPacket(6);
					return;
				}*/
			}
		}
		if ((IOHub.getAccountIo().exists(username.trim())) == false) {
			account = new Account(new Username(StringUtils.format(username.trim(), FormatType.PROTOCOL)), new Password(password.toLowerCase().trim(), true), channel, displayMode, clientSessionKey, serverSessionKey);
		} else {
			System.out.println("loading player");
			account = IOHub.getAccountIo().load(username.trim());
			account.setChannel(channel);
			account.setDisplayMode(displayMode);
			account.setClientSessionKey(clientSessionKey);
			account.setServerSessionKey(serverSessionKey);
		}
		account.putFlag("login_type", type);
		return account;
	}

	private Object fetchPlayerData (ChannelHandlerContext ctx, ChannelBuffer buf) throws ProtocolException {
		if (account == null) {
			throw new ProtocolException("Invalid connecton: " + GAME_DATA);
		}
		//System.out.println("Received request for player data...");
		account.removeFlag("login_type");
		account.putFlag("login_type", LoginType.WORLD_PART_2);
        return account;
	}
	
	/**
	 * @param ctx
	 * @param buf
	 * @throws ProtocolException 
	 */
	/*private void decodeLobbyPayload(ChannelHandlerContext ctx, ChannelBuffer buffer) throws ProtocolException {
		int secureBufferSize = buffer.readShort() & 0xFFFF;
		if (buffer.readableBytes() < secureBufferSize) {
			throw new ProtocolException("Invalid secure buffer size.");
		}

		byte[] secureBytes = new byte[secureBufferSize];
		buffer.readBytes(secureBytes);

		ChannelBuffer secureBuffer = ChannelBuffers.wrappedBuffer(new BigInteger(
				secureBytes).modPow(Constants.JS5PrivateKey,
				Constants.JS5ModulusKey).toByteArray());
		int blockOpcode = secureBuffer.readUnsignedByte();

		if (blockOpcode != 10) {
			throw new ProtocolException("Invalid block opcode.");
		}

		int[] xteaKey = new int[4];
		for (int key = 0; key < xteaKey.length; key++) {
			xteaKey[key] = secureBuffer.readInt();
		}

		long vHash = secureBuffer.readLong();
		if (vHash != 0L) {
			throw new ProtocolException("Invalid login virtual hash.");
		}

		BufferUtils.readString(secureBuffer);

		long[] loginSeeds = new long[2];
		for (int seed = 0; seed < loginSeeds.length; seed++) {
			loginSeeds[seed] = secureBuffer.readLong();
		}

		byte[] xteaBlock = new byte[buffer.readableBytes()];
		buffer.readBytes(xteaBlock);
	}

	/**
	 * @param buf
	 * @return
	 */
	/*private Object decodeClientDetails(ChannelBuffer buffer) {
		/*if (buffer.readableBytes() < loginSize) {	
			return null;
			//throw new ProtocolException("Invalid login size. Expected: "+loginSize+", found="+buffer.readableBytes());
		}*/

		/*int version = buffer.readInt();
		int subVersion = buffer.readInt();

		if (version != Cache.SERVER_REVISION
				&& subVersion != Cache.SUB_BUILD) {
			return new LoginResponse(LoginResponse.GAME_UPDATED);
			// throw new
			// ProtocolException("Invalid client version/sub-version.");
		}

		/*
		 * if (currentLoginType.equals(LoginTypes.GAME)) { buffer.readByte(); }
		 */

		/*byte[] payload = new byte[loginSize - 8];
		buffer.readBytes(payload);
		state = LoginState.PLAYER_DATA;*/
		/*return null;// new LoginPayload(currentLoginType, payload);
	}

	/**
	 * @param buf
	 * @return
	 */
	/*private Object decodeConnectionType(ChannelBuffer buffer) {
		int loginType = buffer.readUnsignedByte();
		if (loginType != 16 && loginType != 18 && loginType != 19) {
			System.out.println("Invalid login opcode: " + loginType);
			return new LoginResponse(LoginResponse.BAD_LOGIN_PACKET);
			// throw new ProtocolException("Invalid login opcode: " + loginType);
		}

		currentLoginType = loginType == 19 ? LoginType.LOBBY : LoginType.GAME;
		loginSize = buffer.readShort() & 0xFFFF;

		state = LoginState.CLIENT_DETAILS;
		return decodeClientDetails(buffer);
	}*/
	
	/*private Object decodeHeader(ChannelHandlerContext ctx, ChannelBuffer buf) {
                new SecureRandom().nextInt();

                ChannelBuffer unpooled = ChannelBuffers.buffer(1);
                unpooled.writeByte(0);
                // unpooled.writeLong(secureKey);
                ctx.getChannel().write(unpooled);

                setState(LoginState.CONNECTION_TYPE);
		return null;
	}*/

	
	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	public LoginState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(LoginState state) {
		this.state = state;
	}
	
	
}
