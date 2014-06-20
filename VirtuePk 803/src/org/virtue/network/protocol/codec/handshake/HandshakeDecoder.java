package org.virtue.network.protocol.codec.handshake;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.game.config.ClientVars;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.vars.VarDomainType;
import org.virtue.network.RS2Network;
import org.virtue.network.RS3PacketTransmitter;
import org.virtue.network.protocol.codec.creation.AccountCreationDecoder;
import org.virtue.network.protocol.codec.creation.AccountCreationEncoder;
import org.virtue.network.protocol.codec.login.RS3LoginDecoder;
import org.virtue.network.protocol.codec.ondemand.JS5Decoder;
import org.virtue.network.protocol.codec.ondemand.JS5Encoder;
import org.virtue.network.protocol.codec.ondemand.XorEncoder;
import org.virtue.network.protocol.messages.VarMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.impl.GameScreenEncoder;
import org.virtue.network.protocol.packet.encoder.impl.VarEncoder;
import org.virtue.utility.DisplayMode;

/**
 * @author Taylor Moon
 *
 * @since v1.0
 */
public class HandshakeDecoder extends FrameDecoder {

	/**
	 * Constructs a {@code HandshakeDecoder} instance.
	 */
	public HandshakeDecoder() {
		super(true);
	}

	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		int opcode = 0xFF & buffer.readByte();
		HandshakeType handshakeType = new HandshakeType(opcode);
		System.out.println(opcode);
		switch (handshakeType.getType()) {
		case HANDSHAKE_LOGIN:
			channel.write(validateResponse());
			channel.getPipeline().addBefore("upHandler", "loginDecoder", new RS3LoginDecoder());
			break;
		case HANDSHAKE_ONDEMAND:
			channel.getPipeline().addFirst("encoder", new JS5Encoder());
			channel.getPipeline().addFirst("xor-encoder", new XorEncoder());
			channel.getPipeline().addBefore("upHandler", "updateDecoder", new JS5Decoder());
			break;
		case HANSHAKE_CREATION:
			sendCreationVarps(channel);
			channel.getPipeline().addFirst("encoder", new AccountCreationEncoder());
			channel.getPipeline().addBefore("upHandler", "accountDecoder", new AccountCreationDecoder());
		default:
			break;
		}
		channel.getPipeline().remove(HandshakeDecoder.class);
		return buffer.readable() ? new Object[] { handshakeType, buffer.readBytes(buffer.readableBytes()) } : handshakeType;
	}

	/**
	 * Validates the login response.
	 */
	private ChannelBuffer validateResponse() {
		ChannelBuffer channelBuffer = ChannelBuffers.buffer(1);
		channelBuffer.writeByte(0);
		return channelBuffer;
	}
	
	private void sendCreationVarps(Channel channel) {
		ChannelBuffer channelBuffer = ChannelBuffers.buffer(1);
		channelBuffer.writeByte(0);
		channel.write(channelBuffer);
		RS3PacketBuilder packet = new RS3PacketBuilder();
		packet.putPacket(OutgoingOpcodes.RESET_VARP_CACHE);
		RS3PacketTransmitter.processPacket(packet, channel);
		int[] varps = ClientVars.getCreationVarps();
		for (int i = 0; i < varps.length; i++) {
			if (varps[i] != 0) {
				RS3PacketBuilder varPacket = RS2Network.getEncoders().get(VarEncoder.class).buildPacket(new VarMessage(VarDomainType.CLIENT, i, varps[i]));
				RS3PacketTransmitter.processPacket(varPacket, channel);
			}
		}
		RS3PacketBuilder pane = RS2Network.getEncoders().get(GameScreenEncoder.class).buildPacket(DisplayMode.ACCOUNT_CREATION);
		RS3PacketTransmitter.processPacket(pane, channel);
	}
}
