package org.virtue.game.logic.node.interfaces;

import org.virtue.network.protocol.messages.InterfaceMessage;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class RS3Interface {	

		public static final InterfaceMessage DIALOG_BOX = new InterfaceMessage(1418, 233, RS3Interface.GAME_SCREEN, true);
		
		public static final InterfaceMessage INPUT_DIALOG = new InterfaceMessage(1469, 0, 1418, true);
		
		public static final int INT_INPUT_SCRIPT = 108;
        
        public static final int RIBBON = 1431;
        
        public static final int MINIMAP = 1465;
        
        public static final int GAME_SCREEN = 1477;
        
        public static final int MANAGEMENT = 1448;
        
        public static final int BANK = 762;
        
        //Standard top-level interfaces
        
        public static final int FRIENDS_CHAT_SETTINGS = 1108;
        
        public static final int CLAN_SETTINGS = 1096;
		
		public static final int CLAN_BROADCAST_SETTINGS = 573;
		
		public static final int LODESTONE = 1092;
        
		//Tab interfaces
	
        public static final int FRIENDS_LIST = 550;   
        
        public static final int FRIENDS_CHAT_INFO = 1427;
        
        public static final int CLAN = 1110;
		
        public static final int EQUIPMENT = 1464;
    
        public static final int INVENTORY = 1473;
        
        public static final int SKILLS = 1466;
        
        //Management interfaces
        
        public static final int POLLS = 1029;
        
        public static final int CHALLANGES = 1343;   
        
        public static final int MINIGAMES = 1344; 
        
        public static final int LOADOUT = 1463;  //1474  
        
        
}
