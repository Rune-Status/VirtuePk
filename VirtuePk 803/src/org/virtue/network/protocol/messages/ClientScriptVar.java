package org.virtue.network.protocol.messages;

/**
 * @author Taylor Moon
 * @since Jan 28, 2014
 */
public class ClientScriptVar {

	/**
	 * Represents the opcode of this client script.
	 */
	private int opcode;
	
	/**
	 * Represents the parameters of this client script.
	 */
	private Object[] parameters;
	
	/**
	 * Constructs a new {@code ClientScriptVar.java}.
	 * @param opcode The opcode of the script.
	 * @param parameters The parameters.
	 */
	public ClientScriptVar(int opcode, Object... parameters) {
		this.opcode = opcode;
		this.parameters = parameters;
	}

	/**
	 * @return the opcode
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * @param opcode the opcode to set
	 */
	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	/**
	 * Returns the variable string used to represents the data to be decoded in a specific order by the client.
	 * @return The variable string.
	 */
	public String getVariableString() {
		String vars = "";
		if (parameters == null) {
			return "";
		}
		for (int parameter = parameters.length - 1; parameter >= 0; parameter--) {
			if (parameters[parameter] instanceof String) {
				vars += "s";
			} else {
				vars += "i";
			}
		}
		return vars;
	}
}
