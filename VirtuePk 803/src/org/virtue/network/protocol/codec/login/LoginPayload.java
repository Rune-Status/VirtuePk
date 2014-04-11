package org.virtue.network.protocol.codec.login;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 5, 2014
 */
public class LoginPayload {
	
	public static enum LoginType {
		LOBBY(19), GAME(16);
		
		public final int opcode;
		
		LoginType (int opcode) {
			this.opcode = opcode;
		}
		
		public static LoginType getType(int opcode) {
			switch (opcode) {
				case 19:
					return LOBBY;
				case 16:
					return GAME;
				default:
					return null;
			}
		}
	}
	
	private byte[] payload;
	private org.virtue.network.protocol.codec.login.LoginType type;
	
	public LoginPayload (org.virtue.network.protocol.codec.login.LoginType type, byte[] payload) {
		this.type = type;
		this.payload = payload;
	}
	
	public org.virtue.network.protocol.codec.login.LoginType getType() {
		return this.type;
	}
	
	public byte[] getPayload() {
		return this.payload;
	}

}
