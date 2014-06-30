package org.virtue.game.logic.node.interfaces.impl;

import java.util.EnumSet;

import org.virtue.Launcher;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RS3Interface;
import org.virtue.game.logic.social.clans.internal.ClanMember;
import org.virtue.game.logic.social.clans.internal.ClanPermission;
import org.virtue.game.logic.social.clans.internal.ClanMember.VarClanMember;
import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class ClanSettingsInterface extends AbstractInterface {
	
	public static enum Tab {CLANMATES, SETTINGS, PERMISSIONS};
	
	public static enum PermissionTab {
		ADMIN(1),CHAT(2),EVENTS(3),CITADEL(4),SKILL(5);
		
		private final int id;
		
		PermissionTab (int id) {
			this.id = id;
		}
		
		public int getID () {
			return id;
		}
	};
	
	private final long clanHash;
	
	private ClanRank selectedPermissionGroup;
	
	private EnumSet<ClanPermission> heldPermissions;
	
	//private Tab currentTab = Tab.CLANMATES;
	
	//private PermissionTab permTab = PermissionTab.ADMIN;
	
	private ClanMember selectedMember = null;
	private ClanRank memberNewRank = null;

	public ClanSettingsInterface(Player p, long clanHash) {
		super(RS3Interface.CLAN_SETTINGS, p);
		this.clanHash = clanHash;
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
			Launcher.getClanManager().sendMemberInfo(player.getChatManager().getSocialUser(), slot1, false);
			break;
		case 282://Clan member rank select
		case 268://Clan member job select
		case 63://Clan member ban from citadel
		case 67://Clan member ban from keep
		case 71://Clan member ban from island
		case 324://Clan member save
		case 315://Clan member kick
			handleMemberOption(component, slot1);
			break;
		case 52:
			sendClanMemberInfo(null);
			break;
		/*Clan permissions tab*/
		case 399://Recruit
			setPermissionGroup(ClanRank.RECRUIT);
			break;
		case 407://Corporal
			setPermissionGroup(ClanRank.CORPORAL);
			break;
		case 415://Sergeant
			setPermissionGroup(ClanRank.SERGEANT);
			break;
		case 423://Lieutenant
			setPermissionGroup(ClanRank.LIEUTENANT);
			break;
		case 431://Captain
			setPermissionGroup(ClanRank.CAPTAIN);
			break;
		case 439://General
			setPermissionGroup(ClanRank.GENERAL);
			break;
		case 447://Admin
			setPermissionGroup(ClanRank.ADMIN);
			break;
		case 455://Organiser
			setPermissionGroup(ClanRank.ORGANISER);
			break;
		case 463://Coordinator
			setPermissionGroup(ClanRank.COORDINATOR);
			break;
		case 471://Overseer
			setPermissionGroup(ClanRank.OVERSEER);
			break;
		case 479://Deputy Owner
			setPermissionGroup(ClanRank.DEPUTY_OWNER);
			break;
		case 493://Admin permission tab
			setPermissionTab(PermissionTab.ADMIN);
			break;
		case 502://Chat permission tab
			setPermissionTab(PermissionTab.CHAT);
			break;
		case 510://Events permission tab
			setPermissionTab(PermissionTab.EVENTS);
			break;
		case 518://Citadel permission tab
			setPermissionTab(PermissionTab.CITADEL);
			break;
		case 526://Skills permission tab			
			setPermissionTab(PermissionTab.SKILL);
			break;
		case 859://Clan broadcast settings
			getPlayer().getInterfaces().setTopInterface(new ClanBroadcastSettings(player, this), 71);
			break;
		case 538://Toggle lock lower ranks from keep
		case 551://Toggle lock lower ranks from citadel
		case 733://Toggle allowed in citadel
		case 746://Toggle allowed in keep
		case 871://Edit broadcast settings
		case 487://Recruit
		case 652://RCW leader
		case 585://Edit signpost
		case 562://Set min talk rank
		case 574://Set min kick rank
		case 596://Edit noticeboard
		case 853://Broadcast event
		case 641://Start battlefield event
		case 664://Start vote
		case 677://Start meeting
		case 787://Operate theater
		case 689://Operate party room
		case 607://Add citadel upgrades
		case 618://Add citade downgrades
		case 629://Edit battlefield
		case 776://Change clanguage
		case 842://Remove avatar habitat
		case 831://Add avatar buff
		case 820://Customise avatar
		case 809://Check resources
		case 713://Change build tick
		case 798://Lock skill plots
		case 701://Set objectives
			handlePermissionButton(component, slot1);
			break;
		case 137://Clan motto editor
		case 130://Clan motif editor
		case 166://Clan keyword editor
		default:
			System.out.println("Unhandled clan settings button: component="+component+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
			break;
		}		
	}
	
	public void handleMemberOption (int component, int slot) {
		if (selectedMember == null) {
			return;
		}
		switch (component) {
		case 324://Clan member save
			if (memberNewRank != null) {
				Launcher.getClanManager().setRank(clanHash, player.getChatManager().getSocialUser(), selectedMember.getProtocolName(), memberNewRank);
			}
			player.getPacketDispatcher().dispatchMessage("Changes have been saved to clanmate.", MessageOpcode.CLAN_SYSTEM);
			sendClanMemberInfo(selectedMember);
			break;
		case 315://Clan member kick
			//TODO: Add confirmation of kick
			if (Launcher.getClanManager().kickClanMember(clanHash, player.getChatManager().getSocialUser(), selectedMember.getProtocolName())) {
				sendClanMemberInfo(null);
				player.getPacketDispatcher().dispatchMessage("Successfully kicked clan member.", MessageOpcode.CLAN_SYSTEM);
			} else {
				player.getPacketDispatcher().dispatchMessage("Failed to kick clan member.", MessageOpcode.CLAN_SYSTEM);
			}			
			break;
		case 282://Clan member rank select
			if (selectedMember.getRank().equals(ClanRank.OWNER)) {
				if (slot >= ClanRank.DEPUTY_OWNER.getID()) {
					player.getPacketDispatcher().dispatchMessage("You can only adjust your rank to overseer or lower.", MessageOpcode.CLAN_SYSTEM);
					return;
				}
				//TODO: This should be handled via dialogs using confirmation
				//Applying changes to clan settings.
				//You must appoint a deputy owner to take over as owner before you can demote yourself.
			}
			memberNewRank = ClanRank.forID(slot);
			break;
		case 268://Clan member job select
		case 63://Clan member ban from citadel
		case 67://Clan member ban from keep
		case 71://Clan member ban from island
		default:
			System.out.println("Unhandled clan member button: component="+component+", slot1="+slot);			
			break;
		}
	}
	
	public void sendClanMemberInfo (ClanMember member) {
		selectedMember = member;
		if (member == null) {
			player.getVarManager().setVarClient(1500, -1);//Rank
			player.getVarManager().setVarClient(1501, -1);//Job
			player.getVarManager().setVarClient(1564, -1);//[Unknown]
			player.getVarManager().setVarClient(1566, -1);//Ban from citadel
			player.getVarManager().setVarClient(1565, -1);//Ban from keep
			player.getVarManager().setVarClient(1567, -1);//Ban from island
			player.getVarManager().setVarClient(1568, -1);//Probation status
			player.getVarManager().setVarClient(2521, "");//Display name
			return;
		}
		//1845 - bits 0-9, bit 10, bit 11, bit 12, bit 13, bits 14-15, bit 30, bit 31
		//1846 - bits 0-6, 7-13
		//player.getVarManager().setVarPlayer(new VarMessage(1845, 131));
		player.getVarManager().setVarPlayer(1846, 0);
		player.getVarManager().setVarPlayer(1845, member.getVarClanMember());//32768
		player.getVarManager().setVarClient(1500, member.getRank().getID());//Rank
		player.getVarManager().setVarClient(1501, member.getVarMemberBit(VarClanMember.JOB));//Job
		player.getVarManager().setVarClient(1564, 0);//[Unknown]
		player.getVarManager().setVarClient(1565, member.getVarMemberBit(VarClanMember.KEEP_BANNED));//Ban from keep
		player.getVarManager().setVarClient(1566, member.getVarMemberBit(VarClanMember.CITADEL_BANNED));//Ban from citadel
		player.getVarManager().setVarClient(1567, member.getVarMemberBit(VarClanMember.ISLAND_BANNED));//Ban from island
		player.getVarManager().setVarClient(1568, member.getVarMemberBit(VarClanMember.PROBATION));//Probation status
		player.getVarManager().setVarClient(2521, member.getDisplayName());//Display name
		
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(4314));
	}
	
	public void setTab(Tab tab) {
		//currentTab = tab;
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
	
	private void handlePermissionButton (int component, int slot) {
		ClanPermission permission = null;
		switch (component) {
		case 487://Recruit
			permission = ClanPermission.RECRUIT;
			break;
		case 538://Toggle lock lower ranks from keep
			permission = ClanPermission.LOCK_KEEP;
			break;
		case 551://Toggle lock lower ranks from citadel
			permission = ClanPermission.LOCK_CITADEL;
			break;
		case 562://Set min talk rank
		case 574://Set min kick rank
			System.out.println("Unhandled clan permission button: component="+component+", slot1="+slot);
			return;
		case 585://Edit signpost
			permission = ClanPermission.EDIT_SIGNPOST;
			break;
		case 596://Edit noticeboard
			permission = ClanPermission.EDIT_NOTICEBOARD;
			break;
		case 607://Add citadel upgrades
			permission = ClanPermission.CITADEL_UPGRADE;
			break;
		case 618://Add citade downgrades
			permission = ClanPermission.CITADEL_DOWNGRADE;
			break;
		case 629://Edit battlefield
			permission = ClanPermission.EDIT_BATTLEFIELD;
			break;
		case 641://Start battlefield event
			permission = ClanPermission.START_BATTLEFIELD;
			break;
		case 652://RCW leader
			permission = ClanPermission.LEAD_RCW;
			break;
		case 664://Start vote
			permission = ClanPermission.START_VOTE;
			break;
		case 677://Start meeting
			permission = ClanPermission.START_MEETING;
			break;
		case 689://Operate party room
			permission = ClanPermission.PARTY_TECH;
			break;
		case 701://Set objectives
			permission = ClanPermission.SET_GATHER_TARGETS;
			break;
		case 713://Change build tick
			permission = ClanPermission.MOVE_TICK;
			break;
		case 733://Toggle allowed in citadel
			permission = ClanPermission.ENTER_CITADEL;
			break;
		case 746://Toggle allowed in keep
			permission = ClanPermission.ENTER_KEEP;
			break;
		case 776://Change clan language
			permission = ClanPermission.SET_LANGUAGE;
			break;
		case 787://Operate theater
			permission = ClanPermission.THEATRE_TECH;
			break;
		case 798://Lock skill plots
			permission = ClanPermission.LOCK_SKILLPLOT;
			break;
		case 809://Check resources
			permission = ClanPermission.CHECK_RESOURCES;
			break;
		case 820://Customise avatar
			permission = ClanPermission.CUSTOMISE_AVATAR;
			break;
		case 831://Add avatar buff
			permission = ClanPermission.EDIT_AVATAR_BUFFS;
			break;
		case 842://Remove avatar habitat
			permission = ClanPermission.REMOVE_AVATAR;
			break;
		case 853://Broadcast event
			permission = ClanPermission.BROADCAST_EVENT;
			break;
		case 871://Edit broadcast settings
			permission = ClanPermission.EDIT_BROADCASTS;
			break;
		default:
			System.out.println("Unhandled clan permission button: component="+component+", slot1="+slot);
			return;
		}
		//System.out.println("Granting permission "+permission+" to group "+selectedPermissionGroup);
		if (permission != null && heldPermissions != null) {
			Launcher.getClanManager().setPermission(player.getChatManager().getMyClanHash(), player.getChatManager().getSocialUser(), 
					selectedPermissionGroup, permission, !heldPermissions.contains(permission));
		}
	}
	
	public void setPermissionTab(PermissionTab tab) {
		//permTab = tab;
		setComponentHidden(31, true);//Received component hidden: iFace=1096, compID=31, hidden=1
		setComponentHidden(20, true);//Received component hidden: iFace=1096, compID=20, hidden=1
		setComponentHidden(21, true);//Received component hidden: iFace=1096, compID=21, hidden=1
		setComponentHidden(22, true);//Received component hidden: iFace=1096, compID=22, hidden=1
		setComponentHidden(24, true);//Received component hidden: iFace=1096, compID=24, hidden=1
		setComponentHidden(25, true);//Received component hidden: iFace=1096, compID=25, hidden=1
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(5136, tab.getID()));
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
	
	public void sendPermissionGroup (ClanRank rank, EnumSet<ClanPermission> permissions) {
		this.selectedPermissionGroup = rank;
		this.heldPermissions = permissions;
		System.out.println("Permissions for group "+rank+": "+permissions);
		player.getVarManager().setVarClient(1569, rank.getID());
		for (ClanPermission p : ClanPermission.values()) {
			int holds = permissions.contains(p) ? 1 : 0;
			if (p.getMinRank().getID() > rank.getID()) {
				holds = 0;
			}
			player.getVarManager().setVarClient(p.getVarcID(), holds);
		}
		player.getVarManager().setVarClient(1571, 0);
		player.getVarManager().setVarClient(1570, 1);
	}
	
	public void setPermissionGroup (ClanRank group) {
		Launcher.getClanManager().sendPermissionInfo(player.getChatManager().getSocialUser(), group, false);
		/*this.selectedPermissionGroup = rank;
		//Received VarPlayer: key=1846, value=637 (rank)
		player.getVarManager().setVarClient(1569, rank.getID());//Received VarClient: key=1569, value=100
		player.getVarManager().setVarClient(1571, 1);
		player.getVarManager().setVarClient(1570, 1);
		player.getVarManager().setVarClient(1572, 1);
		player.getVarManager().setVarClient(1574, 1);
		player.getVarManager().setVarClient(1573, 1);
		player.getVarManager().setVarClient(1575, 1);
		player.getVarManager().setVarClient(1576, 1);
		player.getVarManager().setVarClient(1577, 1);
		player.getVarManager().setVarClient(1578, 1);
		player.getVarManager().setVarClient(1579, 1);
		player.getVarManager().setVarClient(1580, 1);
		player.getVarManager().setVarClient(1581, 1);
		player.getVarManager().setVarClient(1582, 1);
		player.getVarManager().setVarClient(1583, 1);
		player.getVarManager().setVarClient(1584, 1);
		player.getVarManager().setVarClient(1585, 1);
		player.getVarManager().setVarClient(1586, 1);
		player.getVarManager().setVarClient(1587, 1);
		player.getVarManager().setVarClient(1589, 1);
		player.getVarManager().setVarClient(1649, 0);
		player.getVarManager().setVarClient(1792, 1);
		player.getVarManager().setVarClient(1793, 1);
		player.getVarManager().setVarClient(2001, 0);
		player.getVarManager().setVarClient(2002, 0);
		player.getVarManager().setVarClient(2003, 0);
		player.getVarManager().setVarClient(1590, 0);
		player.getVarManager().setVarClient(3855, 0);
		player.getVarManager().setVarClient(4125, 0);*/
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
