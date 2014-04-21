package org.virtue.network.io;

import java.io.FileNotFoundException;


/**
 * @author Taylor
 * @version 1.0
 */
public interface IOParser<T extends Object> {

	/**
	 * Called when the file should be parsed.
	 * @param buffer The buffer used to parse the file data
	 * @return The object.
	 * @throws FileNotFoundException 
	 */
	T load(Object... params) throws FileNotFoundException;
	
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
