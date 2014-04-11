package org.virtue.network.messages;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 5, 2014
 */
public class LoginConfigData {
	

	private int[] configData;
	private boolean aBoolean;
	
	public LoginConfigData (int[] configData, boolean aBoolean) {
		this.configData = configData;
		this.aBoolean = aBoolean;
	}
	
	public int[] getConfigData () {
		return configData;
	}
	
	public boolean getUnknownBoolean () {
		return aBoolean;
	}

}
