package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RS3Interface;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class ClanBroadcastSettings extends AbstractInterface {
	
	private final ClanSettingsInterface settingsInterface;

	public ClanBroadcastSettings(Player p, ClanSettingsInterface settings) {
		super(RS3Interface.CLAN_BROADCAST_SETTINGS, p);
		this.settingsInterface = settings;
	}

	@Override
	public void postSend() {
		player.getVarManager().setVarPlayer(4285, -1);
		player.getVarManager().setVarPlayer(4276, 16843009);
		player.getVarManager().setVarPlayer(4277, 16843009);
		player.getVarManager().setVarPlayer(4278, 16843009);
		player.getVarManager().setVarPlayer(4279, 16843009);
		player.getVarManager().setVarPlayer(4280, 16843009);
		player.getVarManager().setVarPlayer(4281, 16843009);
		player.getVarManager().setVarPlayer(4282, 16843009);
		player.getVarManager().setVarPlayer(4283, 257);
		player.getVarManager().setVarPlayer(4284, 0);
		player.getPacketDispatcher().dispatchClientScriptVar(8178);//Run CS2: [8178]
		sendInterfaceSettings(59, 0, 126, 2);//IfaceSettings: interface=573, compID=59, fromSlot=0, toSlot=126, settings=2
		sendInterfaceSettings(5, 0, 30, 2);//IfaceSettings: interface=573, compID=5, fromSlot=0, toSlot=30, settings=2
		sendInterfaceSettings(20, 0, 30, 2);//IfaceSettings: interface=573, compID=20, fromSlot=0, toSlot=30, settings=2
		sendInterfaceSettings(19, 0, 6, 2);//IfaceSettings: interface=573, compID=19, fromSlot=0, toSlot=6, settings=2
	}
	
	@Override
	public void close() {
		super.close();
		//TODO: Re-open clan settings
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 71://Enable all
			player.getVarManager().setVarPlayer(4286, 28);//CLIENT_VARPS_803[4286] = 28;
			player.getVarManager().setVarPlayer(4285, -1);//CLIENT_VARPS_803[4285] = -1;
		case 65://Disable all
		case 34://Save button pressed
		case 47://Save confirmed
		case 53://Save canceled
		case 19://Category full enable/disable
		case 20://Specific enable/disable
		case 5://Clicked rank setting
		case 59://Selected rank setting
		case 30://Window closed
		default:
			System.out.println("Unhandled clan broadcast settings button: component="+component+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
			break;
		}
	}

	@Override
	public int getTabID() {
		return -1;
	}
	
}
