package org.virtue.game.logic.node.interfaces;

import java.util.HashMap;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.screen.ClientScreen;
import org.virtue.game.logic.node.interfaces.impl.ClanInterface;
import org.virtue.game.logic.node.interfaces.impl.FriendsChatInfo;
import org.virtue.game.logic.node.interfaces.impl.FriendsList;
import org.virtue.game.logic.node.interfaces.impl.MinimapInterface;
import org.virtue.game.logic.node.interfaces.impl.RibbonInterface;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.InterfaceMessage;
import org.virtue.network.protocol.messages.InterfaceSettingsMessage;
import org.virtue.network.protocol.packet.encoder.impl.GameScreenEncoder;
import org.virtue.network.protocol.packet.encoder.impl.InterfaceSettingsEncoder;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class InterfaceManager {
	/**
	 * Represents the player.
	 */
	private Player player;
	
	/**
	 * Represents all interfaces currently in use for the player
	 */
	private HashMap<Integer, AbstractInterface> tabInterfaces = new HashMap<Integer, AbstractInterface>();
	
	private AbstractInterface topLevelInterface = null;
	
	/**
	 * Constructs a new {@code InterfaceManager.java}.
	 * @param player The player.
	 */
	public InterfaceManager(Player player, ClientScreen screen) {
		this.player = player;
		this.screenInfo = screen;
	}
	
	public static final int GAME_WINDOW_PANE = 1477;
	
	/**
	 * Represents if the screen is resizable.
	 */
	private ClientScreen screenInfo;
	
	public ClientScreen getScreen () {
		return screenInfo;
	}
	
	/**
	 * Sends the game screen.
	 */
	public void sendScreen() {
		player.getAccount().getSession().getTransmitter().send(GameScreenEncoder.class, screenInfo.getDisplayMode());
		sendInterface(true, 1477, 87, 1482);//Interface: id=1482, clipped=1, parent=[1477, 87] (Game scene)
		
		setInterface(player.getSkills(), 313, true);
		
		/*sendInterface(true, 1477, 313, 1466);//Interface: id=1466, clipped=1, parent=[1477, 313] (Skills)
		sendInterfaceSettings(1466, 10, 0, 26, 30);//IfaceSettings: 96075786, 26, 0, 30
		//sendCs2(new ClientScriptVar(8862, 0, 1));//Runscript: [8862, 0, 1]*/
		
		sendInterface(true, 1477, 293, 1220);//Interface: id=1220, clipped=1, parent=[1477, 293] (Active task)
		//sendCs2(new ClientScriptVar(8862, 1, 1));//Runscript: [8862, 1, 1]
		
		setInterface(player.getInventory(), 130, true);//Inventory tab
		/*sendInterface(true, 1477, 130, 1473);//Interface: id=1473, clipped=1, parent=[1477, 130] (Inventory)
		sendInterfaceSettings(1473, 8, -1, -1, 2097152);//IfaceSettings: 96534536, -1, -1, 2097152
		sendInterfaceSettings(1473, 8, 0, 27, 15302030);//IfaceSettings: 96534536, 27, 0, 15302030
		sendInterfaceSettings(1473, 0, 0, 27, 1536);//IfaceSettings: 96534528, 27, 0, 1536
		sendCs2(new ClientScriptVar(8862, 2, 1));//Runscript: [8862, 2, 1]*/
		
		setInterface(player.getEquipment(), 202, true);//Equipment tab
		
		/*sendInterface(true, 1477, 202, 1464);//Interface: id=1464, clipped=1, parent=[1477, 202] (Equipment)
		sendInterfaceSettings(1464, 14, 0, 15, 15302654);//IfaceSettings: 95944718, 15, 0, 15302654
		sendInterfaceSettings(1464, 12, 2, 7, 2);//IfaceSettings: 95944716, 7, 2, 2
		sendCs2(new ClientScriptVar(8862, 3, 1));//Runscript: [8862, 3, 1]*/
		
		sendInterface(true, 1477, 323, 1458);//Interface: id=1458, clipped=1, parent=[1477, 323] (Prayer tab)
		sendInterfaceSettings(1458, 24, 0, 28, 8388610);//IfaceSettings: 95551512, 28, 0, 8388610
		//sendCs2(new ClientScriptVar(8862, 4, 1));//Runscript: [8862, 4, 1]
		
		sendInterface(true, 1477, 239, 1460);//Interface: id=1460, clipped=1, parent=[1477, 239] (Melee abilities)
		sendInterface(true, 1477, 249, 1452);//Interface: id=1452, clipped=1, parent=[1477, 249] (Ranged abilities)
		sendInterface(true, 1477, 259, 1461);//Interface: id=1461, clipped=1, parent=[1477, 259] (Magic abilities)
		sendInterface(true, 1477, 269, 1449);//Interface: id=1449, clipped=1, parent=[1477, 269] (Defense abilities)
		sendInterfaceSettings(1460, 1, 0, 168, 10320902);//IfaceSettings: 95682561, 168, 0, 10320902
		sendInterfaceSettings(1452, 1, 0, 168, 10320902);//IfaceSettings: 95158273, 168, 0, 10320902
		sendInterfaceSettings(1461, 1, 0, 168, 10320902);//IfaceSettings: 95748097, 168, 0, 10320902
		sendInterfaceSettings(1449, 1, 0, 168, 10320902);//IfaceSettings: 94961665, 168, 0, 10320902
		sendInterfaceSettings(1460, 4, 6, 14, 2);//IfaceSettings: 95682564, 14, 6, 2
		sendInterfaceSettings(1452, 7, 6, 14, 2);//IfaceSettings: 95158279, 14, 6, 2
		sendInterfaceSettings(1461, 7, 6, 14, 2);//IfaceSettings: 95748103, 14, 6, 2
		sendInterfaceSettings(1449, 7, 6, 14, 2);//IfaceSettings: 94961671, 14, 6, 2
		//sendCs2(new ClientScriptVar(8862, 5, 1));//Runscript: [8862, 5, 1]
		
		setInterface(new FriendsList(player), 371, true);
                
		/*sendInterface(true, 1477, 371, 550);//Interface: id=550, clipped=1, parent=[1477, 371] (Friends list)
		//sendCs2(new ClientScriptVar(8862, 14, 1));//Runscript: [8862, 14, 1]
		sendInterfaceSettings(550, 25, 0, 500, 510);//IfaceSettings: 36044825, 500, 0, 510
		sendInterfaceSettings(550, 23, 0, 500, 6);//IfaceSettings: 36044823, 500, 0, 6*/
		
		setInterface(new FriendsChatInfo(player), 602, true);
		
		/*sendInterface(true, 1477, 602, 1427);//Interface: id=1427, clipped=1, parent=[1477, 602] (Friends chat)
		sendCs2(new ClientScriptVar(1303, player.getAccount().getUsername().getName(), 1, 1, 93519895));//Runscript: [1303, 93519895, 1, 1, Test]
		sendInterfaceSettings(1427, 23, 0, 600, 1024);//IfaceSettings: 93519895, 600, 0, 1024*/
		//sendCs2(new ClientScriptVar(8862, 15, 1));//Runscript: [8862, 15, 1]
		
		setInterface(new ClanInterface(player), 361, true);
		
		/*sendInterface(true, 1477, 361, 1110);//Interface: id=1110, clipped=1, parent=[1477, 361] (Clan chat)
		//sendCs2(new ClientScriptVar(8862, 16, 1));//Runscript: [8862, 16, 1]
		sendInterfaceSettings(1110, 20, 0, 200, 2);//IfaceSettings: 72744980, 200, 0, 2
		sendInterfaceSettings(1110, 25, 0, 600, 2);//IfaceSettings: 72744985, 600, 0, 2
		sendInterfaceSettings(1110, 23, 0, 600, 1024);//IfaceSettings: 72744983, 600, 0, 1024
		sendInterfaceSettings(1110, 14, 0, 600, 1024);//IfaceSettings: 72744974, 600, 0, 1024*/
		
		sendInterface(true, 1477, 303, 590);//Interface: id=590, clipped=1, parent=[1477, 303]
		sendCs2(new ClientScriptVar(4717, 3874, 38666249, 38666247, 38666248));//Runscript: [4717, 38666248, 38666247, 38666249, 3874]
		sendInterfaceSettings(590, 8, 0, 169, 8388614);//IfaceSettings: 38666248, 169, 0, 8388614
		sendInterfaceSettings(590, 13, 0, 11, 2);//IfaceSettings: 38666253, 11, 0, 2
		sendCs2(new ClientScriptVar(4717, 3874, 94240782, 94240780, 94240781));//Runscript: [4717, 94240781, 94240780, 94240782, 3874]
		//sendCs2(new ClientScriptVar(8862, 9, 1));//Runscript: [8862, 9, 1]
		
		sendInterface(true, 1477, 341, 1416);//Interface: id=1416, clipped=1, parent=[1477, 341] (Music player)
		sendInterfaceSettings(1416, 3, 0, 2443, 30);//IfaceSettings: 92798979, 2443, 0, 30
		sendInterfaceSettings(1416, 11, 0, 11, 2359302);//IfaceSettings: 92798987, 11, 0, 2359302
		sendInterfaceSettings(1416, 11, 12, 23, 4);//IfaceSettings: 92798987, 23, 12, 4
		sendInterfaceSettings(1416, 11, 24, 24, 2097152);//IfaceSettings: 92798987, 24, 24, 2097152
		//sendCs2(new ClientScriptVar(8862, 10, 1));//Runscript: [8862, 10, 1]
		
		sendInterface(true, 1477, 351, 1417);//Interface: id=1417, clipped=1, parent=[1477, 351] (Notes)
		sendInterfaceSettings(1417, 16, 0, 29, 2621470);//IfaceSettings: 92864528, 29, 0, 2621470
		//sendCs2(new ClientScriptVar(8862, 11, 1));//Runscript: [8862, 11, 1]
		//sendCs2(new ClientScriptVar(8862, 12, 0));//Runscript: [8862, 12, 0]
		
		setInterface(new RibbonInterface(player), 174, true);
		
		//sendInterface(true, 1477, 174, 1431);//Interface: id=1431, clipped=1, parent=[1477, 174] (Launcher bar (links to settings, social, powers, etc))
		sendInterface(true, 1477, 835, 568);//Interface: id=568, clipped=1, parent=[1477, 835]
		sendInterfaceSettings(1477, 175, 1, 1, 2);//IfaceSettings: 96796847, 1, 1, 2
		
		setInterface(player.getActionBar(), 58, true);//Action bar
		
		//sendInterface(true, 1477, 58, 1430);//Interface: id=1430, clipped=1, parent=[1477, 58] (Action bar)
		sendInterfaceSettings(1477, 80, 1, 1, 4);//IfaceSettings: 96796752, 1, 1, 4
		
		/*sendInterfaceSettings(1430, 118, -1, -1, 2098176);//IfaceSettings: 93716598, -1, -1, 2098176
		sendInterfaceSettings(1430, 123, -1, -1, 2098176);//IfaceSettings: 93716603, -1, -1, 2098176
		sendInterfaceSettings(1430, 124, -1, -1, 2098176);//IfaceSettings: 93716604, -1, -1, 2098176
		sendInterfaceSettings(1430, 129, -1, -1, 2098176);//IfaceSettings: 93716609, -1, -1, 2098176
		sendInterfaceSettings(1430, 130, -1, -1, 2098176);//IfaceSettings: 93716610, -1, -1, 2098176
		sendInterfaceSettings(1430, 135, -1, -1, 2098176);//IfaceSettings: 93716615, -1, -1, 2098176
		sendInterfaceSettings(1430, 136, -1, -1, 2098176);//IfaceSettings: 93716616, -1, -1, 2098176
		sendInterfaceSettings(1430, 141, -1, -1, 2098176);//IfaceSettings: 93716621, -1, -1, 2098176
		sendInterfaceSettings(1430, 142, -1, -1, 2098176);//IfaceSettings: 93716622, -1, -1, 2098176
		sendInterfaceSettings(1430, 147, -1, -1, 2098176);//IfaceSettings: 93716627, -1, -1, 2098176
		sendInterfaceSettings(1430, 148, -1, -1, 2098176);//IfaceSettings: 93716628, -1, -1, 2098176
		sendInterfaceSettings(1430, 153, -1, -1, 2098176);//IfaceSettings: 93716633, -1, -1, 2098176
		sendInterfaceSettings(1430, 154, -1, -1, 2098176);//IfaceSettings: 93716634, -1, -1, 2098176
		sendInterfaceSettings(1430, 159, -1, -1, 2098176);//IfaceSettings: 93716639, -1, -1, 2098176
		sendInterfaceSettings(1430, 160, -1, -1, 2098176);//IfaceSettings: 93716640, -1, -1, 2098176
		sendInterfaceSettings(1430, 165, -1, -1, 2098176);//IfaceSettings: 93716645, -1, -1, 2098176
		sendInterfaceSettings(1430, 166, -1, -1, 2098176);//IfaceSettings: 93716646, -1, -1, 2098176
		sendInterfaceSettings(1430, 171, -1, -1, 2098176);//IfaceSettings: 93716651, -1, -1, 2098176
		sendInterfaceSettings(1430, 172, -1, -1, 2098176);//IfaceSettings: 93716652, -1, -1, 2098176
		sendInterfaceSettings(1430, 177, -1, -1, 2098176);//IfaceSettings: 93716657, -1, -1, 2098176
		sendInterfaceSettings(1430, 178, -1, -1, 2098176);//IfaceSettings: 93716658, -1, -1, 2098176
		sendInterfaceSettings(1430, 183, -1, -1, 2098176);//IfaceSettings: 93716663, -1, -1, 2098176
		sendInterfaceSettings(1430, 184, -1, -1, 2098176);//IfaceSettings: 93716664, -1, -1, 2098176
		sendInterfaceSettings(1430, 189, -1, -1, 2098176);//IfaceSettings: 93716669, -1, -1, 2098176*/
		sendInterfaceSettings(1458, 24, 0, 28, 8388610);//IfaceSettings: 95551512, 28, 0, 8388610
		/*sendInterfaceSettings(1430, 10, -1, -1, 8388608);//IfaceSettings: 93716490, -1, -1, 8388608
		sendInterfaceSettings(1430, 8, -1, -1, 8650758);//IfaceSettings: 93716488, -1, -1, 8650758
		sendInterfaceSettings(1430, 11, -1, -1, 8388608);//IfaceSettings: 93716491, -1, -1, 8388608*/
		sendInterfaceSettings(1460, 1, 0, 168, 8485894);//IfaceSettings: 95682561, 168, 0, 8485894
		sendInterfaceSettings(1452, 1, 0, 168, 8485894);//IfaceSettings: 95158273, 168, 0, 8485894
		sendInterfaceSettings(1461, 1, 0, 168, 8485894);//IfaceSettings: 95748097, 168, 0, 8485894
		sendInterfaceSettings(1449, 1, 0, 168, 8485894);//IfaceSettings: 94961665, 168, 0, 8485894
		sendInterfaceSettings(590, 8, 0, 169, 8388614);//IfaceSettings: 38666248, 169, 0, 8388614
		
		setInterface(new MinimapInterface(player), 60, true);
		
		//sendInterface(true, 1477, 60, 1465);//Interface: id=1465, clipped=1, parent=[1477, 60] (Minimap)
		sendInterfaceSettings(1477, 82, 1, 1, 6);//IfaceSettings: 96796754, 1, 1, 6
		sendInterface(true, 1477, 34, 1433);//Interface: id=1433, clipped=1, parent=[1477, 34] (Settings menu)
		sendInterface(true, 1477, 390, 1483);//Interface: id=1483, clipped=1, parent=[1477, 390] (Grave timer)
		sendInterface(true, 1477, 409, 745);//Interface: id=745, clipped=1, parent=[1477, 409] (Assisting interface)
		sendInterface(true, 1477, 386, 1485);//Interface: id=1485, clipped=1, parent=[1477, 386]
		sendInterface(true, 1477, 0, 1213);//Interface: id=1213, clipped=1, parent=[1477, 0] (Skill popups)
		sendInterface(true, 1477, 76, 1448);//Interface: id=1448, clipped=1, parent=[1477, 76]
		sendInterface(true, 1477, 832, 557);//Interface: id=557, clipped=1, parent=[1477, 832] (Current task)
		sendInterface(true, 1477, 18, 1484);//Interface: id=1484, clipped=1, parent=[1477, 18]
		sendInterface(true, 1477, 106, 137);//Interface: id=137, clipped=1, parent=[1477, 106] (Chat box)
		sendInterface(true, 1477, 178, 1467);//Interface: id=1467, clipped=1, parent=[1477, 178] (Another chat box)
		sendInterface(true, 1477, 186, 1472);//Interface: id=1472, clipped=1, parent=[1477, 186] (Another chat box)
		sendInterface(true, 1477, 194, 1471);//Interface: id=1471, clipped=1, parent=[1477, 194] (Another chat box)
		sendInterface(true, 1477, 333, 1470);//Interface: id=1470, clipped=1, parent=[1477, 333] (Another chat box)
		sendInterface(true, 1477, 824, 464);//Interface: id=464, clipped=1, parent=[1477, 824] (Another chat box)
		sendInterface(true, 1477, 222, 182);//Interface: id=182, clipped=1, parent=[1477, 222]
		sendInterfaceSettings(137, 90, 0, 99, 2046);//IfaceSettings: 8978522, 99, 0, 2046
		sendInterfaceSettings(1467, 61, 0, 99, 2046);//IfaceSettings: 96141373, 99, 0, 2046
		sendInterfaceSettings(1472, 61, 0, 99, 2046);//IfaceSettings: 96469053, 99, 0, 2046
		sendInterfaceSettings(1471, 60, 0, 99, 2046);//IfaceSettings: 96403516, 99, 0, 2046
		sendInterfaceSettings(1470, 60, 0, 99, 2046);//IfaceSettings: 96337980, 99, 0, 2046
		sendInterfaceSettings(464, 63, 0, 99, 2046);//IfaceSettings: 30408767, 99, 0, 2046
		sendInterfaceSettings(182, 0, 0, 99, 2046);//IfaceSettings: 11927552, 99, 0, 2046
		sendInterfaceSettings(1477, 63, -1, -1, 2097152);//IfaceSettings: 96796735, -1, -1, 2097152
		sendCs2(new ClientScriptVar(139, 96796736));//Runscript: [139, 96796736]
		
		sendInterface(true, 1477, 38, 1488);//Interface: id=1488, clipped=1, parent=[1477, 38]
		sendCs2(new ClientScriptVar(8778));//Runscript: [8778]
		sendCs2(new ClientScriptVar(4704));//Runscript: [4704]
		sendCs2(new ClientScriptVar(4308, 0, 18));//Runscript: [4308, 18, 0]
		
		//sendInterface(true, 1477, 159, 669);//Interface: id=669, clipped=1, parent=[1477, 159] (Information box)
		sendInterface(true, 1477, 21, 1215);//Interface: id=1215, clipped=1, parent=[1477, 21] (Experience counter)
		sendCs2(new ClientScriptVar(5559, 3122093));//Runscript: [5559, 3122093]
		sendCs2(new ClientScriptVar(5559, 3122093));//Runscript: [5559, 3122093]
		sendCs2(new ClientScriptVar(5557, 1));//Runscript: [5557, 1]
		sendCs2(new ClientScriptVar(6501));//Runscript: [6501]
		sendCs2(new ClientScriptVar(279));//Runscript: [279]
		sendInterfaceSettings(1477, 173, 1, 7, 9175040);//IfaceSettings: 96796845, 7, 1, 9175040
		sendInterfaceSettings(1477, 173, 11, 13, 9175040);//IfaceSettings: 96796845, 13, 11, 9175040
		sendInterfaceSettings(1477, 173, 0, 0, 9175040);//IfaceSettings: 96796845, 0, 0, 9175040
		sendInterfaceSettings(1477, 78, -1, -1, 2097152);//IfaceSettings: 96796750, -1, -1, 2097152
		sendInterfaceSettings(1477, 57, 1, 7, 9175040);//IfaceSettings: 96796729, 7, 1, 9175040
		sendInterfaceSettings(1477, 57, 11, 13, 9175040);//IfaceSettings: 96796729, 13, 11, 9175040
		sendInterfaceSettings(1477, 57, 0, 0, 9175040);//IfaceSettings: 96796729, 0, 0, 9175040
		sendInterfaceSettings(1477, 79, -1, -1, 2097152);//IfaceSettings: 96796751, -1, -1, 2097152
		sendInterfaceSettings(1477, 62, 1, 7, 9175040);//IfaceSettings: 96796734, 7, 1, 9175040
		sendInterfaceSettings(1477, 62, 11, 13, 9175040);//IfaceSettings: 96796734, 13, 11, 9175040
		sendInterfaceSettings(1477, 62, 0, 0, 9175040);//IfaceSettings: 96796734, 0, 0, 9175040
		sendInterfaceSettings(1477, 62, 3, 4, 9175040);//IfaceSettings: 96796734, 4, 3, 9175040
		sendInterfaceSettings(1477, 81, -1, -1, 2097152);//IfaceSettings: 96796753, -1, -1, 2097152
		sendInterfaceSettings(1477, 109, 1, 7, 9175040);//IfaceSettings: 96796781, 7, 1, 9175040
		sendInterfaceSettings(1477, 109, 11, 13, 9175040);//IfaceSettings: 96796781, 13, 11, 9175040
		sendInterfaceSettings(1477, 109, 0, 0, 9175040);//IfaceSettings: 96796781, 0, 0, 9175040
		sendInterfaceSettings(1477, 109, 3, 4, 9175040);//IfaceSettings: 96796781, 4, 3, 9175040
		sendInterfaceSettings(1477, 99, -1, -1, 2097152);//IfaceSettings: 96796771, -1, -1, 2097152
		sendInterfaceSettings(1477, 180, 1, 7, 9175040);//IfaceSettings: 96796852, 7, 1, 9175040
		sendInterfaceSettings(1477, 180, 11, 13, 9175040);//IfaceSettings: 96796852, 13, 11, 9175040
		sendInterfaceSettings(1477, 180, 0, 0, 9175040);//IfaceSettings: 96796852, 0, 0, 9175040
		sendInterfaceSettings(1477, 180, 3, 4, 9175040);//IfaceSettings: 96796852, 4, 3, 9175040
		sendInterfaceSettings(1477, 100, -1, -1, 2097152);//IfaceSettings: 96796772, -1, -1, 2097152
		sendInterfaceSettings(1477, 188, 1, 7, 9175040);//IfaceSettings: 96796860, 7, 1, 9175040
		sendInterfaceSettings(1477, 188, 11, 13, 9175040);//IfaceSettings: 96796860, 13, 11, 9175040
		sendInterfaceSettings(1477, 188, 0, 0, 9175040);//IfaceSettings: 96796860, 0, 0, 9175040
		sendInterfaceSettings(1477, 188, 3, 4, 9175040);//IfaceSettings: 96796860, 4, 3, 9175040
		sendInterfaceSettings(1477, 101, -1, -1, 2097152);//IfaceSettings: 96796773, -1, -1, 2097152
		sendInterfaceSettings(1477, 196, 1, 7, 9175040);//IfaceSettings: 96796868, 7, 1, 9175040
		sendInterfaceSettings(1477, 196, 11, 13, 9175040);//IfaceSettings: 96796868, 13, 11, 9175040
		sendInterfaceSettings(1477, 196, 0, 0, 9175040);//IfaceSettings: 96796868, 0, 0, 9175040
		sendInterfaceSettings(1477, 196, 3, 4, 9175040);//IfaceSettings: 96796868, 4, 3, 9175040
		sendInterfaceSettings(1477, 102, -1, -1, 2097152);//IfaceSettings: 96796774, -1, -1, 2097152
		sendInterfaceSettings(1477, 335, 1, 7, 9175040);//IfaceSettings: 96797007, 7, 1, 9175040
		sendInterfaceSettings(1477, 335, 11, 13, 9175040);//IfaceSettings: 96797007, 13, 11, 9175040
		sendInterfaceSettings(1477, 335, 0, 0, 9175040);//IfaceSettings: 96797007, 0, 0, 9175040
		sendInterfaceSettings(1477, 335, 3, 4, 9175040);//IfaceSettings: 96797007, 4, 3, 9175040
		sendInterfaceSettings(1477, 103, -1, -1, 2097152);//IfaceSettings: 96796775, -1, -1, 2097152
		sendInterfaceSettings(1477, 826, 1, 7, 9175040);//IfaceSettings: 96797498, 7, 1, 9175040
		sendInterfaceSettings(1477, 826, 11, 13, 9175040);//IfaceSettings: 96797498, 13, 11, 9175040
		sendInterfaceSettings(1477, 826, 0, 0, 9175040);//IfaceSettings: 96797498, 0, 0, 9175040
		sendInterfaceSettings(1477, 826, 3, 4, 9175040);//IfaceSettings: 96797498, 4, 3, 9175040
		sendInterfaceSettings(1477, 104, -1, -1, 2097152);//IfaceSettings: 96796776, -1, -1, 2097152
		sendInterfaceSettings(1477, 306, 1, 7, 9175040);//IfaceSettings: 96796978, 7, 1, 9175040
		sendInterfaceSettings(1477, 306, 11, 13, 9175040);//IfaceSettings: 96796978, 13, 11, 9175040
		sendInterfaceSettings(1477, 306, 0, 0, 9175040);//IfaceSettings: 96796978, 0, 0, 9175040
		sendInterfaceSettings(1477, 306, 3, 4, 9175040);//IfaceSettings: 96796978, 4, 3, 9175040
		sendInterfaceSettings(1477, 97, -1, -1, 2097152);//IfaceSettings: 96796769, -1, -1, 2097152
		sendInterfaceSettings(1477, 133, 1, 7, 9175040);//IfaceSettings: 96796805, 7, 1, 9175040
		sendInterfaceSettings(1477, 133, 11, 13, 9175040);//IfaceSettings: 96796805, 13, 11, 9175040
		sendInterfaceSettings(1477, 133, 0, 0, 9175040);//IfaceSettings: 96796805, 0, 0, 9175040
		sendInterfaceSettings(1477, 133, 3, 4, 9175040);//IfaceSettings: 96796805, 4, 3, 9175040
		sendInterfaceSettings(1477, 98, -1, -1, 2097152);//IfaceSettings: 96796770, -1, -1, 2097152
		sendInterfaceSettings(1477, 134, 1, 1, 2);//IfaceSettings: 96796806, 1, 1, 2
		sendInterfaceSettings(1477, 242, 1, 7, 9175040);//IfaceSettings: 96796914, 7, 1, 9175040
		sendInterfaceSettings(1477, 242, 11, 13, 9175040);//IfaceSettings: 96796914, 13, 11, 9175040
		sendInterfaceSettings(1477, 242, 0, 0, 9175040);//IfaceSettings: 96796914, 0, 0, 9175040
		sendInterfaceSettings(1477, 242, 3, 4, 9175040);//IfaceSettings: 96796914, 4, 3, 9175040
		sendInterfaceSettings(1477, 117, -1, -1, 2097152);//IfaceSettings: 96796789, -1, -1, 2097152
		sendInterfaceSettings(1477, 243, 1, 1, 2);//IfaceSettings: 96796915, 1, 1, 2
		sendInterfaceSettings(1477, 252, 1, 7, 9175040);//IfaceSettings: 96796924, 7, 1, 9175040
		sendInterfaceSettings(1477, 252, 11, 13, 9175040);//IfaceSettings: 96796924, 13, 11, 9175040
		sendInterfaceSettings(1477, 252, 0, 0, 9175040);//IfaceSettings: 96796924, 0, 0, 9175040
		sendInterfaceSettings(1477, 252, 3, 4, 9175040);//IfaceSettings: 96796924, 4, 3, 9175040
		sendInterfaceSettings(1477, 118, -1, -1, 2097152);//IfaceSettings: 96796790, -1, -1, 2097152
		sendInterfaceSettings(1477, 253, 1, 1, 2);//IfaceSettings: 96796925, 1, 1, 2
		sendInterfaceSettings(1477, 262, 1, 7, 9175040);//IfaceSettings: 96796934, 7, 1, 9175040
		sendInterfaceSettings(1477, 262, 11, 13, 9175040);//IfaceSettings: 96796934, 13, 11, 9175040
		sendInterfaceSettings(1477, 262, 0, 0, 9175040);//IfaceSettings: 96796934, 0, 0, 9175040
		sendInterfaceSettings(1477, 262, 3, 4, 9175040);//IfaceSettings: 96796934, 4, 3, 9175040
		sendInterfaceSettings(1477, 119, -1, -1, 2097152);//IfaceSettings: 96796791, -1, -1, 2097152
		sendInterfaceSettings(1477, 263, 1, 1, 2);//IfaceSettings: 96796935, 1, 1, 2
		sendInterfaceSettings(1477, 272, 1, 7, 9175040);//IfaceSettings: 96796944, 7, 1, 9175040
		sendInterfaceSettings(1477, 272, 11, 13, 9175040);//IfaceSettings: 96796944, 13, 11, 9175040
		sendInterfaceSettings(1477, 272, 0, 0, 9175040);//IfaceSettings: 96796944, 0, 0, 9175040
		sendInterfaceSettings(1477, 272, 3, 4, 9175040);//IfaceSettings: 96796944, 4, 3, 9175040
		sendInterfaceSettings(1477, 120, -1, -1, 2097152);//IfaceSettings: 96796792, -1, -1, 2097152
		sendInterfaceSettings(1477, 273, 1, 1, 2);//IfaceSettings: 96796945, 1, 1, 2
		sendInterfaceSettings(1477, 205, 1, 7, 9175040);//IfaceSettings: 96796877, 7, 1, 9175040
		sendInterfaceSettings(1477, 205, 11, 13, 9175040);//IfaceSettings: 96796877, 13, 11, 9175040
		sendInterfaceSettings(1477, 205, 0, 0, 9175040);//IfaceSettings: 96796877, 0, 0, 9175040
		sendInterfaceSettings(1477, 205, 3, 4, 9175040);//IfaceSettings: 96796877, 4, 3, 9175040
		sendInterfaceSettings(1477, 114, -1, -1, 2097152);//IfaceSettings: 96796786, -1, -1, 2097152
		sendInterfaceSettings(1477, 206, 1, 1, 2);//IfaceSettings: 96796878, 1, 1, 2
		sendInterfaceSettings(1477, 215, 1, 7, 9175040);//IfaceSettings: 96796887, 7, 1, 9175040
		sendInterfaceSettings(1477, 215, 11, 13, 9175040);//IfaceSettings: 96796887, 13, 11, 9175040
		sendInterfaceSettings(1477, 215, 0, 0, 9175040);//IfaceSettings: 96796887, 0, 0, 9175040
		sendInterfaceSettings(1477, 215, 3, 4, 9175040);//IfaceSettings: 96796887, 4, 3, 9175040
		sendInterfaceSettings(1477, 115, -1, -1, 2097152);//IfaceSettings: 96796787, -1, -1, 2097152
		sendInterfaceSettings(1477, 216, 1, 1, 2);//IfaceSettings: 96796888, 1, 1, 2
		sendInterfaceSettings(1477, 296, 1, 7, 9175040);//IfaceSettings: 96796968, 7, 1, 9175040
		sendInterfaceSettings(1477, 296, 11, 13, 9175040);//IfaceSettings: 96796968, 13, 11, 9175040
		sendInterfaceSettings(1477, 296, 0, 0, 9175040);//IfaceSettings: 96796968, 0, 0, 9175040
		sendInterfaceSettings(1477, 296, 3, 4, 9175040);//IfaceSettings: 96796968, 4, 3, 9175040
		sendInterfaceSettings(1477, 123, -1, -1, 2097152);//IfaceSettings: 96796795, -1, -1, 2097152
		sendInterfaceSettings(1477, 297, 1, 1, 2);//IfaceSettings: 96796969, 1, 1, 2
		sendInterfaceSettings(1477, 281, 1, 7, 9175040);//IfaceSettings: 96796953, 7, 1, 9175040
		sendInterfaceSettings(1477, 281, 11, 13, 9175040);//IfaceSettings: 96796953, 13, 11, 9175040
		sendInterfaceSettings(1477, 281, 0, 0, 9175040);//IfaceSettings: 96796953, 0, 0, 9175040
		sendInterfaceSettings(1477, 281, 3, 4, 9175040);//IfaceSettings: 96796953, 4, 3, 9175040
		sendInterfaceSettings(1477, 121, -1, -1, 2097152);//IfaceSettings: 96796793, -1, -1, 2097152
		sendInterfaceSettings(1477, 282, 1, 1, 2);//IfaceSettings: 96796954, 1, 1, 2
		sendInterfaceSettings(1477, 316, 1, 7, 9175040);//IfaceSettings: 96796988, 7, 1, 9175040
		sendInterfaceSettings(1477, 316, 11, 13, 9175040);//IfaceSettings: 96796988, 13, 11, 9175040
		sendInterfaceSettings(1477, 316, 0, 0, 9175040);//IfaceSettings: 96796988, 0, 0, 9175040
		sendInterfaceSettings(1477, 316, 3, 4, 9175040);//IfaceSettings: 96796988, 4, 3, 9175040
		sendInterfaceSettings(1477, 122, -1, -1, 2097152);//IfaceSettings: 96796794, -1, -1, 2097152
		sendInterfaceSettings(1477, 317, 1, 1, 2);//IfaceSettings: 96796989, 1, 1, 2
		sendInterfaceSettings(1477, 326, 1, 7, 9175040);//IfaceSettings: 96796998, 7, 1, 9175040
		sendInterfaceSettings(1477, 326, 11, 13, 9175040);//IfaceSettings: 96796998, 13, 11, 9175040
		sendInterfaceSettings(1477, 326, 0, 0, 9175040);//IfaceSettings: 96796998, 0, 0, 9175040
		sendInterfaceSettings(1477, 326, 3, 4, 9175040);//IfaceSettings: 96796998, 4, 3, 9175040
		sendInterfaceSettings(1477, 116, -1, -1, 2097152);//IfaceSettings: 96796788, -1, -1, 2097152
		sendInterfaceSettings(1477, 327, 1, 1, 2);//IfaceSettings: 96796999, 1, 1, 2
		sendInterfaceSettings(1477, 344, 1, 7, 9175040);//IfaceSettings: 96797016, 7, 1, 9175040
		sendInterfaceSettings(1477, 344, 11, 13, 9175040);//IfaceSettings: 96797016, 13, 11, 9175040
		sendInterfaceSettings(1477, 344, 0, 0, 9175040);//IfaceSettings: 96797016, 0, 0, 9175040
		sendInterfaceSettings(1477, 344, 3, 4, 9175040);//IfaceSettings: 96797016, 4, 3, 9175040
		sendInterfaceSettings(1477, 124, -1, -1, 2097152);//IfaceSettings: 96796796, -1, -1, 2097152
		sendInterfaceSettings(1477, 345, 1, 1, 2);//IfaceSettings: 96797017, 1, 1, 2
		sendInterfaceSettings(1477, 354, 1, 7, 9175040);//IfaceSettings: 96797026, 7, 1, 9175040
		sendInterfaceSettings(1477, 354, 11, 13, 9175040);//IfaceSettings: 96797026, 13, 11, 9175040
		sendInterfaceSettings(1477, 354, 0, 0, 9175040);//IfaceSettings: 96797026, 0, 0, 9175040
		sendInterfaceSettings(1477, 354, 3, 4, 9175040);//IfaceSettings: 96797026, 4, 3, 9175040
		sendInterfaceSettings(1477, 127, -1, -1, 2097152);//IfaceSettings: 96796799, -1, -1, 2097152
		sendInterfaceSettings(1477, 355, 1, 1, 2);//IfaceSettings: 96797027, 1, 1, 2
		sendInterfaceSettings(1477, 374, 1, 7, 9175040);//IfaceSettings: 96797046, 7, 1, 9175040
		sendInterfaceSettings(1477, 374, 11, 13, 9175040);//IfaceSettings: 96797046, 13, 11, 9175040
		sendInterfaceSettings(1477, 374, 0, 0, 9175040);//IfaceSettings: 96797046, 0, 0, 9175040
		sendInterfaceSettings(1477, 374, 3, 4, 9175040);//IfaceSettings: 96797046, 4, 3, 9175040
		sendInterfaceSettings(1477, 125, -1, -1, 2097152);//IfaceSettings: 96796797, -1, -1, 2097152
		sendInterfaceSettings(1477, 375, 1, 1, 2);//IfaceSettings: 96797047, 1, 1, 2
		sendInterfaceSettings(1477, 364, 1, 7, 9175040);//IfaceSettings: 96797036, 7, 1, 9175040
		sendInterfaceSettings(1477, 364, 11, 13, 9175040);//IfaceSettings: 96797036, 13, 11, 9175040
		sendInterfaceSettings(1477, 364, 0, 0, 9175040);//IfaceSettings: 96797036, 0, 0, 9175040
		sendInterfaceSettings(1477, 364, 3, 4, 9175040);//IfaceSettings: 96797036, 4, 3, 9175040
		sendInterfaceSettings(1477, 126, -1, -1, 2097152);//IfaceSettings: 96796798, -1, -1, 2097152
		sendInterfaceSettings(1477, 365, 1, 1, 2);//IfaceSettings: 96797037, 1, 1, 2
		sendInterfaceSettings(1477, 605, 1, 7, 9175040);//IfaceSettings: 96797277, 7, 1, 9175040
		sendInterfaceSettings(1477, 605, 11, 13, 9175040);//IfaceSettings: 96797277, 13, 11, 9175040
		sendInterfaceSettings(1477, 605, 0, 0, 9175040);//IfaceSettings: 96797277, 0, 0, 9175040
		sendInterfaceSettings(1477, 605, 3, 4, 9175040);//IfaceSettings: 96797277, 4, 3, 9175040
		sendInterfaceSettings(1477, 128, -1, -1, 2097152);//IfaceSettings: 96796800, -1, -1, 2097152
		sendInterfaceSettings(1477, 606, 1, 1, 2);//IfaceSettings: 96797278, 1, 1, 2
		sendInterfaceSettings(1477, 407, 1, 7, 9175040);//IfaceSettings: 96797079, 7, 1, 9175040
		sendInterfaceSettings(1477, 407, 11, 13, 9175040);//IfaceSettings: 96797079, 13, 11, 9175040
		sendInterfaceSettings(1477, 407, 0, 0, 9175040);//IfaceSettings: 96797079, 0, 0, 9175040
		sendInterfaceSettings(1477, 407, 3, 4, 9175040);//IfaceSettings: 96797079, 4, 3, 9175040
		sendInterfaceSettings(1477, 235, 1, 2, 9175040);//IfaceSettings: 96796907, 2, 1, 9175040
		sendInterfaceSettings(1477, 235, 0, 0, 9175040);//IfaceSettings: 96796907, 0, 0, 9175040
		sendInterfaceSettings(1477, 235, 3, 4, 9175040);//IfaceSettings: 96796907, 4, 3, 9175040
		sendInterfaceSettings(1477, 237, 1, 2, 9175040);//IfaceSettings: 96796909, 2, 1, 9175040
		sendInterfaceSettings(1477, 237, 0, 0, 9175040);//IfaceSettings: 96796909, 0, 0, 9175040
		sendInterfaceSettings(1477, 237, 3, 4, 9175040);//IfaceSettings: 96796909, 4, 3, 9175040
		sendInterfaceSettings(1477, 290, 0, 0, 9175040);//IfaceSettings: 96796962, 0, 0, 9175040
		sendInterfaceSettings(1477, 51, -1, -1, 2097152);//IfaceSettings: 96796723, -1, -1, 2097152
		sendInterfaceSettings(1477, 291, 1, 1, 2);//IfaceSettings: 96796963, 1, 1, 2
		sendInterfaceSettings(1477, 387, 1, 2, 9175040);//IfaceSettings: 96797059, 2, 1, 9175040
		sendInterfaceSettings(1477, 387, 0, 0, 9175040);//IfaceSettings: 96797059, 0, 0, 9175040
		sendInterfaceSettings(1477, 387, 3, 4, 9175040);//IfaceSettings: 96797059, 4, 3, 9175040
		sendInterfaceSettings(1477, 391, 1, 2, 9175040);//IfaceSettings: 96797063, 2, 1, 9175040
		sendInterfaceSettings(1477, 391, 0, 0, 9175040);//IfaceSettings: 96797063, 0, 0, 9175040
		sendInterfaceSettings(1477, 391, 3, 4, 9175040);//IfaceSettings: 96797063, 4, 3, 9175040
		sendInterfaceSettings(1477, 395, 1, 2, 9175040);//IfaceSettings: 96797067, 2, 1, 9175040
		sendInterfaceSettings(1477, 395, 0, 0, 9175040);//IfaceSettings: 96797067, 0, 0, 9175040
		sendInterfaceSettings(1477, 395, 3, 4, 9175040);//IfaceSettings: 96797067, 4, 3, 9175040
		sendInterfaceSettings(1477, 404, 1, 2, 9175040);//IfaceSettings: 96797076, 2, 1, 9175040
		sendInterfaceSettings(1477, 404, 0, 0, 9175040);//IfaceSettings: 96797076, 0, 0, 9175040
		sendInterfaceSettings(1477, 404, 3, 4, 9175040);//IfaceSettings: 96797076, 4, 3, 9175040
		sendInterfaceSettings(1477, 383, 1, 2, 9175040);//IfaceSettings: 96797055, 2, 1, 9175040
		sendInterfaceSettings(1477, 383, 0, 0, 9175040);//IfaceSettings: 96797055, 0, 0, 9175040
		sendInterfaceSettings(1477, 383, 3, 4, 9175040);//IfaceSettings: 96797055, 4, 3, 9175040
		sendInterfaceSettings(1477, 410, 1, 2, 9175040);//IfaceSettings: 96797082, 2, 1, 9175040
		sendInterfaceSettings(1477, 410, 0, 0, 9175040);//IfaceSettings: 96797082, 0, 0, 9175040
		sendInterfaceSettings(1477, 410, 3, 4, 9175040);//IfaceSettings: 96797082, 4, 3, 9175040
		sendInterfaceSettings(1477, 22, 1, 2, 9175040);//IfaceSettings: 96796694, 2, 1, 9175040
		sendInterfaceSettings(1477, 22, 0, 0, 9175040);//IfaceSettings: 96796694, 0, 0, 9175040
		sendInterfaceSettings(1477, 22, 3, 4, 9175040);//IfaceSettings: 96796694, 4, 3, 9175040
		sendInterfaceSettings(1477, 19, 1, 2, 9175040);//IfaceSettings: 96796691, 2, 1, 9175040
		sendInterfaceSettings(1477, 19, 0, 0, 9175040);//IfaceSettings: 96796691, 0, 0, 9175040
		sendInterfaceSettings(1477, 19, 3, 4, 9175040);//IfaceSettings: 96796691, 4, 3, 9175040
		sendInterfaceSettings(1477, 16, 1, 2, 9175040);//IfaceSettings: 96796688, 2, 1, 9175040
		sendInterfaceSettings(1477, 16, 5, 5, 9175040);//IfaceSettings: 96796688, 5, 5, 9175040
		sendInterfaceSettings(1477, 16, 11, 13, 9175040);//IfaceSettings: 96796688, 13, 11, 9175040
		sendInterfaceSettings(1477, 16, 0, 0, 9175040);//IfaceSettings: 96796688, 0, 0, 9175040
		sendInterfaceSettings(1477, 16, 3, 4, 9175040);//IfaceSettings: 96796688, 4, 3, 9175040
		sendInterfaceSettings(1477, 728, 1, 2, 9175040);//IfaceSettings: 96797400, 2, 1, 9175040
		sendInterfaceSettings(1477, 728, 0, 0, 9175040);//IfaceSettings: 96797400, 0, 0, 9175040
		sendInterfaceSettings(1477, 728, 3, 4, 9175040);//IfaceSettings: 96797400, 4, 3, 9175040
		sendInterfaceSettings(1477, 223, 1, 7, 9175040);//IfaceSettings: 96796895, 7, 1, 9175040
		sendInterfaceSettings(1477, 223, 11, 13, 9175040);//IfaceSettings: 96796895, 13, 11, 9175040
		sendInterfaceSettings(1477, 223, 0, 0, 9175040);//IfaceSettings: 96796895, 0, 0, 9175040
		sendInterfaceSettings(1477, 223, 3, 4, 9175040);//IfaceSettings: 96796895, 4, 3, 9175040
		sendInterfaceSettings(1477, 55, 1, 2, 9175040);//IfaceSettings: 96796727, 2, 1, 9175040
		sendInterfaceSettings(1477, 55, 0, 0, 9175040);//IfaceSettings: 96796727, 0, 0, 9175040
		sendInterfaceSettings(1477, 55, 3, 4, 9175040);//IfaceSettings: 96796727, 4, 3, 9175040
		sendInterfaceSettings(1477, 12, -1, -1, 2097152);//IfaceSettings: 96796684, -1, -1, 2097152
		sendInterfaceSettings(1477, 77, 1, 1, 2);//IfaceSettings: 96796749, 1, 1, 2
		sendInterfaceSettings(1477, 96, 1, 7, 9175040);//IfaceSettings: 96796768, 7, 1, 9175040
		sendInterfaceSettings(1477, 96, 11, 13, 9175040);//IfaceSettings: 96796768, 13, 11, 9175040
		sendInterfaceSettings(1477, 96, 0, 0, 9175040);//IfaceSettings: 96796768, 0, 0, 9175040
		sendInterfaceSettings(1477, 96, 3, 4, 9175040);//IfaceSettings: 96796768, 4, 3, 9175040
		sendInterfaceSettings(1477, 7, -1, -1, 2097152);//IfaceSettings: 96796679, -1, -1, 2097152
		sendInterfaceSettings(1477, 833, 1, 2, 9175040);//IfaceSettings: 96797505, 2, 1, 9175040
		sendInterfaceSettings(1477, 833, 0, 0, 9175040);//IfaceSettings: 96797505, 0, 0, 9175040
		sendInterfaceSettings(1477, 833, 3, 4, 9175040);//IfaceSettings: 96797505, 4, 3, 9175040
		//player.getAccount().getSession().getTransmitter().send(VarpEncoder.class, new VarpMessage(823, 1, true));
	}
	
	public void sendMeleePowersTab() {
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(3708));
		sendInterface(true, 1448, 3, 1450);
		sendInterface(true, 1448, 3, 1450);
		sendInterfaceSettings(1450, 0, 6, 14, 2);
		sendInterfaceSettings(1450, 3, 0, 168, 10320902);
		sendInterface(true, 1448, 5, 1454);
		sendInterface(true, 1448, 7, 1435);
		sendInterface(true, 1448, 9, 1436);
		sendInterfaceSettings(1430, 97, -1, -1, 11108350);
		sendInterfaceSettings(1430, 101, -1, -1, 11108350);
		sendInterfaceSettings(1430, 102, -1, -1, 2098176);
		sendInterfaceSettings(1430, 106, -1, -1, 2098176);
		sendInterfaceSettings(1430, 107, -1, -1, 2098176);
		sendInterfaceSettings(1430, 111, -1, -1, 2098176);
		sendInterfaceSettings(1430, 112, -1, -1, 2098176);
		sendInterfaceSettings(1430, 116, -1, -1, 2098176);
		sendInterfaceSettings(1430, 117, -1, -1, 2098176);
		sendInterfaceSettings(1430, 121, -1, -1, 2098176);
		sendInterfaceSettings(1430, 122, -1, -1, 2098176);
		sendInterfaceSettings(1430, 126, -1, -1, 2098176);
		sendInterfaceSettings(1430, 127, -1, -1, 2098176);
		sendInterfaceSettings(1430, 131, -1, -1, 2098176);
		sendInterfaceSettings(1430, 132, -1, -1, 2098176);
		sendInterfaceSettings(1430, 136, -1, -1, 2098176);
		sendInterfaceSettings(1430, 137, -1, -1, 2098176);
		sendInterfaceSettings(1430, 141, -1, -1, 2098176);
		sendInterfaceSettings(1430, 142, -1, -1, 2098176);
		sendInterfaceSettings(1430, 146, -1, -1, 2098176);
		sendInterfaceSettings(1430, 147, -1, -1, 2098176);
		sendInterfaceSettings(1430, 151, -1, -1, 2098176);
		sendInterfaceSettings(1430, 152, -1, -1, 2098176);
		sendInterfaceSettings(1430, 156, -1, -1, 2098176);
		sendInterfaceSettings(1436, 12, -1, -1, 11108350);
		sendInterfaceSettings(1436, 16, -1, -1, 11108350);
		sendInterfaceSettings(1436, 22, -1, -1, 2098176);
		sendInterfaceSettings(1436, 26, -1, -1, 2098176);
		sendInterfaceSettings(1436, 32, -1, -1, 2098176);
		sendInterfaceSettings(1436, 36, -1, -1, 2098176);
		sendInterfaceSettings(1436, 42, -1, -1, 2098176);
		sendInterfaceSettings(1436, 46, -1, -1, 2098176);
		sendInterfaceSettings(1436, 52, -1, -1, 2098176);
		sendInterfaceSettings(1436, 56, -1, -1, 2098176);
		sendInterfaceSettings(1436, 62, -1, -1, 2098176);
		sendInterfaceSettings(1436, 66, -1, -1, 2098176);
		sendInterfaceSettings(1436, 72, -1, -1, 2098176);
		sendInterfaceSettings(1436, 76, -1, -1, 2098176);
		sendInterfaceSettings(1436, 82, -1, -1, 2098176);
		sendInterfaceSettings(1436, 86, -1, -1, 2098176);
		sendInterfaceSettings(1436, 92, -1, -1, 2098176);
		sendInterfaceSettings(1436, 96, -1, -1, 2098176);
		sendInterfaceSettings(1436, 102, -1, -1, 2098176);
		sendInterfaceSettings(1436, 106, -1, -1, 2098176);
		sendInterfaceSettings(1436, 112, -1, -1, 2098176);
		sendInterfaceSettings(1436, 116, -1, -1, 2098176);
		sendInterfaceSettings(1436, 122, -1, -1, 2098176);
		sendInterfaceSettings(1436, 126, -1, -1, 2098176);
		sendInterfaceSettings(1458, 24, 0, 28, 8388610);
		sendInterfaceSettings(1430, 4, -1, -1, 8388608);
		//sendInterfaceSettings(1465, 4, -1, -1, 8388608);
		sendInterfaceSettings(1430, 2, -1, -1, 8650754);
		sendInterfaceSettings(1430, 5, -1, -1, 8388608);
		sendInterfaceSettings(1450, 3, 0, 168, 8485894);
		sendInterfaceSettings(1460, 1, 0, 168, 8485894);
		sendInterfaceSettings(1452, 1, 0, 168, 8485894);
		sendInterfaceSettings(1461, 1, 0, 168, 8485894);
		sendInterfaceSettings(1449, 1, 0, 168, 8485894);
		sendInterfaceSettings(590, 8, 0, 166, 8388614);
		
	}
	
	public void setTopInterface (AbstractInterface iFace) {
		if (topLevelInterface != null) {
			topLevelInterface.close();
		}
		this.topLevelInterface = iFace;
		setInterface(iFace, 236, false);
	}
	
	public void openBank () {
		if (topLevelInterface != null) {
			topLevelInterface.close();
		}
		this.topLevelInterface = player.getBank();
		setInterface(player.getBank(), 13, false);
	}
	
	public void closeTopInterface () {
		if (topLevelInterface != null) {
			topLevelInterface.close();
		}
		this.topLevelInterface = null;
	}
	
	public void setInterface(AbstractInterface tab, int windowLocation, boolean clipped) {
		setInterface(tab, windowLocation, clipped, GAME_WINDOW_PANE);
	}
	
	public void setInterface(AbstractInterface tab, int windowLocation, boolean clipped, int windowID) {
		tab.send(windowID, windowLocation, clipped);
		tabInterfaces.put(tab.getID(), tab);
	}
	
	/**
	 * Returns the interface for the specified ID
	 * @param id	The ID for the interface
	 * @return		The {@code AbstractInterface} object for the interface 
	 */
	public AbstractInterface getInterface (int id) {
		return tabInterfaces.get(id);
	}
	
	/**
	 * Sends an interface.
	 * @param walkable If the interface is walkable.
	 * @param windowId The window ID.
	 * @param windowLocation The window location.
	 * @param interfaceId The interface ID.
	 */
	public void sendInterface(boolean walkable, int windowId, int windowLocation, int interfaceId) {
		player.getPacketDispatcher().dispatchInterface(new InterfaceMessage(interfaceId, windowLocation, windowId, walkable));
	}
	
	public void sendCs2 (ClientScriptVar scriptData) {
		player.getPacketDispatcher().dispatchClientScriptVar(scriptData);
	}	
	
	public void sendInterfaceSettings(int interfaceID, int component, int fromSlot, int toSlot, int settings) {
		player.getAccount().getSession().getTransmitter().send(InterfaceSettingsEncoder.class, new InterfaceSettingsMessage(interfaceID, component, fromSlot, toSlot, settings));
	}
	

	public void sendHideIcomponent(int interfaceID, int component, boolean hidden) {
		player.getAccount().getSession().getTransmitter().send(InterfaceSettingsEncoder.class, new InterfaceSettingsMessage(interfaceID, component, hidden));
	}
	
	public void sendXPPopup() {
		//sendTab(resizableScreen ? 38 : 10, 1213); //xp 
	}
	
	public void sendXPDisplay() {
		sendXPDisplay(1215);  //xp counter
	}
	
	public void sendXPDisplay(int interfaceId) {
		//sendTab(resizableScreen ? 27 : 29, interfaceId);  //xp counter
	}
	
	public void sendEquipment() {
		//sendTab(resizableScreen ? 116 : 176, 387);
	}
	
	public void sendCombatStyles() {
		//sendTab(resizableScreen ? 111 : 171, 884);
	}
	
	public void sendTaskSystem() {
		//sendTab(resizableScreen ? 112 : 172, 1056);
	}

	public void sendSkills() {
		//sendTab(resizableScreen ? 113 : 173, 320);
	}

	public void sendSettings(int interfaceId) {
		//sendTab(resizableScreen ? 123 : 183, interfaceId);
	}

	public void sendPrayerBook() {
		//sendTab(resizableScreen ? 117 : 177, 271);
	}

	public void sendMagicBook() {
		//sendTab(resizableScreen ? 118 : 178, 192);
	}
	
	public void sendEmotes() {
		//sendTab(resizableScreen ? 124 : 184, 590);
	}
}
