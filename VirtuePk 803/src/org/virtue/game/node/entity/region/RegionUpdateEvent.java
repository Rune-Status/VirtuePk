package org.virtue.game.node.entity.region;


/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class RegionUpdateEvent {
	
	/**
	 * Represents the region being updated.
	 */
	private final Region region;
	
	/**
	 * Represents the event being ran after the update is complete.
	 */
	private final Runnable event;
	
	/**
	 * Constructs a new {@code RegionUpdateEvent.java}.
	 * @param region The region being updated.
	 * @param event The event being ran after the update is complete.
	 */
	public RegionUpdateEvent(Region region, Runnable event) {
		this.region = region;
		this.event = event;
	}

	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * @return the event
	 */
	public Runnable getEvent() {
		return event;
	}
}
