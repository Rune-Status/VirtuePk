package org.virtue.game.node.entity.player.screen;

import org.virtue.game.node.entity.player.Player;
import org.virtue.network.messages.InterfaceMessage;
import org.virtue.network.messages.VarpMessage;
import org.virtue.network.protocol.packet.encoder.impl.r803.InterfaceEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.GameScreenEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.VarpEncoder;
import org.virtue.utility.DisplayMode;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class InterfaceManager {
	/**
	 * Represents the player.
	 */
	private Player player;
	
	/**
	 * Constructs a new {@code InterfaceManager.java}.
	 * @param player The player.
	 */
	public InterfaceManager(Player player) {
		this.player = player;
	}
	
	/**
	 * Represenst if the screen is resizable.
	 */
	private boolean resizableScreen = false;
	
	/**
	 * Sends the game screen.
	 */
	public void sendScreen() {
		player.getAccount().getSession().getTransmitter().send(GameScreenEncoder.class, player.getAccount().getDisplayMode());
		if (player.getAccount().getDisplayMode().equals(DisplayMode.FIXED)) {
			sendTab(161, 752);
			sendTab(37, 751);
			sendTab(23, 745);
			sendTab(25, 754);
			sendTab(155, 747); 
			sendTab(151, 748);
			sendTab(152, 749);
			sendTab(153, 750);
			sendInterface(true, 752, 9, 137);
			sendMagicBook();
			sendPrayerBook();
			sendEquipment();
			sendInventory();
			sendTab(174, 190);//quest
			sendTab(181, 1109);// 551 ignore now friendchat
			sendTab(182, 1110);// 589 old clan chat now new clan chat
			sendTab(180, 550);// friend list
			sendTab(185, 187);// music
			sendTab(186, 34); // notes
			sendTab(189, 182);
			sendSkills();
			sendEmotes();
			sendSettings();
			sendTaskSystem();
			sendCombatStyles();
		} else {
			sendTab(21, 752);
			sendTab(22, 751);
			sendTab(15, 745);
			sendTab(25, 754);
			sendTab(195, 748); 
			sendTab(196, 749);
			sendTab(197, 750);
			sendTab(198, 747); 
			sendInterface(true, 752, 9, 137);
			sendCombatStyles();
			sendTaskSystem();
			sendSkills();
			sendTab(114, 190);
			sendInventory();
			sendEquipment();
			sendPrayerBook();
			sendMagicBook();
			sendTab(120, 550); // friend list
			sendTab(121, 1109); // 551 ignore now friendchat
			sendTab(122, 1110); // 589 old clan chat now new clan chat
			sendSettings();
			sendEmotes();
			sendTab(125, 187); // music
			sendTab(126, 34); // notes
			sendTab(129, 182); // logout*/
		}
		player.getAccount().getSession().getTransmitter().send(VarpEncoder.class, new VarpMessage(823, 1, true));
	}
	
	public void sendTab(int tabId, int interfaceId) {
		DisplayMode mode = player.getAccount().getDisplayMode();
		sendInterface(true, mode.equals(DisplayMode.FIXED) ? 548 : 746, tabId, interfaceId);
	}
	
	/**
	 * Sends an interface.
	 * @param walkable If the interface is walkable.
	 * @param windowId The window ID.
	 * @param windowLocation The window location.
	 * @param interfaceId The interface ID.
	 */
	public void sendInterface(boolean walkable, int windowId, int windowLocation, int interfaceId) {
		player.getAccount().getSession().getTransmitter().send(InterfaceEncoder.class, new InterfaceMessage(interfaceId, windowLocation, windowId, walkable));
	}
	
	public void sendXPPopup() {
		sendTab(resizableScreen ? 38 : 10, 1213); //xp 
	}
	
	public void sendXPDisplay() {
		sendXPDisplay(1215);  //xp counter
	}
	
	public void sendXPDisplay(int interfaceId) {
		sendTab(resizableScreen ? 27 : 29, interfaceId);  //xp counter
	}
	
	public void sendEquipment() {
		sendTab(resizableScreen ? 116 : 176, 387);
	}

	public void sendInventory() {
		sendTab(resizableScreen ? 115 : 175, 679);
	}
	
	public void sendCombatStyles() {
		sendTab(resizableScreen ? 111 : 171, 884);
	}
	
	public void sendTaskSystem() {
		sendTab(resizableScreen ? 112 : 172, 1056);
	}

	public void sendSkills() {
		sendTab(resizableScreen ? 113 : 173, 320);
	}

	public void sendSettings() {
		sendSettings(261);
	}

	public void sendSettings(int interfaceId) {
		sendTab(resizableScreen ? 123 : 183, interfaceId);
	}

	public void sendPrayerBook() {
		sendTab(resizableScreen ? 117 : 177, 271);
	}

	public void sendMagicBook() {
		sendTab(resizableScreen ? 118 : 178, 192);
	}
	
	public void sendEmotes() {
		sendTab(resizableScreen ? 124 : 184, 590);
	}
}
