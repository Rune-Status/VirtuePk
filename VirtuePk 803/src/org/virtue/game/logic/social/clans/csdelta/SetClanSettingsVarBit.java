package org.virtue.game.logic.social.clans.csdelta;

import org.virtue.network.protocol.packet.RS3PacketBuilder;

public class SetClanSettingsVarBit implements ClanSettingsDelta {
	
	private final int key;
	private final int value;
	private final int start;
	private final int end;

	public SetClanSettingsVarBit (int key, int value, int start, int end) {
		this.key = key;
		this.value	= value;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void packDelta(RS3PacketBuilder buffer) {
		buffer.putInt(key);
		buffer.putInt(value);
		buffer.put(start);
		buffer.put(end);
	}

	@Override
	public int getTypeID() {
		return 11;
	}

}
