package org.virtue.game.logic.node.interfaces.management;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.ActionTab;
import org.virtue.game.logic.node.interfaces.ManagementInterface.Type;
import org.virtue.game.logic.node.interfaces.RSInterface;

public class Loadout extends ActionTab {

	public Loadout(Player p) {
		super(RSInterface.LOADOUT, p);
	}

	@Override
	public void postSend() {
		setComponentText(9, player.getAccount().getUsername().getName());//Received interface component text: [Display name], iFace=1463, compID=9
		player.getInterfaces().sendInterfaceSettings(1474, 13, -1, -1, 2097152);//IfaceSettings: interface=1474, compID=13, fromSlot=-1, toSlot=-1, settings=2097152
		player.getInterfaces().sendInterfaceSettings(1474, 13, 0, 27, 15302030);//IfaceSettings: interface=1474, compID=13, fromSlot=0, toSlot=27, settings=15302030
		player.getInterfaces().sendInterface(true, 1448, 3, 1474);//Interface: id=1463, clipped=1, parentID=1448, compID=5
		player.getInterfaces().sendInterface(true, 1448, 7, 1462);//Interface: id=1462, clipped=1, parentID=1448, compID=7
		player.getInterfaces().sendInterfaceSettings(1462, 14, 0, 15, 15302654);//IfaceSettings: interface=1462, compID=14, fromSlot=0, toSlot=15, settings=15302654
		player.getInterfaces().sendInterfaceSettings(1462, 20, 2, 7, 2);//IfaceSettings: interface=1462, compID=20, fromSlot=2, toSlot=7, settings=2
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		//System.out.println("Unhandled loadout interface button: component="+component+", button="+button.getID()+", slot1="+slot1+", slot2="+slot2);
	}

	@Override
	public boolean isCompHidden(int tabID) {
		return tabID != 3 && tabID != 5 && tabID != 7;
	}

	@Override
	public int getUnknownInt() {
		return 136454657;
	}
	
	@Override
	public int getComponentID () {
		return 5;
	}

	@Override
	public int getTab() {
		return 1;
	}

	@Override
	public Type getType() {
		return Type.GEAR;
	}

}
