package org.virtue.network.protocol.handlers.impl;

import org.virtue.network.RS2Network;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.handlers.commands.Command;
import org.virtue.network.session.WorldSession;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class CommandHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		String cmd = getFlag("syntax", "");
		String[] args = new String[0];
		if (cmd == null || cmd.length() < 1) {
			return;
		}
		String syntax = cmd.split(" ")[0];
//		args = new String[cmd.length() - syntax.length()];
//		System.arraycopy(cmd, 0, args, 0, args.length);
		Command command = RS2Network.getCommands().forSyntax(syntax);
		if (command == null) {
			System.err.println("Unhandled command: " + syntax);
			return;
		}
		command.handle(syntax, session.getPlayer(), getFlag("client_command", false), args);
	}
}
