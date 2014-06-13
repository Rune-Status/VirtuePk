package org.virtue.game.logic.content.combat.ability;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.network.protocol.messages.ClientScriptVar;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 * TODO UPDATE WHATS NEED TO 803
 */
public class ActionBar extends AbstractInterface {
	
public static final int[] SHORTCUT_CHILD_IDS = new int[] { 32, 72, 76, 80, 84, 88, 92, 96, 100, 104, 108, 112 };
	
	private Player player;
	
	public int bar = 0;
	public boolean locked = false;
	public Shortcut[][] shortcuts = new Shortcut[5][12];
	
	public static final int ACTION_BAR_INTERFACE_ID = 1430;

	public ActionBar(Player player) {
		super(ACTION_BAR_INTERFACE_ID, player);
		this.player = player;
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(118, -1, -1, 2098176);//IfaceSettings: 93716598, -1, -1, 2098176
		sendInterfaceSettings(123, -1, -1, 2098176);//IfaceSettings: 93716603, -1, -1, 2098176
		sendInterfaceSettings(124, -1, -1, 2098176);//IfaceSettings: 93716604, -1, -1, 2098176
		sendInterfaceSettings(129, -1, -1, 2098176);//IfaceSettings: 93716609, -1, -1, 2098176
		sendInterfaceSettings(130, -1, -1, 2098176);//IfaceSettings: 93716610, -1, -1, 2098176
		sendInterfaceSettings(135, -1, -1, 2098176);//IfaceSettings: 93716615, -1, -1, 2098176
		sendInterfaceSettings(136, -1, -1, 2098176);//IfaceSettings: 93716616, -1, -1, 2098176
		sendInterfaceSettings(141, -1, -1, 2098176);//IfaceSettings: 93716621, -1, -1, 2098176
		sendInterfaceSettings(142, -1, -1, 2098176);//IfaceSettings: 93716622, -1, -1, 2098176
		sendInterfaceSettings(147, -1, -1, 2098176);//IfaceSettings: 93716627, -1, -1, 2098176
		sendInterfaceSettings(148, -1, -1, 2098176);//IfaceSettings: 93716628, -1, -1, 2098176
		sendInterfaceSettings(153, -1, -1, 2098176);//IfaceSettings: 93716633, -1, -1, 2098176
		sendInterfaceSettings(154, -1, -1, 2098176);//IfaceSettings: 93716634, -1, -1, 2098176
		sendInterfaceSettings(159, -1, -1, 2098176);//IfaceSettings: 93716639, -1, -1, 2098176
		sendInterfaceSettings(160, -1, -1, 2098176);//IfaceSettings: 93716640, -1, -1, 2098176
		sendInterfaceSettings(165, -1, -1, 2098176);//IfaceSettings: 93716645, -1, -1, 2098176
		sendInterfaceSettings(166, -1, -1, 2098176);//IfaceSettings: 93716646, -1, -1, 2098176
		sendInterfaceSettings(171, -1, -1, 2098176);//IfaceSettings: 93716651, -1, -1, 2098176
		sendInterfaceSettings(172, -1, -1, 2098176);//IfaceSettings: 93716652, -1, -1, 2098176
		sendInterfaceSettings(177, -1, -1, 2098176);//IfaceSettings: 93716657, -1, -1, 2098176
		sendInterfaceSettings(178, -1, -1, 2098176);//IfaceSettings: 93716658, -1, -1, 2098176
		sendInterfaceSettings(183, -1, -1, 2098176);//IfaceSettings: 93716663, -1, -1, 2098176
		sendInterfaceSettings(184, -1, -1, 2098176);//IfaceSettings: 93716664, -1, -1, 2098176
		sendInterfaceSettings(189, -1, -1, 2098176);//IfaceSettings: 93716669, -1, -1, 2098176
		
		sendInterfaceSettings(10, -1, -1, 8388608);//IfaceSettings: 93716490, -1, -1, 8388608
		sendInterfaceSettings(8, -1, -1, 8650758);//IfaceSettings: 93716488, -1, -1, 8650758
		sendInterfaceSettings(11, -1, -1, 8388608);//IfaceSettings: 93716491, -1, -1, 8388608
	}
	
	public Shortcut getShortcut(int id) {
		return shortcuts[bar][id];
	}
	
	public void setShortcut(int id, Shortcut s) {
		shortcuts[bar][id] = s;
		sendShortcut(id);
	}
	
	public void refresh() {
		sendSettings();
		
		for(int i=0; i<12; i++) {
			sendShortcut(i);
		}
		
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(6998, 1));
	}
		
	
	private void sendSettings() {
		int bitconf = (bar + 1) << 5;
		if(locked) {
			bitconf |= 0x10;
		}

		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(682, bitconf));
	}

	public void sendShortcut(int id) {
		Shortcut s = shortcuts[bar][id];
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(727 + id, s != null ? s.id() : 0));
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(739 + id, 0));
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(811 + id, s != null ? s.itemId() : -1));
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(823 + id, -1));
		
		player.getInterfaces().sendInterfaceSettings(640, SHORTCUT_CHILD_IDS[id], -1, -1, s == null ? 2097152 : (locked ? 2195454 : 11108350));
	}
	
	public void sendMomentum(int momentum) {
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(679, momentum));
	}

	@Override
	public void handleActionButton(int componentID, int slotID1, int slotID2, ActionButton button) {
		System.out.println("Action bar button pressed: component="+componentID+", slot1="+slotID1+", slot2="+slotID2+", button="+button.getID());
	}

	@Override
	public int getTabID() {
		return 1003;
	}

}
