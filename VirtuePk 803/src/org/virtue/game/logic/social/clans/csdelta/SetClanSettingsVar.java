package org.virtue.game.logic.social.clans.csdelta;

import org.virtue.network.protocol.packet.RS3PacketBuilder;

public class SetClanSettingsVar implements ClanSettingsDelta {
	
	private enum Type {INTEGER, STRING, LONG, INVALID};
	
	private final int key;
	private final Object value;
	private final Type type;
	
	public SetClanSettingsVar (int key, Object value) {
		this.key = key;
		this.value = value;
		Type type = Type.INVALID;
		if (value instanceof Integer) {
			type = Type.INTEGER;
		} else if (value instanceof String) {
			type = Type.STRING;
		} else if (value instanceof Long) {
			type = Type.LONG;
		}
		this.type = type;
	}

	@Override
	public void packDelta(RS3PacketBuilder buffer) {
		buffer.putInt(key);
		switch (type) {
		case INTEGER:
			buffer.putInt((Integer) value);
			break;
		case STRING:
			buffer.putString((String) value);
			break;
		case LONG:
			buffer.putLong((Long) value);
			break;
		case INVALID:
			throw new RuntimeException("Attempted to pack invalid VarClanSettings");
		}
	}

	@Override
	public int getTypeID() {
		switch (type) {
		case INTEGER:
			return 8;
		case STRING:
			return 10;
		case LONG:
			return 9;
		default:
			return -1;
		}
	}

}
