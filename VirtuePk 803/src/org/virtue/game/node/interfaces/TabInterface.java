package org.virtue.game.node.interfaces;

import org.virtue.game.node.entity.player.Player;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.InterfaceMessage;
import org.virtue.network.protocol.messages.InterfaceSettingsMessage;
import org.virtue.network.protocol.packet.encoder.impl.InterfaceSettingsEncoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 15, 2014
 */
public abstract class TabInterface {
	
	public static final int UNLOCK_SCRIPT = 8862;
	
	private final int interfaceID;
	
	/**
	 * Represents the player.
	 */
	private final Player player;
	
	public TabInterface (int id, Player p) {
		this.interfaceID = id;
		this.player = p;
	}
	
	/**
	 * Returns the ID for this interface
	 * @return	The ID
	 */
	public int getID () {
		return interfaceID;
	}
	
	/**
	 * Returns the player associated with this interface
	 * @return	The player
	 */
	public Player getPlayer () {
		return player;
	}
	
	/**
	 * Send this interface to the player
	 * @param parentID			The ID of the interface to attach this to
	 * @param parentComponent	The component of the interface to attach this to
	 * @param clipped			Whether or not the interface is "walkable" (ie the player can move with the interface open)
	 */
	public void send (int parentID, int parentComponent, boolean clipped) {
		player.getPacketDispatcher().dispatchInterface(new InterfaceMessage(interfaceID, parentComponent, parentID, clipped));
		sendInitData();
	}
	
	/**
	 * Called after the interface has been sent
	 */
	public abstract void sendInitData ();
	
	/**
	 * Called whenever the player clicks on a component on the interface
	 * @param componentID	The id of the component
	 * @param slotID1		The first custom slot ID
	 * @param slotID2		The second custom slot ID
	 * @param button		The {@link ActionButton} that was clicked
	 */
	public abstract void handleActionButton (int componentID, int slotID1, int slotID2, ActionButton button);
	
	public void setLock (boolean isLocked) {
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(UNLOCK_SCRIPT, getTabID(), (isLocked ? 0 : 1)));
	}
	
	public abstract int getTabID ();
	

	public void sendInterfaceSettings(int component, int fromSlot, int toSlot, int settings) {
		player.getAccount().getSession().getTransmitter().send(InterfaceSettingsEncoder.class, new InterfaceSettingsMessage(interfaceID, component, fromSlot, toSlot, settings));
	}
}
