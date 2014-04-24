package org.virtue.network.io.channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.internal.InternalFriendManager;
import org.virtue.game.logic.social.internal.SocialUser;
import org.virtue.network.io.IOParser;

public class FriendsParser implements IOParser<InternalFriendManager> {

	public static final File SAVE_PATH = new File("data/friends/");
	
	@Override
	public InternalFriendManager load(Object... params) throws FileNotFoundException {
		System.out.println("Loading friends...");
		File friendData = new File(getPath(), params[0]+".bin");
		SocialUser player = (SocialUser)params[1];
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
		System.out.println("Saving friends");
		File friendData = new File(getPath(), params[0]+".bin");
		InternalFriendManager friendManager = (InternalFriendManager) params[1];
		
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