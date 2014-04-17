package org.virtue.game.logic.node.entity.player.identity;

import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * @author Taylor
 * @date Jan 15, 2014
 */
public class Username {

	/**
	 * Represents the username.
	 */
	private String username;
	
	/**
	 * Represents the display name.
	 */
	private String displayName;
	
	/**
	 * Represents whether the player has a title or not
	 */
	private boolean hasTitle = false;
	
	/**
	 * Constructs a new {@code Username.java}.
	 * @param username The name of the user's account.
	 * @param displayName The name to be displayed in game.
	 */
	public Username(String username, String displayName) {
		this.username = username;
		this.displayName = displayName;
	}
	
	/**
	 * Constructs a new {@code Username.java}.
	 * @param username The username.
	 */
	public Username(String username) {
		this(username, username);
	}
	
	/**
	 * Returns whether or not the player has a title
	 * @return	true if the player has a title, false otherwise
	 */
	public boolean hasTitle () {
		return hasTitle;
	}
	
	/**
	 * Returns the player's display name with attached titles, if any.
	 * @return	The name.
	 */
	public String getNameWithTitles () {
		return getName();
	}
	
	/**
	 * Returns the default name.
	 * @return The name.
	 */
	public String getName() {
		return StringUtils.format(displayName, FormatType.NAME);
	}
	
	/**
	 * Returns the name as a protocol string.
	 * @return The name.
	 */
	public String getNameAsProtocol() {
		return StringUtils.format(displayName, FormatType.PROTOCOL);
	}

	/**
	 * Returns the default account name.
	 * @return The account name.
	 */
	public String getAccountName() {
		return username;
	}
	
	/**
	 * Returns the name as a protocol string.
	 * @return The name.
	 */
	public String getAccountNameAsProtocol() {
		return StringUtils.format(username, FormatType.PROTOCOL);
	}
	
	/**
	 * Returns if the player has ever changed the name is history.
	 * @return True if so; false otherwise.
	 */
	public boolean hasChangedName() {
		return username != displayName;
	}
}
