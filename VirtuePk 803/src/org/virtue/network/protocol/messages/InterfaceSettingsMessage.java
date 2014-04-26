package org.virtue.network.protocol.messages;

public class InterfaceSettingsMessage {
	
	public enum InterfaceSettingsType {SLOT, CLOSE, STRING, HIDECOMP}
	
	private final int interfaceHash;
	private int fromSlot;
	private int toSlot;
	private int settingsHash;
	private String settingsString;
	
	private final InterfaceSettingsType type;
	
	public InterfaceSettingsMessage (int iFaceID, int component, int from, int to, int settings) {
		this.interfaceHash = iFaceID << 16 | component;
		this.fromSlot = from;
		this.toSlot = to;
		this.settingsHash = settings;
		type = InterfaceSettingsType.SLOT;
	}
	
	public InterfaceSettingsMessage (int parentHash) {
		this.interfaceHash = parentHash;
		type = InterfaceSettingsType.CLOSE;
	}
	
	public InterfaceSettingsMessage (int interfaceID, int component, boolean hidden) {
		this.interfaceHash = interfaceID << 16 | component;
		settingsHash = (hidden ? 1 : 0);
		type = InterfaceSettingsType.HIDECOMP;
	}
	
	public InterfaceSettingsMessage (int interfaceID, int component, String settings) {
		this.interfaceHash = interfaceID << 16 | component;
		this.settingsString = settings;
		type = InterfaceSettingsType.STRING;
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
	
	public String getComponentText () {
		return settingsString;
	}
	
	public InterfaceSettingsType getType () {
		return type;
	}
}
