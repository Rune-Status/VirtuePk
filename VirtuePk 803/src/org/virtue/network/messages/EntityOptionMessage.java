package org.virtue.network.messages;

public class EntityOptionMessage {

	private String option;
	private int slot;
	private boolean top;
	private int cursor;
	
	/**
	 * Constructs a new EntityOptionMessage, used for setting the right-click options on players and NPCs
	 * @param option	The option string
	 * @param slot		The position ('slot') of the option
	 * @param top		Whether the option should appear at the top
	 * @param cursor	The cursor used when the option is hovered over
	 */
	public EntityOptionMessage (String option, int slot, boolean top, int cursor) {
		this.option = option;
		this.slot = slot;
		this.top = top;
		this.cursor = cursor;
	}
	
	/**
	 * Gets the option string
	 * @return	The option string.
	 */
	public String getOption () {
		return option;
	}
	
	/**
	 * Gets the option slot
	 * @return	The option slot.
	 */
	public int getSlot () {
		return slot;
	}
	
	/**
	 * Returns whether the option should appear at the top
	 * @return	True if it should, false otherwise
	 */
	public boolean isTop() {
		return top;
	}
	
	/**
	 * Gets the option cursor
	 * @return	The cursor sprite ID
	 */
	public int getCursor () {
		return cursor;
	}
}
