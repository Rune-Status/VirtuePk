package org.virtue.network.protocol.messages;

import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class GameMessage {
	
	/**
	 * @author Taylor Moon
	 * @since Jan 27, 2014
	 */
	public enum MessageOpcode {
		
		/**
		 * Represents a default game message in the chatbox.
		 */
		CHAT_BOX(0),
		
		/**
		 * Friend/Ignore-related
		 */
		GAME_PRIVATE(4),
		/**
		 * Represents a default game message in the chatbox, yet with filter
		 * properties enabled.
		 */
		CHAT_BOX_FILTER(109),

		/**
		 * Represents a console message in the developer console.
		 */
		CONSOLE(99),

		/**
		 * Represents a trade request message.
		 */
		TRADE_REQUEST(100),

		/**
		 * Represetns a default minigame/activity request message.
		 */
		DEFAULT_REQUEST(101);

		/**
		 * Represents the message opcode.
		 */
		private final int opcode;

		/**
		 * Constructs a new {@code GameMessage.java}.
		 * @param opcode The message opcode.
		 */
		MessageOpcode(int opcode) {
			this.opcode = opcode;
		}

		/**
		 * @return the opcode
		 */
		public int getOpcode() {
			return opcode;
		}
	}
	
	/**
	 * Represents the opcode of the message.
	 */
	private final MessageOpcode opcode;
	
	/**
	 * Represents the string of text to be displayed when this message is send
	 */
	private final String message;
	
	/**
	 * Represents the player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code GameMessage.java}.
	 * @param opcode The opcode of the message.
	 * @param message The string of text to be displayed when this message is send
	 * @param tileHash The player.
	 */
	public GameMessage(MessageOpcode opcode, String message, Player player) {
		this.opcode = opcode;
		this.message = message;
		this.player = player;
	}

	/**
	 * @return the opcode
	 */
	public MessageOpcode getOpcode() {
		return opcode;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
