package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.content.lodestones.LodestoneInterface;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RS3Interface;

public class MinimapInterface extends AbstractInterface {

	public MinimapInterface(Player p) {
		super(RS3Interface.MINIMAP, p);
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(4, -1, -1, 8388608);

	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 0:
			if (button.equals(ActionButton.ONE)) {
				getPlayer().toggleRun();
			} else if (button.equals(ActionButton.TWO)) {
				System.out.println("Unhandled rest request for player: "+getPlayer().getAccount().getUsername().getName());
			}
			break;
		case 10://Home teleport
			if (button.equals(ActionButton.ONE)) {//Open interface
				player.getInterfaces().setTopInterface(new LodestoneInterface(player));
			} else if (button.equals(ActionButton.TWO)) {//Teleport to previous location
				System.out.println("Unhandled teleport-to-prev request for player: "+getPlayer().getAccount().getUsername().getName());
			}
			break;
		case 7://Reset camera
		case 9://World map
		default:
			System.out.println("Unhandled minimap button: component="+component+", button="+button.getID()+", slot1="+slot1+", slot2="+slot2);			
			break;
		}
	}

	@Override
	public int getTabID() {
		return 1005;
	}

}
