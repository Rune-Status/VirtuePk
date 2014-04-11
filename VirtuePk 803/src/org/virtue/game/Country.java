package org.virtue.game;

/**
 * @author Taylor
 * @version 1.0
 */
public enum Country {
	
	/**
	 * Australia
	 */
	AUSTRALIA(16, "Australia"),

	/**
	 * Belgium
	 */
	BELGIUM(22, "Belgium"),

	/**
	 * Brazil
	 */
	BRAZIL(31, "Brazil"),

	/**
	 * Canada
	 */
	CANADA(38, "Canada"),

	/**
	 * Denmark
	 */
	DENMARK(58, "Denmark"),

	/**
	 * Finland
	 */
	FINLAND(69, "Finland"),

	/**
	 * Ireland
	 */
	IRELAND(101, "Ireland"),

	/**
	 * Mexico
	 */
	MEXICO(152, "Mexico"),

	/**
	 * Netherlands
	 */
	NETHERLANDS(161, "Netherlands"),

	/**
	 * Norway
	 */
	NORWAY(162, "Norway"),

	/**
	 * Sweden
	 */
	SWEDEN(191, "Sweden"),

	/**
	 * UK
	 */
	UK(77, "United Kingdom"),

	/**
	 * USA
	 */
	USA(225, "United States"),

	/**
	 * Flag highlight
	 */
	FLAG_HIGHLIGHT(16),

	/**
	 * Lootshare flag
	 */
	FLAG_LOOTSHARE(8),

	/**
	 * Members flag
	 */
	FLAG_MEMBERS(1),

	/**
	 * Non memebers flag
	 */
	FLAG_NON_MEMBERS(0),

	/**
	 * PVP flag
	 */
	FLAG_PVP(4);

	/**
	 * The ID of this country
	 */
	private final int ID;
	
	private final String name;

	/**
	 * Constructs a new country
	 * @param id The ID
	 */
	Country(int id) {
		this(id, null);
	}
	
	Country (int id, String name) {
		ID = id;
		this.name = name;
	}

	/**
	 * @return The iD
	 */
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
}
