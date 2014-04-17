package org.virtue.network.protocol.handlers.impl;

import org.virtue.Launcher;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.session.impl.WorldSession;

public class MessageHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		String message = getFlag("message", "");
		int colourEffect = getFlag("colourEffect", 0);
		int moveEffect = getFlag("moveEffect", 0);

		System.out.println("Received message: message="+message+", type="+session.getPlayer().getChatType()+", colour="+colourEffect+", move="+moveEffect);
		
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarByte(158);//TODO: Encode this in the proper method
		buffer.putShort(session.getPlayer().getIndex());
		buffer.putShort(0);
		buffer.put(0);
		Launcher.getHuffman().huffmanEncrypt(buffer, message);
		buffer.endPacketVarByte();
		session.getTransmitter().send(buffer);
	}

}
