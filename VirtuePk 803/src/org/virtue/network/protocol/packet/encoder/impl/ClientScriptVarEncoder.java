package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Belthazar
 */
public class ClientScriptVarEncoder implements PacketEncoder<ClientScriptVar> {

	@Override
	public RS3PacketBuilder buildPacket(ClientScriptVar script) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.RUN_CS2_PACKET);
		buffer.putString(script.getVariableString());
		if (script.getParameters() != null) {
			int parameter = 0;
			for (int var = script.getParameters().length - 1; var >= 0; var--) {
				if (script.getVariableString().charAt(var) == 's') {
					buffer.putString((String) script.getParameters()[parameter++]);
				} else {
					buffer.putInt((Integer) script.getParameters()[parameter++]);
				}
			}
		}
		buffer.putInt(script.getOpcode());
		buffer.endPacketVarShort();
		return buffer;
	}
}
