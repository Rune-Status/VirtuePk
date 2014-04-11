package org.virtue.game.item;

/**
 * @author Taylor
 * @date Jan 21, 2014
 */
public class Item {

	/**
	 * Represents the ID.
	 */
	private int id;
	
	/**
	 * Represents the amount.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@code Item.java}.
	 * @param id The id.
	 * @param amount The amount.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public int getEquipId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
