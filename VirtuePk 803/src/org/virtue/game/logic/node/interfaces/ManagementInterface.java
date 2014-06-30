package org.virtue.game.logic.node.interfaces;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.management.Challanges;
import org.virtue.game.logic.node.interfaces.management.Loadout;
import org.virtue.game.logic.node.interfaces.management.Polls;
import org.virtue.network.protocol.messages.ClientScriptVar;

public class ManagementInterface extends AbstractInterface {
	
	public static enum Type {
			HERO(0),//Summary - Skills - Pets - Achievements
			GEAR(1),//Loadout - Wardrobe - Titles - Animations - Appearance
			POWERS(2),//Melee - Ranged - Magic - Defensive - Prayers - Combat settings
			ADVENTURE(3),//Featured - Quests - Challenges - Minigames - Path
			COMMUNITY(4);//Vote now - Hiscores - Activity Stream - Chat Settings
			
			private final int id;
			
			Type (int id) {
				this.id = id;
			};
			
			public int getID() {
				return id;
			}
		}
	
	private ActionTab currentTab;

	public ManagementInterface(Player p) {
		super(RS3Interface.MANAGEMENT, p);
	}

	@Override
	public void postSend() {
		
	}
	
	public void openInterface(Type type) {
		player.getInterfaces().sendHideComponent(1477, 77, false);//Received component hidden: interface=1477, component=77, hidden=0
		player.getInterfaces().sendInterfaceSettings(1477, 76, 0, 24, 2);//IfaceSettings: interface=1477, compID=76, fromSlot=0, toSlot=24, settings=2
		player.getInterfaces().sendInterfaceSettings(1477, 78, 1, 1, 2);//IfaceSettings: interface=1477, compID=78, fromSlot=1, toSlot=1, settings=2
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8194, 1, type.getID()));//Received Cs2:[8194, 1, 1]
		player.getVarManager().setVarClient(2911, type.getID());//Received VarClient: key=2911, value=4
		switch (type) {
		case ADVENTURE:
			openTab(new Challanges(player));
			break;
		case COMMUNITY:
			openTab(new Polls(player));
			break;
		case GEAR:
			openTab(new Loadout(player));
			break;
		case HERO:
			break;
		case POWERS:
			break;
		}
	}
	
	public void openTab (ActionTab tab) {
		this.currentTab = tab;
		player.getVarManager().setVarPlayer(3708, tab.getUnknownInt());//Received VarPlayer: key=3708, value=42729988
		player.getVarManager().setVarPlayer(1758, 0);//Received VarPlayer: key=1758, value=0
		player.getVarManager().setVarPlayer(1765, -1);//Received VarPlayer: key=1765, value=-1
		player.getVarManager().setVarPlayer(4041, 0);//Received VarPlayer: key=4041, value=0
				
		//setComponentHidden(3, false);//Received component hidden: interface=1448, component=3, hidden=0
		player.getInterfaces().setInterface(tab, tab.getComponentID(), true, 1448);//Interface: id=1029, clipped=1, parentID=1448, compID=3
		
		for (int i=1;i<13;i++) {
			if (i == 2 || i == 4) {
				continue;
			}
			setComponentHidden(i, tab.isCompHidden(i));
		}
		/*setComponentHidden(5, true);//Received component hidden: interface=1448, component=5, hidden=1
		setComponentHidden(6, true);//Received component hidden: interface=1448, component=6, hidden=1
		setComponentHidden(7, true);//Received component hidden: interface=1448, component=7, hidden=1
		setComponentHidden(8, true);//Received component hidden: interface=1448, component=8, hidden=1
		setComponentHidden(9, true);//Received component hidden: interface=1448, component=9, hidden=1
		setComponentHidden(10, true);//Received component hidden: interface=1448, component=10, hidden=1
		setComponentHidden(11, true);//Received component hidden: interface=1448, component=11, hidden=1
		setComponentHidden(12, true);//Received component hidden: interface=1448, component=12, hidden=1
		setComponentHidden(1, true);//Received component hidden: interface=1448, component=1, hidden=1*/
	}
	
	public ActionTab getCurrentTab () {
		return currentTab;
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		System.out.println("Unhandled management interface button: component="+component+", button="+button.getID()+", slot1="+slot1+", slot2="+slot2);
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
