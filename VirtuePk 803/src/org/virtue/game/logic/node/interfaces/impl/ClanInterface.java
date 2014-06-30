package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.Launcher;
import org.virtue.game.logic.events.InputEnteredEvent;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RS3Interface;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class ClanInterface extends AbstractInterface {

	public ClanInterface(Player p) {
		super(RS3Interface.CLAN, p);
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(20, 0, 200, 2);//IfaceSettings: 72744980, 200, 0, 2
		sendInterfaceSettings(25, 0, 600, 2);//IfaceSettings: 72744985, 600, 0, 2
		sendInterfaceSettings(23, 0, 600, 1024);//IfaceSettings: 72744983, 600, 0, 1024
		sendInterfaceSettings(14, 0, 600, 1024);//IfaceSettings: 72744974, 600, 0, 1024
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 78:
		case 80:
		case 82://These indicate changing tabs on the clan interface. No need to worry about them...
			break;
		case 106://Join/leave main clan chat
			if (player.getChatManager().inClanChannel()) {
				player.getChatManager().leaveClanChannel(false);
			} else {
				if (player.getChatManager().getMyClanHash() == 0L) {
					player.getPacketDispatcher().dispatchMessage("You must be a member of a clan in order to join their channel.", MessageOpcode.CLAN_SYSTEM);
				} else {
					player.getChatManager().joinMyClanChannel();
				}
			}
			break;
		case 113://Join/leave guest cc
			if (player.getChatManager().getGuestClanHash() != 0L) {
				player.getChatManager().leaveClanChannel(true);
			} else {
				getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.DIALOG_BOX);
				getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.INPUT_DIALOG);
				getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
				getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(110, "Please enter the name of the clan to chat in:"));
				getPlayer().setInputEvent(onJoinGuestClan);
			}
			break;
		case 104://Clan settings
			if (player.getChatManager().getMyClanHash() == 0L) {
				player.getPacketDispatcher().dispatchMessage("You're not in a clan.", MessageOpcode.CLAN_SYSTEM);
			} else {
				getPlayer().getInterfaces().setTopInterface(new ClanSettingsInterface(player, player.getChatManager().getMyClanHash()));
			}
			break;
		case 124://Leave clan
			if (Launcher.getClanManager().leaveMyClan(player.getChatManager().getSocialUser())) {
				player.getPacketDispatcher().dispatchMessage("You have successfully left your clan.", MessageOpcode.CLAN_SYSTEM);
			}
			break;
		case 103://Clan info
			if (player.getChatManager().getMyClanHash() == 0L) {
				player.getPacketDispatcher().dispatchMessage("You're not in a clan.", MessageOpcode.CLAN_SYSTEM);
			}
		case 153://Clan noticeboard functions
		case 135://Check resources
		case 120://Kick/ban guest
		case 25://Clanmate options
		case 34://Close clanmate options
		case 114://Add clan ban
		case 116://Remove clan ban
		default:
			System.out.println("Unhandled clan button: component="+component+", button="+button+", slot1="+slot1+", slot2="+slot2);
			break;
		}		
	}
	
	@Override
	public void handleInterfaceOnPlayer (Player target, int component, int slot1, int slot2) {
		if (component == 110) {//TODO: Show the proper recruitment dialogs
			if (!Launcher.getClanManager().joinClan(player.getChatManager().getSocialUser(), target.getChatManager().getSocialUser())) {
				player.getPacketDispatcher().dispatchMessage("Error - Could not recruit clan member (already in a clan).");
			}
		} else {
			super.handleInterfaceOnPlayer(target, component, slot1, slot2);
		}
	}
	
	private InputEnteredEvent onJoinGuestClan = new InputEnteredEvent () {
		@Override
		public void onInputEntered(Object input) {
			String clanName = (String) input;
			player.getChatManager().joinGuestChannel(clanName);
		}
		@Override
		public void onCancel() { }		
	};

	@Override
	public int getTabID() {
		return 16;
	}

}
