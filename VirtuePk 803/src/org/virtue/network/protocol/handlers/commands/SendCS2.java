package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;

public class SendCS2 implements Command {

    @Override
    public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
        int id, value;
		try {
			id = Integer.parseInt(args[0]);
			value = Integer.parseInt(args[1]);
		} catch (Exception ex) {
			return false;
		}
		//player.getPacketDispatcher().dispatchVarp(new VarpMessage(key, value));
		//System.out.println("Sending cs2 to client: id="+id+", params="+value);
		return false;
    }

    @Override
    public String[] getPossibleSyntaxes() {
        return new String[] { "sendcs2", "runscript" };
    }
    
}
