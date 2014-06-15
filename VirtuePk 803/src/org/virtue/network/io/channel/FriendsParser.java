package org.virtue.network.io.channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.virtue.game.logic.social.internal.InternalFriendManager;
import org.virtue.game.logic.social.internal.InternalSocialUser;
import org.virtue.network.io.IOParser;

public class FriendsParser implements IOParser<InternalFriendManager> {

	/**
	 * (non-Javadoc)
	 * @see org.virtue.network.io.IOParser#SAVE_PATH
	 */
	private File SAVE_PATH = new File("data/friends/");
	
	@Override
	public InternalFriendManager load(Object... params) throws FileNotFoundException {
		System.out.println("Loading friends...");
		File friendData = new File(getPath(), params[0]+".bin");
		InternalSocialUser player = (InternalSocialUser)params[1];
		InternalFriendManager friendManager = new InternalFriendManager(player);
		try (DataInputStream input = new DataInputStream(new FileInputStream(friendData))) {
			friendManager.deserialise(input);
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return friendManager;
	}

	@Override
	public boolean save(Object... params) {
		File friendData = new File(getPath(), params[0]+".bin");
		InternalFriendManager friendManager = (InternalFriendManager) params[1];
		if (!friendManager.hasStarted()) {
			System.err.println("Could not save friend data - Friend manager not loaded.");
			return false;
		}
		System.out.println("Saving friends");
		
		try (DataOutputStream output = new DataOutputStream(new FileOutputStream(friendData))) {
			friendManager.serialise(output);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public File getPath() {
		return SAVE_PATH;
	}
	
	public boolean exists(String name) {
		File file = new File(getPath(), name+".bin");
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

}
