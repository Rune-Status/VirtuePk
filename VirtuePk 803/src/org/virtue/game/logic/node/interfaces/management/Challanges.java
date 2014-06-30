package org.virtue.game.logic.node.interfaces.management;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.ActionTab;
import org.virtue.game.logic.node.interfaces.ManagementInterface.Type;
import org.virtue.game.logic.node.interfaces.RS3Interface;
import org.virtue.network.protocol.messages.ClientScriptVar;

public class Challanges extends ActionTab {

	public Challanges(Player p) {
		super(RS3Interface.CHALLANGES, p);
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(63, 0, 23, 2);//IfaceSettings: interface=1343, compID=63, fromSlot=0, toSlot=23, settings=2
		sendInterfaceSettings(50, 0, 5, 2);//IfaceSettings: interface=1343, compID=50, fromSlot=0, toSlot=5, settings=2
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(4507, 88014879, 88014900, 88014911, 22));//Received Cs2:[4507, 22, 88014911, 88014900, 88014879]
		player.getInterfaces().sendInterface(true, 1448, 5, 1056);//Interface: id=1056, clipped=1, parentID=1448, compID=5
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		System.out.println("Unhandled challanges interface button: component="+component+", button="+button.getID()+", slot1="+slot1+", slot2="+slot2);
	}

	@Override
	public boolean isCompHidden(int tabID) {
		return tabID != 3 && tabID != 5;
	}

	@Override
	public int getUnknownInt() {
		return 140640771;
	}

	@Override
	public int getTab() {
		return 3;
	}

	@Override
	public Type getType() {
		return Type.ADVENTURE;
	}

}
