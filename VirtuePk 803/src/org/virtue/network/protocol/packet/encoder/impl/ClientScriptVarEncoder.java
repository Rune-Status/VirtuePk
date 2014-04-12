package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.network.messages.ClientScriptVar;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Taylor Moon
 * @since Jan 28, 2014
 */
public class ClientScriptVarEncoder implements PacketEncoder<ClientScriptVar> {

	@Override
	public RS3PacketBuilder buildPacket(ClientScriptVar script) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.RUN_CS2_PACKET);
		/*
		 * The variable string is the line of text that contains variable
		 * markers that are used for the client to know what data to expect, and
		 * in what order. This is used to decode either string or integer
		 * variables from the parameters.
		 */
		buffer.putString(script.getVariableString());
		if (script.getParameters() != null) {
			int parameter = 0;
			for (int var = script.getParameters().length - 1; var >= 0; var--) {
				if (script.getVariableString().charAt(var) == 's') {
					/*
					 * Puts a string-variable parameter in reverse byte order so
					 * the client can decode it descendingly.
					 */
					buffer.putString((String) script.getParameters()[parameter++]);
				} else {
					/*
					 * Puts an int-variable parameter in reverse byte order so
					 * the client can decode it descendingly.
					 */
					buffer.putInt((Integer) script.getParameters()[parameter++]);
				}
			}
		}
		/*
		 * Lets the client know what script to execute.
		 */
		buffer.putInt(script.getOpcode());
		buffer.endPacketVarShort();
		return buffer;
	}
}
