package org.virtue.game.logic.vars;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class VarBitOverflowException extends Exception {
	
	private static final long serialVersionUID = 8968256997766476697L;

	public VarBitOverflowException () {
		
	}
	
	public VarBitOverflowException (String message) {
		super(message);
	}
	
	public VarBitOverflowException (Throwable cause) {
		super(cause);
	}
}
