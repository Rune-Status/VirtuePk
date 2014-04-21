package org.virtue.game.logic.social;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public enum ChatType {
	
	PUBLIC(0),
	FRIENDS(1),
	CLAN(2),
	GUEST_CLAN(3),
	PRIVATE(4);
	
	private int code;
	
	ChatType(int code) {
		this.code = code;
	}
	
	public int getCode () {
		return code;
	}
	
	public static ChatType forCode (int code) {
		if (code < ChatType.values().length) {
			ChatType type = ChatType.values()[code];
			if (type.code == code) {
				return type;
			}
		}
		for (ChatType t : ChatType.values()) {
			if (t.code == code) {
				return t;
			}
		}
		return null;
	}
}
