package org.virtue.game.logic.node.interfaces;

import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.InterfaceMessage;
import org.virtue.network.protocol.messages.InterfaceSettingsMessage;
import org.virtue.network.protocol.packet.encoder.impl.ClientScriptVarEncoder;
import org.virtue.network.protocol.packet.encoder.impl.InterfaceSettingsEncoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 15, 2014
 */
public abstract class AbstractInterface {
    
	public static final int UNLOCK_SCRIPT = 8862;
	
	private final int interfaceID;
	
	private int parentComponent;
	
	/**
	 * Represents the player.
	 */
	protected final Player player;
	
	public AbstractInterface (int id, Player p) {
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
		this.parentComponent = parentComponent;
		player.getPacketDispatcher().dispatchInterface(new InterfaceMessage(interfaceID, parentComponent, parentID, clipped));
		postSend();
		setLock(false);
	}
	
	public void close () {
		player.getAccount().getSession().getTransmitter().send(InterfaceSettingsEncoder.class, new InterfaceSettingsMessage(parentComponent));
	}
	
	/**
	 * Called after the interface has been sent
	 */
	public abstract void postSend ();
	
	/**
	 * Called whenever the player clicks on a component on the interface
	 * @param component	The id of the component
	 * @param slot1		The first custom slot ID
	 * @param slot2		The second custom slot ID
	 * @param button	The {@link ActionButton} that was clicked
	 */
	public abstract void handleActionButton (int component, int slot1, int slot2, ActionButton button);
	
	public Item getItem (int component, int itemID, int slotID) {
		return null;
	}
	
	public void setLock (boolean isLocked) {
		if (getTabID() == -1) {
			return;
		}
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(UNLOCK_SCRIPT, (isLocked ? 0 : 1), getTabID()));
	}
	
	public abstract int getTabID ();//Skills=0, Active task=1, Backpack=2, Equipment=3, Prayer Abilities=4
	//Magic Abilities=5 (also does other abilities), Melee Abilities=6, Ranged Abilities=7, Defence Abilities=8
	//Emotes=9, Music Player=10, Notes=11, Familiar=12, Friends=14, Friends Chat Info=15, Minigames=17,
	//Clan=16, Private Chat=19, All Chat=18, Clan Chat=21, Friends Chat=20, Trade and Assistance=23,
	//Guest Clan Chat=22
	//Action Bar=1004
	

	public void sendInterfaceSettings(int component, int fromSlot, int toSlot, int settings) {
		player.getAccount().getSession().getTransmitter().send(InterfaceSettingsEncoder.class, new InterfaceSettingsMessage(interfaceID, component, fromSlot, toSlot, settings));
	}
	
	public void setComponentText (int component, String text) {
		player.getAccount().getSession().getTransmitter().send(InterfaceSettingsEncoder.class, new InterfaceSettingsMessage(interfaceID, component, text));
	}
	
	public void setComponentHidden (int component, boolean hidden) {
		player.getAccount().getSession().getTransmitter().send(InterfaceSettingsEncoder.class, new InterfaceSettingsMessage(interfaceID, component, hidden));
	}
}
