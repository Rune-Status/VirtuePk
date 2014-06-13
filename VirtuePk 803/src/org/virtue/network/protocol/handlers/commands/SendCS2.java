package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class SendCS2 implements Command {

    @Override
    public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
    	if (args.length < 1) {
			player.getPacketDispatcher().dispatchMessage("You must specifiy a client script ID.", MessageOpcode.CONSOLE);
			return false;
		}
        int id;
        
		try {
			id = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			return false;
		}
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(id));
		//System.out.println("Sending cs2 to client: id="+id+", params="+value);
		return true;
    }

    @Override
    public String[] getPossibleSyntaxes() {
        return new String[] { "sendcs2", "runscript", "cs2" };
    }
    
}
