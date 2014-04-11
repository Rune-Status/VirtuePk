package org.virtue.game.node.entity.player.identity;

/**
 * @author Taylor
 * @date Jan 15, 2014
 */
public class Password {

	/**
	 * Represents the password.
	 */
	private String password;
	
	/**
	 * Represents if the password is encrypted.
	 */
	private boolean encrypt;

	/**
	 * @return the encrypt
	 */
	public boolean isEncrypt() {
		return encrypt;
	}

	/**
	 * Constructs a new {@code Password.java}.
	 * @param password The password.
	 * @param encrypt Encrypt.
	 */
	public Password(String password, boolean encrypt) {
		this.password = password;
		this.encrypt = encrypt;
	}
	
	/**
	 * @param encrypt the encrypt to set
	 */
	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
