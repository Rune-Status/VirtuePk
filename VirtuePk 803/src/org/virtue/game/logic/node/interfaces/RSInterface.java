package org.virtue.game.logic.node.interfaces;

import org.virtue.network.protocol.messages.InterfaceMessage;

/**
 *
 * @author author Virtue Development Team 2014 (c).
 */
public class RSInterface {	

		public static InterfaceMessage dialog = new InterfaceMessage(1418, 233, RSInterface.GAME_SCREEN, true);
		
		public static InterfaceMessage input = new InterfaceMessage(1469, 0, 1418, true);
	
        public static final int FRIENDS_LIST = 550;   
        
        public static final int RIBBON = 1431;
        
        public static final int POLLS = 1029;
        
        public static final int FRIENDS_CHAT_SETTINGS = 1108;
        
        public static final int FRIENDS_CHAT_INFO = 1427;
        
        public static final int EQUIPMENT = 1464;
    
        public static final int INVENTORY = 1473;
        
        public static final int MINIMAP = 1465;
        
        public static final int SKILLS = 1466;
        
        public static final int GAME_SCREEN = 1477;
        
}
