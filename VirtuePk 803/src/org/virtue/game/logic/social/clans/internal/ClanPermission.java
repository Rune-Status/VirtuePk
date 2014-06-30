package org.virtue.game.logic.social.clans.internal;

import org.virtue.cache.def.VarBitType;
import org.virtue.cache.def.VarBitTypeLoader;
import org.virtue.game.logic.social.clans.ClanRank;

public enum ClanPermission {	
	//Received VarClient: key=1571, value=1
	//Received VarClient: key=1570, value=1
	LOCK_KEEP(1572, 6182, 6186, ClanRank.ADMIN),//Received VarClient: key=1572, value=1
	LOCK_CITADEL(1574, 6187, 6191, ClanRank.ADMIN),//Received VarClient: key=1574, value=1
	ENTER_KEEP(1573, -1, -1, ClanRank.RECRUIT),//Received VarClient: key=1573, value=1
	ENTER_CITADEL(1575, -1, -1, ClanRank.RECRUIT),//Received VarClient: key=1575, value=1
	RECRUIT(1576, 6192, 6202, ClanRank.RECRUIT),//Received VarClient: key=1576, value=1
	START_BATTLEFIELD(1577, 6203, 6213, ClanRank.RECRUIT),//Received VarClient: key=1577, value=1
	LEAD_RCW(1578, 6214, 6224, ClanRank.RECRUIT),//Received VarClient: key=1578, value=1
	START_VOTE(1579, 6225, 6235, ClanRank.RECRUIT),//Received VarClient: key=1579, value=1
	START_MEETING(1580, 6236, 6246, ClanRank.RECRUIT),//Received VarClient: key=1580, value=1
	PARTY_TECH(1581, 6247, 6257, ClanRank.RECRUIT),//Received VarClient: key=1581, value=1
	THEATRE_TECH(1582, 6258, 6268, ClanRank.RECRUIT),//Received VarClient: key=1582, value=1
	EDIT_NOTICEBOARD(1583, 6269, 6273, ClanRank.ADMIN),//Received VarClient: key=1583, value=1
	EDIT_SIGNPOST(1584, 6274, 6278, ClanRank.ADMIN),//Received VarClient: key=1584, value=1
	EDIT_BATTLEFIELD(1585, 6279, 6289, ClanRank.RECRUIT),//Received VarClient: key=1585, value=1
	CITADEL_UPGRADE(1586, 6290, 6294, ClanRank.ADMIN),//Received VarClient: key=1586, value=1
	CITADEL_DOWNGRADE(1587, 6295, 6299, ClanRank.ADMIN),//Received VarClient: key=1587, value=1
	TRANSFER_RESOURCES(1588, 6300, 6304, ClanRank.ADMIN),//Note: Unused
	SET_GATHER_TARGETS(1589, 6305, 6309, ClanRank.ADMIN),//Received VarClient: key=1589, value=1
	SET_LANGUAGE(1649, 6313, 6314, ClanRank.OVERSEER),//Received VarClient: key=1649, value=0
	LOCK_SKILLPLOT(1792, 6166, 6170, ClanRank.ADMIN),//Received VarClient: key=1792, value=1
	CHECK_RESOURCES(1793, 6171, 6181, ClanRank.RECRUIT),//Received VarClient: key=1793, value=1
	REMOVE_AVATAR(2001, 6341, 6344, ClanRank.ADMIN),//Received VarClient: key=2001, value=0
	CUSTOMISE_AVATAR(2003, 6345, 6349, ClanRank.ADMIN),//Received VarClient: key=2002, value=0
	EDIT_AVATAR_BUFFS(2002, 6350, 6354, ClanRank.ADMIN),//Received VarClient: key=2003, value=0
	MOVE_TICK(1590, 6315, 6316, ClanRank.OVERSEER),//Received VarClient: key=1590, value=0
	BROADCAST_EVENT(3855, 20978, 20988, ClanRank.RECRUIT),//Received VarClient: key=3855, value=0
	EDIT_BROADCASTS(4125, 21952, 21956, ClanRank.ADMIN);//Received VarClient: key=4125, value=0
	
	private final int varcID;
	
	private final int startVarBit, endVarBit;
	
	private final ClanRank minRank;
	
	ClanPermission(int varcID, int startVarBit, int endVarBit, ClanRank minRank) {
		this.varcID = varcID;
		this.startVarBit = startVarBit;
		this.endVarBit = endVarBit;
		this.minRank = minRank;
	}
	
	public int getVarcID () {
		return varcID;
	}
	
	public ClanRank getMinRank () {
		return minRank;
	}
	
	public VarBitType getVarBit (ClanRank rank) {
		return VarBitTypeLoader.forID(getVarBitID(rank));
	}
	
	public int getVarBitID (ClanRank rank) {
		if (rank == null || rank.getID() < minRank.getID()) {
			return -1;
		}
		switch (rank) {
		case DEPUTY_OWNER:
			return endVarBit;
		case OVERSEER:
			return endVarBit-1;
		case COORDINATOR:
			return endVarBit-2;
		case ORGANISER:
			return endVarBit-3;
		case ADMIN:
			return endVarBit-4;
		case RECRUIT:
			return startVarBit;
		case CORPORAL:
			return startVarBit+1;
		case SERGEANT:
			return startVarBit+2;
		case LIEUTENANT:
			return startVarBit+3;
		case CAPTAIN:
			return startVarBit+4;
		case GENERAL:
			return startVarBit+5;
		default:
			return -1;
		}		
	}
}
