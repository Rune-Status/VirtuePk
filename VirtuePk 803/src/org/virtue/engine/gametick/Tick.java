package org.virtue.engine.gametick;

/**
 * @author Taylor Moon
 * @since Jan 23, 2014
 */
public abstract class Tick {

	/**
	 * Represents the generic tick state to be called instead of the original
	 * until further removal.
	 */
	private TickState genericTickState; 
	
	/**
	 * Called when the game cycle has been executed.
	 * @return The state of the tick once a cycle is completed.
	 */
	public abstract TickState onTick();
	
	/**
	 * Checks if this tick has as generic state.
 	 * @return True if so; false otherwise.
	 */
	public final boolean hasGenericTickState() {
		return genericTickState == null;
	}
	
	/**
	 * @return the genericTickState
	 */
	public final TickState getGenericTickState() {
		return genericTickState;
	}

	/**
	 * @param genericTickState the genericTickState to set
	 */
	public final void setGenericTickState(TickState genericTickState) {
		this.genericTickState = genericTickState;
	}
}
