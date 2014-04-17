package org.virtue.network.io.channel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;

import org.jboss.netty.channel.Channel;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.game.logic.node.entity.player.identity.Password;
import org.virtue.game.logic.node.entity.player.identity.Email;
import org.virtue.game.logic.node.entity.player.identity.Age;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.entity.region.Tile;
import org.virtue.utility.DisplayMode;

public class JSONPlayerSaving {

	public Tile tile;
	
	public void createPlayer(Player p) {
		JsonObject obj = new JsonObject();
		
		obj.addProperty("username", p.getAccount().getUsername().getAccountName());
		obj.addProperty("password", p.getAccount().getPassword().getPassword());
		obj.addProperty("email", p.getAccount().getEmail().getEmail());
		obj.addProperty("age", p.getAccount().getAge().getAge());
		//obj.addProperty("dateofbirth", p.getAccount().getDateOfBirth().getDateOfBirth());
		obj.addProperty("rank", p.getAccount().getRank().name());
		
		JsonArray location = new JsonArray();
		JsonObject coords = new JsonObject();
		coords.addProperty("x", tile.getX());
		coords.addProperty("y", tile.getY());
		coords.addProperty("z", tile.getPlane());
		location.add(coords);
		obj.add("location", location);
		
		JsonArray skills = new JsonArray();
		JsonObject skillSets = new JsonObject();
		int skillIds = 28;
		for (int id = 0; id < skillIds; id++) {
			skillSets.addProperty("skillID", id);
			skillSets.addProperty("level", 1);
			skillSets.addProperty("experience", 0);
			skills.add(skillSets);
		}
		obj.add("skills", skills);
		
	}
	
	public void savePlayer(Player p) {
		
	}
	
	public Account loadPlayer(File path, Channel channel, DisplayMode mode, long clientSessionKey, long serverSessionKey) {
		JsonObject obj = new JsonObject();
		String username = obj.get("username").getAsString();
		String password = obj.get("password").getAsString();
		String email = obj.get("email").getAsString();
		int age = obj.get("age").getAsInt();
		String dateofbirth = obj.get("dateofbirth").getAsString();
		String rank = obj.get("rank").getAsString();
		
		JsonArray arr = obj.get("location").getAsJsonArray();
		int x=0, y=0, z=0;
		for (JsonElement e : arr) {
			 x = obj.get("x").getAsInt();
			 y = obj.get("y").getAsInt();
			 z = obj.get("z").getAsInt();
		}
		Account account = new Account(new Username(username), new Password(password, false), Rank.ADMINISTRATOR, new Email(email), new Age(age), new Tile(x, y, z), channel, mode, clientSessionKey, serverSessionKey);
		return account;
	}
	
}	