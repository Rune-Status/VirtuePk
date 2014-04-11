package org.virtue.game.region;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public interface SubRegion {

	/**
	 * Called to prepare to a region update. Used to initiate proper
	 * region update event sequence.
	 */
	void preUpdate();
	
	/**
	 * Called during the update process immediatley after {@link #preUpdate()}
	 * is called. Used to re apply and dispatch new region information and data.
	 */
	void update();
	
	/**
	 * Called to wrap up after an update process. The stages
	 * {@link #preUpdate()} and {@link #update()} will have been called by now,
	 * rendering this region fully updated. A post update cleans the region
	 * update sequnce of useless information.
	 */
	void postUpdate();
	
	/**
	 * Called to perform an attribute reset on a region. Simply refreshes
	 * temporary surface properties of a region.
	 */
	void refresh();
}
