package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.content.skills.SkillData;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class SkillEncoder implements PacketEncoder<SkillData> {

	@Override
	public RS3PacketBuilder buildPacket(SkillData node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(OutgoingOpcodes.SKILL_DATA_PACKET);
		buffer.putByteC(node.getSkill().getID());
		buffer.putByteS(node.getCurrentLevel());
		buffer.putInt(node.getExperience()/10);
		//System.out.println("Sending skill. Xp="+node.getExperience());
		return buffer;
	}

}
