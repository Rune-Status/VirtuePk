package org.virtue.network.messages;

public class InterfaceSettingsMessage {
	
	private final int interfaceHash;
	private final int fromSlot;
	private final int toSlot;
	private final int settingsHash;
	
	public InterfaceSettingsMessage (int iFaceID, int component, int from, int to, int settings) {
		this.interfaceHash = iFaceID << 16 | component;
		this.fromSlot = from;
		this.toSlot = to;
		this.settingsHash = settings;
	}
	
	/**
	 * 
	 * @return The interface hash
	 */
	public int getInterface () {
		return interfaceHash;
	}
	
	public int getFromSlot () {
		return fromSlot;
	}
	
	public int getToSlot () {
		return toSlot;
	}
	
	public int getSettings () {
		return settingsHash;
	}
}
