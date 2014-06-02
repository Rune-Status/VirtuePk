package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.game.logic.social.clans.ClanMember;
import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.game.logic.social.internal.InternalSocialUser;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.VarMessage;
import org.virtue.network.protocol.messages.VarcStringMessage;

public class ClanSettingsInterface extends AbstractInterface {
	
	public static enum Tab {CLANMATES, SETTINGS, PERMISSIONS};
	
	public static enum PermissionTab {ADMIN,CHAT,EVENTS,CITADEL,SKILL};
	
	private Tab currentTab = Tab.CLANMATES;
	
	private PermissionTab permTab = PermissionTab.ADMIN;
	
	private ClanMember selectedMember = null;

	public ClanSettingsInterface(Player p) {
		super(RSInterface.CLAN_SETTINGS, p);
	}	

	@Override
	public void postSend() {
		player.getChatManager().setClanSettings(this);
		sendInterfaceSettings(46, 0, 5, 2);//IfaceSettings: interface=1096, compID=46, fromSlot=0, toSlot=5, settings=2
		setComponentHidden(223, false);//Received component hidden: iFace=1096, compID=223, hidden=0
		setComponentHidden(208, false);//Received component hidden: iFace=1096, compID=208, hidden=0
		sendInterfaceSettings(282, 0, 126, 2);//IfaceSettings: interface=1096, compID=282, fromSlot=0, toSlot=126, settings=2
		sendInterfaceSettings(268, 0, 225, 2);//IfaceSettings: interface=1096, compID=268, fromSlot=0, toSlot=225, settings=2
		sendInterfaceSettings(372, 0, 127, 2);//IfaceSettings: interface=1096, compID=372, fromSlot=0, toSlot=127, settings=2
		sendInterfaceSettings(246, 0, 144, 2);//IfaceSettings: interface=1096, compID=246, fromSlot=0, toSlot=144, settings=2
		sendInterfaceSettings(296, 1, 201, 2);//IfaceSettings: interface=1096, compID=296, fromSlot=1, toSlot=201, settings=2
		sendInterfaceSettings(213, 0, 3, 2);//IfaceSettings: interface=1096, compID=213, fromSlot=0, toSlot=3, settings=2
		sendInterfaceSettings(228, 0, 4, 2);//IfaceSettings: interface=1096, compID=228, fromSlot=0, toSlot=4, settings=2
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 119:
			setTab(Tab.CLANMATES);
			break;
		case 126:
			setTab(Tab.SETTINGS);
			break;
		case 390:
			setTab(Tab.PERMISSIONS);
			break;
		/*Clanmates tab*/
		case 372://Clan member rank filter
			System.out.println("Selected clan member rank filter. Rank="+ClanRank.forID(slot1-1));
			break;
		case 46://Arrow beside member
			player.getChatManager().getClanData().sendMemberInfo(new InternalSocialUser(player), slot1);
			break;
		case 282://Clan member rank select
		case 268://Clan member job select
		case 63://Clan member ban from citadel
		case 67://Clan member ban from keep
		case 71://Clan member ban from island
		case 324://Clan member save
		case 315://Clan member kick
			System.out.println("Unhandled clan member button: component="+component+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
			break;
		/*Clan permissions tab*/
		case 399://Recruit
			player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(5136, 1));//Admin - Recruit
			break;
		case 407://Corporal
			
			break;
		case 415://Sergeant
		case 423://Lieutenant
		case 431://Captain
		case 439://General
		case 447://Admin
		case 455://Organiser
		case 463://Coordinator
		case 471://Overseer
		case 479://Deputy Owner
			player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(5136, 1));
			setPermissionGroup(ClanRank.ADMIN);
			break;
		case 859://Clan broadcast settings
			getPlayer().getInterfaces().setTopInterface(new ClanBroadcastSettings(player, this), 71);
			break;
		case 137://Clan motto editor
		case 130://Clan motif editor
		case 166://Clan keyword editor
		default:
			System.out.println("Unhandled clan settings button: component="+component+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
			break;
		}		
	}
	
	public void editMember (ClanMember member) {
		/*player.getPacketDispatcher().dispatchVarp(new VarMessage(1845, 131));
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1846, 0));
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1845, 32768));
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1500, 1, true));//Received VarClient: key=1500, value=1
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1501, 131, true));//Received VarClient: key=1501, value=131
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1564, 0, true));//Received VarClient: key=1564, value=0
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1566, 0, true));//Received VarClient: key=1566, value=0
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1565, 0, true));//Received VarClient: key=1565, value=0
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1567, 0, true));//Received VarClient: key=1567, value=0
		player.getPacketDispatcher().dispatchVarp(new VarMessage(1568, 2, true));//Received VarClient: key=1568, value=2
		player.getPacketDispatcher().dispatchVarcString(new VarcStringMessage(2521, "Test"));
		//test();
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(4314));*/
	}
	
	public void sendClanMemberInfo (ClanMember member) {
		selectedMember = member;
		//1845 - bits 0-9, bit 10, bit 11, bit 12, bit 13
		//player.getPacketDispatcher().dispatchVarp(new VarMessage(1845, 131));
		player.getPacketDispatcher().dispatchVar(new VarMessage(1846, 0));
		player.getPacketDispatcher().dispatchVar(new VarMessage(1845, 32768));
		player.getPacketDispatcher().dispatchVar(new VarMessage(1500, member.getRank().getID(), true));//Rank
		player.getPacketDispatcher().dispatchVar(new VarMessage(1501, 129, true));//Job
		player.getPacketDispatcher().dispatchVar(new VarMessage(1564, 0, true));//[Unknown]
		player.getPacketDispatcher().dispatchVar(new VarMessage(1566, member.isBannedFromCitadel() ? 1 : 0, true));//Ban from citadel
		player.getPacketDispatcher().dispatchVar(new VarMessage(1565, 0, true));//Ban from keep
		player.getPacketDispatcher().dispatchVar(new VarMessage(1567, 0, true));//Ban from island
		player.getPacketDispatcher().dispatchVar(new VarMessage(1568, 2, true));//Probation status
		player.getPacketDispatcher().dispatchVarcString(new VarcStringMessage(2521, member.getDisplayName()));//Display name
		
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(4314));
	}
	
	public void setTab(Tab tab) {
		currentTab = tab;
		switch (tab) {
			case CLANMATES:
				setComponentHidden(88, false);//Received component hidden: iFace=1096, compID=88, hidden=1
				setComponentHidden(89, true);//Received component hidden: iFace=1096, compID=89, hidden=1
				setComponentHidden(90, true);//Received component hidden: iFace=1096, compID=90, hidden=0
				setComponentHidden(116, false);//Received component hidden: iFace=1096, compID=116, hidden=1
				setComponentHidden(123, true);//Received component hidden: iFace=1096, compID=123, hidden=1
				setComponentHidden(389, true);//Received component hidden: iFace=1096, compID=389, hidden=0
				break;
			case SETTINGS:
				setComponentHidden(88, true);//Received component hidden: iFace=1096, compID=88, hidden=1
				setComponentHidden(89, false);//Received component hidden: iFace=1096, compID=89, hidden=0
				setComponentHidden(90, true);//Received component hidden: iFace=1096, compID=90, hidden=1
				setComponentHidden(116, true);//Received component hidden: iFace=1096, compID=116, hidden=1
				setComponentHidden(123, false);//Received component hidden: iFace=1096, compID=123, hidden=0
				setComponentHidden(389, true);//Received component hidden: iFace=1096, compID=389, hidden=1
				break;
			case PERMISSIONS:
				setComponentHidden(88, true);//Received component hidden: iFace=1096, compID=88, hidden=1
				setComponentHidden(89, true);//Received component hidden: iFace=1096, compID=89, hidden=1
				setComponentHidden(90, false);//Received component hidden: iFace=1096, compID=90, hidden=0
				setComponentHidden(116, true);//Received component hidden: iFace=1096, compID=116, hidden=1
				setComponentHidden(123, true);//Received component hidden: iFace=1096, compID=123, hidden=1
				setComponentHidden(389, false);//Received component hidden: iFace=1096, compID=389, hidden=0
				break;
		}
	}
	
	public void setPermissionTab(PermissionTab tab) {
		permTab = tab;
		setComponentHidden(31, true);//Received component hidden: iFace=1096, compID=31, hidden=1
		setComponentHidden(20, true);//Received component hidden: iFace=1096, compID=20, hidden=1
		setComponentHidden(21, true);//Received component hidden: iFace=1096, compID=21, hidden=1
		setComponentHidden(22, true);//Received component hidden: iFace=1096, compID=22, hidden=1
		setComponentHidden(24, true);//Received component hidden: iFace=1096, compID=24, hidden=1
		setComponentHidden(25, true);//Received component hidden: iFace=1096, compID=25, hidden=1
		switch (tab) {
		case ADMIN:
			setComponentHidden(20, false);
			break;
		case CHAT:
			setComponentHidden(21, false);
			break;
		case EVENTS:
			setComponentHidden(22, false);
			break;
		case CITADEL:
			setComponentHidden(24, false);
			break;
		case SKILL:
			setComponentHidden(25, false);
			break;
		}
	}
	
	public void setPermissionGroup (ClanRank rank) {
		player.getPacketDispatcher().dispatchVar(new VarMessage(1569, 100, true));//Received VarClient: key=1569, value=100
		player.getPacketDispatcher().dispatchVar(new VarMessage(1571, 1, true));//Received VarClient: key=1571, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1570, 1, true));//Received VarClient: key=1570, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1572, 1, true));//Received VarClient: key=1572, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1574, 1, true));//Received VarClient: key=1574, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1573, 1, true));//Received VarClient: key=1573, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1575, 1, true));//Received VarClient: key=1575, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1576, 1, true));//Received VarClient: key=1576, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1577, 1, true));//Received VarClient: key=1577, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1578, 1, true));//Received VarClient: key=1578, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1579, 1, true));//Received VarClient: key=1579, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1580, 1, true));//Received VarClient: key=1580, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1581, 1, true));//Received VarClient: key=1581, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1582, 1, true));//Received VarClient: key=1582, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1583, 1, true));//Received VarClient: key=1583, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1584, 1, true));//Received VarClient: key=1584, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1585, 1, true));//Received VarClient: key=1585, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1586, 1, true));//Received VarClient: key=1586, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1587, 1, true));//Received VarClient: key=1587, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1589, 1, true));//Received VarClient: key=1589, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1649, 0, true));//Received VarClient: key=1649, value=0
		player.getPacketDispatcher().dispatchVar(new VarMessage(1792, 1, true));//Received VarClient: key=1792, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(1793, 1, true));//Received VarClient: key=1793, value=1
		player.getPacketDispatcher().dispatchVar(new VarMessage(2001, 0, true));//Received VarClient: key=2001, value=0
		player.getPacketDispatcher().dispatchVar(new VarMessage(2002, 0, true));//Received VarClient: key=2002, value=0
		player.getPacketDispatcher().dispatchVar(new VarMessage(2003, 0, true));//Received VarClient: key=2003, value=0
		player.getPacketDispatcher().dispatchVar(new VarMessage(1590, 0, true));//Received VarClient: key=1590, value=0
		player.getPacketDispatcher().dispatchVar(new VarMessage(3855, 0, true));//Received VarClient: key=3855, value=0
		player.getPacketDispatcher().dispatchVar(new VarMessage(4125, 0, true));//Received VarClient: key=4125, value=0
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
