package org.virtue.network.protocol.handlers.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class CommandRepository {
	
	/**
	 * Represents a list of commands.
	 */
	private List<Command> commands = new ArrayList<>();
	
	/**
	 * Constructs a new {@code CommandRepository.java}.
	 */
	public CommandRepository() {
		commands.add(new TestCommand());
		commands.add(new AddPlayer());
		commands.add(new SetClientVarp());
		commands.add(new SendItem());
		commands.add(new ReloadAppearance());
		commands.add(new SpawnNpc());
		commands.add(new AddExperience());
		commands.add(new SystemUpdate());
		commands.add(new SendInterface());
		commands.add(new SetNoClip());
		commands.add(new ShowBank());
		commands.add(new ShowGraphics());	
		commands.add(new ShowAnimation());
		commands.add(new CheckPassword());
		
	}
	
	/**
	 * Returns a command corresponding to the syntax.
	 * @param syntax The syntax of the command.
	 * @return The command.
	 */
	public Command forSyntax(String syntax) {
		for (Command command : commands) {
			for (String s : command.getPossibleSyntaxes()) {
				if (s.equalsIgnoreCase(syntax)) {
					return command;
				}
			}
		}
		return null;
	}
}
