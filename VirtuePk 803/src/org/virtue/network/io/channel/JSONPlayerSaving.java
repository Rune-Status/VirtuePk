package org.virtue.network.io.channel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jboss.netty.channel.Channel;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Age;
import org.virtue.game.logic.node.entity.player.identity.DateOfBirth;
import org.virtue.game.logic.node.entity.player.identity.Email;
import org.virtue.game.logic.node.entity.player.identity.Password;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.game.logic.node.entity.region.Tile;
import org.virtue.utility.DisplayMode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSONPlayerSaving {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Account account = new Account(new Username("kyle"), new Password("kylep", false), Rank.ADMINISTRATOR, new Email("kyle@kyle.com"), new Age(27), new DateOfBirth("1100"), null, null, null, 0L, 0L);
		Player p = new Player(account);
		JSONPlayerSaving save = new JSONPlayerSaving();
		save.createPlayer(p);
	}
	
	public void createPlayer(Player p) throws IOException {
		JsonObject obj = new JsonObject();
		
		obj.addProperty("username", p.getAccount().getUsername().getAccountName());
		obj.addProperty("password", p.getAccount().getPassword().getPassword());
		obj.addProperty("email", p.getAccount().getEmail().getEmail());
		obj.addProperty("age", p.getAccount().getAge().getAge());
		obj.addProperty("dateofbirth", p.getAccount().getDateOfBirth().getDateOfBirth());
		obj.addProperty("rank", p.getAccount().getRank().name());
		
		JsonArray location = new JsonArray();
		JsonObject coords = new JsonObject();
		coords.addProperty("x", p.getLastTile().getX());
		coords.addProperty("y", p.getLastTile().getY());
		coords.addProperty("z", p.getLastTile().getPlane());
		location.add(coords);
		obj.add("location", location);
		
		/*JsonArray skills = new JsonArray();
		JsonObject skillSets = new JsonObject();
		for (int id = 0; id < 28; id++) {
			skillSets.addProperty("skillID", id);
			skillSets.addProperty("level", 1);
			skillSets.addProperty("experience", 0);
			skills.add(skillSets);
		}*/
		obj.add("skills", p.getSkillManager().serialise());
		
		File file = new File("./data/characters/"+p.getAccount().getUsername().getAccountName()+".json");
		FileWriter writer = new FileWriter(file);
		writer.write(obj.toString());
		writer.close();
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
		String permission = obj.get("rank").getAsString();
		
		Rank rank = null;
		switch (permission.toUpperCase()) {
		case "ADMINISTRATOR":
			rank = Rank.ADMINISTRATOR;
			break;
		case "MODERATOR":
			rank = Rank.MODERATOR;
			break;
		case "PLAYER":
			rank = Rank.PLAYER;
			break;
		}
		
		JsonArray arr = obj.get("location").getAsJsonArray();
		int x=0, y=0, z=0;
		for (JsonElement e : arr) {
			 x = obj.get("x").getAsInt();
			 y = obj.get("y").getAsInt();
			 z = obj.get("z").getAsInt();
		}
		Account account = new Account(new Username(username), new Password(password, false), rank, new Email(email), new Age(age), new DateOfBirth(dateofbirth), new Tile(x, y, z), channel, mode, clientSessionKey, serverSessionKey);
		return account;
	}
	
}	