package org.virtue.network.protocol.handlers.impl;

import org.virtue.network.RS2Network;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.handlers.commands.Command;
import org.virtue.network.session.impl.WorldSession;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class CommandHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		String cmd = getFlag("syntax", "");
		if (cmd == null || cmd.length() < 1) {
			return;
		}
		String syntax = cmd.split(" ")[0];		
		String[] args = new String[cmd.split(" ").length - 1];
		System.arraycopy(cmd.split(" "), 1, args, 0, args.length);
		Command command = RS2Network.getCommands().forSyntax(syntax);
		if (command == null) {
			System.err.println("Unhandled command: " + syntax);
			return;
		}
		command.handle(syntax, session.getPlayer(), getFlag("client_command", false), args);
	}
}
