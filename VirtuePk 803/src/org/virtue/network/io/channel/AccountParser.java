package org.virtue.network.io.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Age;
import org.virtue.game.logic.node.entity.player.identity.DateOfBirth;
import org.virtue.game.logic.node.entity.player.identity.Email;
import org.virtue.game.logic.node.entity.player.identity.Password;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.io.IOParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * @author Taylor
 * @version 1.0
 */
public class AccountParser implements IOParser<Account> {
	
	/*public static void main(String... args) {
		AccountParser parser = new AccountParser();
		if ((parser.save(new Player(new Account(new Username("kyle_username"), new Password("kyle_password", false), Rank.MODERATOR, new Email("kyle@runelove.com"), new Age(27), new DateOfBirth("1100"), new Tile(0, 0, 0), null)))) == false)
			System.out.println("Could not save player");
	}*/
	
	public static final File SAVE_PATH = new File("data/characters/");
	
	/**
	 * (non-Javadoc)
	 * @throws FileNotFoundException 
	 * @throws JsonSyntaxException 
	 * @throws JsonIOException 
	 * @see org.virtue.network.io.IOParser#load(java.lang.Object)
	 */
	@Override
	public Account load(Object... params) throws FileNotFoundException {
		File character = new File(getPath(), params[0]+".json");

		JsonParser parser = new JsonParser();
		Object parsed = parser.parse(new FileReader(character));
		
		JsonObject obj = (JsonObject) parsed;
		
		String username = obj.get("username").getAsString();
		String password = obj.get("password").getAsString();
		String email = obj.get("email").getAsString();
		int age = obj.get("age").getAsInt();
		String dateofbirth = obj.get("dateofbirth").getAsString();
		int rankID = obj.get("rank").getAsInt();
		
		Rank rank = Rank.forID(rankID);
		if (rank == null) {
			rank = Rank.PLAYER;
		}
		/*switch (permission.toUpperCase()) {
		case "ADMINISTRATOR":
			rank = Rank.ADMINISTRATOR;
			break;
		case "MODERATOR":
			rank = Rank.MODERATOR;
			break;
		case "PLAYER":
			rank = Rank.PLAYER;
			break;
		}*/
		
		JsonArray loc = obj.get("location").getAsJsonArray();
		int x=0, y=0, z=0;
		for (JsonElement e : loc) {
			JsonObject coords = e.getAsJsonObject();
			 x = coords.get("x").getAsInt();
			 y = coords.get("y").getAsInt();
			 z = coords.get("z").getAsInt();
		}
		
		//Player player = new Player(new Account(new Username(username), new Password(password, false), rank, new Email(email), new Age(age), new DateOfBirth(dateofbirth), new Tile(x, y, z)));
		
		//player.getSkillManager().deserialise(obj.get("skills").getAsJsonArray());
		
		return new Account(new Username(username), new Password(password, false), rank, new Email(email), new Age(age), new DateOfBirth(dateofbirth), new Tile(x, y, z), obj);
	}

	/**
	 * (non-Javadoc)
	 * @see org.virtue.network.io.IOParser#save()
	 */
	@Override
	public boolean save(Object... params) {
		JsonObject obj = new JsonObject();
		
		Player p = (Player) params[0];
		
		obj.addProperty("username", p.getAccount().getUsername().getAccountName());
		obj.addProperty("password", p.getAccount().getPassword().getPassword());
		obj.addProperty("email", (p.getAccount().getEmail() == null ? "null" : p.getAccount().getEmail().getEmail()));
		obj.addProperty("age", (p.getAccount().getAge() == null ? -1 : p.getAccount().getAge().getAge()));
		obj.addProperty("dateofbirth", (p.getAccount().getDateOfBirth() == null ? "null" : p.getAccount().getDateOfBirth().getDateOfBirth()));
		obj.addProperty("rank", p.getAccount().getRank().getID());
		
		JsonArray location = new JsonArray();
		JsonObject coords = new JsonObject();
		coords.addProperty("x", p.getTile().getX());
		coords.addProperty("y", p.getTile().getY());
		coords.addProperty("z", p.getTile().getPlane());
		location.add(coords);
		obj.add("location", location);
		
		obj.add("skills", p.getSkillManager().serialise());
		
		obj.add("inventory", p.getInventory().serialise());
		
		obj.add("equipment", p.getEquipment().serialise());
		
		obj.add("chatData", p.getChatManager().serialiseData());
		
		System.out.println("Saving player...");
		
		File file = new File(getPath(), p.getAccount().getUsername().getAccountName()+".json");
		/*if (file.exists())
			file.delete();*/
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(obj.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.virtue.network.io.IOParser#getPath()
	 */
	@Override
	public File getPath() {
		return SAVE_PATH;
	}
	
	public boolean exists(String name) {
		File file = new File(getPath(), name+".json");
		if (file.exists())
			return true;
		else
			return false;
	}

}
