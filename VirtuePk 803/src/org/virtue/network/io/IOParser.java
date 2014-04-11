package org.virtue.network.io;


/**
 * @author Taylor
 * @version 1.0
 */
public interface IOParser<T extends Object, F extends Object> {

	/**
	 * Called when the file should be parsed.
	 * @param buffer The buffer used to parse the file data
	 * @return The object.
	 */
	T load(F buffer);
	
	/**
	 * Called when the file should be saved.
	 * @return A future representation on the successfulness of the method.
	 */
	boolean save(Object... params);
	
	/**
	 * Returns the path of the file being parsed.
	 * @return The path.
	 */
	String getPath();
}
