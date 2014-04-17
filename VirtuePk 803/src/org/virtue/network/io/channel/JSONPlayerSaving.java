package org.virtue.network.io.channel;

import java.io.File;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.region.Tile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
			//skillSets.addProperty("level", skillData.getCurrentLevel());
			//skillSets.addProperty("experience", skillData.getExperience());
			skills.add(skillSets);
		}
		obj.add("skills", skills);
		
	}
	
	public void savePlayer(Player p) {
		
	}
	
	public Player loadPlayer(File p) {
		return null;
		//return new Player();
	}
	
}	