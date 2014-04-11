package org.virtue.network.protocol.codec.login;

import java.math.BigInteger;
import java.net.ProtocolException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.cache.Cache;
import org.virtue.game.node.entity.player.identity.Account;
import org.virtue.game.node.entity.player.identity.Password;
import org.virtue.game.node.entity.player.identity.Username;
import org.virtue.utility.Base37Utils;
import org.virtue.utility.BufferUtils;
import org.virtue.utility.DisplayMode;
import org.virtue.utility.XTEA;

/**
 * @author Taylor
 * @version 1.0
 */
public class RS2LoginDecoder extends FrameDecoder implements ChannelHandler {

	/**
	 * Represents the RSA private key for player detail encryption
	 */
	public static final BigInteger RSA_EXPONENT = new BigInteger("90587072701551327129007891668787349509347630408215045082807628285770049664232156776755654198505412956586289981306433146503308411067358680117206732091608088418458220580479081111360656446804397560752455367862620370537461050334224448167071367743407184852057833323917170323302797356352672118595769338616589092625");
	
	/**
	 * Represents the RSA modulus key for player detail encryption.
	 */
	public static final BigInteger RSA_MODULUS = new BigInteger("102876637271116124732338500663639643113504464789339249490399312659674772039314875904176809267475033772367707882873773291786014475222178654932442254125731622781524413208523465520758537060408541610254619166907142593731337618490879831401461945679478046811438574041131738117063340726565226753787565780501845348613");
	
	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		LoginType type;
		if (!buffer.readable()) {
			throw new ProtocolException("Bad buffer state.");
		}
		int connectionRequest = buffer.readByte() & 0xFF;
		if (connectionRequest != 16 && connectionRequest != 19 && connectionRequest != 18) {
			throw new ProtocolException("Invalid connecton: " + connectionRequest);
		}
		type = (connectionRequest == 19 ? LoginType.LOBBY : LoginType.WORLD_PART_2);
		buffer.readShort();
		int version = buffer.readInt();
		int subVersion = buffer.readInt();
		if (version != Cache.SERVER_REVISION && subVersion != Cache.SUB_BUILD) {
			throw new ProtocolException("Client out of date.");
		}
		if (type.equals(LoginType.WORLD_PART_2)) {
			buffer.readByte();
		}
		int readableBytes = buffer.readShort() & 0xFFFF;
		if (buffer.readableBytes() < readableBytes) {
			throw new ProtocolException("Invalid buffer size: " + readableBytes);
		}
		byte[] encryptedBytes = new byte[readableBytes];
		buffer.readBytes(encryptedBytes);
		ChannelBuffer rsaBuffer = ChannelBuffers.wrappedBuffer(new BigInteger(encryptedBytes).modPow(RSA_EXPONENT, RSA_MODULUS).toByteArray());
		int magicNumber = rsaBuffer.readByte() & 0xFF;
		if (magicNumber != 10) {
			throw new ProtocolException("Invalid magic number: " + magicNumber);
		}
		int[] xteaSeeds = new int[4];
		for (int i = 0; i < xteaSeeds.length; i++) {
			xteaSeeds[i] = rsaBuffer.readInt();
		}
		long verifivation = rsaBuffer.readLong();
		if (verifivation != 0) {
			throw new ProtocolException("Cannot verify \"" + verifivation + "\".");
		}
		String password = BufferUtils.readString(rsaBuffer);
		long clientSessionKey = rsaBuffer.readLong();
		long serverSessionKey = rsaBuffer.readLong();
		byte[] xteaData = new byte[buffer.readableBytes()];
		buffer.readBytes(xteaData);
		ChannelBuffer xteaBuffer = ChannelBuffers.wrappedBuffer(XTEA.decipher(xteaSeeds, xteaData));
		boolean unEncrypted = xteaBuffer.readByte() == 1;
		String username = unEncrypted ? BufferUtils.readString(xteaBuffer) : Base37Utils.decodeBase37(xteaBuffer.readLong());
		DisplayMode displayMode = DisplayMode.forId(xteaBuffer.readByte());
		xteaBuffer.readByte();
		if (type.equals(LoginType.WORLD_PART_2)) {
			xteaBuffer.readShort();
			xteaBuffer.readShort();
		}
		xteaBuffer.skipBytes(24);
		switch (type) {
		case LOBBY:
			BufferUtils.readString(xteaBuffer);
			int indexFiles = xteaBuffer.readByte() & 0xFF;
			int[] crcValues = new int[indexFiles];
			for (int i = 0; i < crcValues.length; i++)
				crcValues[i] = xteaBuffer.readInt();
			break;
		case WORLD_PART_2:
			BufferUtils.readString(xteaBuffer);
			xteaBuffer.readInt();
			xteaBuffer.skipBytes(xteaBuffer.readByte() & 0xFF);
			break;
		default:
			break;
		}
		Account account = new Account(new Username(username.toLowerCase().trim()), new Password(password.toLowerCase().trim(), true), channel, displayMode, clientSessionKey, serverSessionKey);
		account.putFlag("login_type", type);
		return account;
	}
}
