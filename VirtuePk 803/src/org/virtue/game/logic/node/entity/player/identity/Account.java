package org.virtue.game.logic.node.entity.player.identity;

import org.jboss.netty.channel.Channel;
import org.virtue.Constants;
import org.virtue.game.core.AttributeSet;
import org.virtue.game.logic.node.entity.player.screen.ClientScreen;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.session.Session;
import org.virtue.utility.DisplayMode;

import com.google.gson.JsonObject;

/**
 * @author Taylor
 * @date Jan 15, 2014
 */
public class Account extends AttributeSet {
	
	/**
	 * Represents this account's username.
	 */
	private final Username username;
	
	/**
	 * Represents this account's password.
	 */
	private final Password password;
	
	/**
	 * Represents the rank.
	 */
	private Rank rank = Rank.ADMINISTRATOR;
	
	/**
	 * Represents the bound channel on this account.
	 */
	private Channel channel;
	
	/**
	 * Represents the bound session.
	 */
	private Session session;
	
	/**
	 * Represents the display mode.
	 */
	private ClientScreen screen;
	
	/**
	 * Represents the key for the ongoing client session.
	 */
	private long clientSessionKey;
	
	/**
	 * Represents the key for the ongoing server session.
	 */
	private long serverSessionKey;
	
	private Email email;
	
	private Age age;
	
	private DateOfBirth dateofbirth;
	
	/**
	 * Represents the default tile for the player
	 */
	private Tile tile = new Tile(Constants.DEFAULT_LOCATION);
	
	private JsonObject charFile;
	
	/**
	 * Constructs a new {@code Account.java}.
	 * @param username The username.
	 * @param password The password.
	 */
	public Account(Username username, Password password, Channel channel, ClientScreen screen, long clientSessionKey, long serverSessionKey) {
		System.out.println("Creating new account for user: "+username.getName());
		this.username = username;
		this.password = password;
		this.channel = channel;
		this.screen = screen;
		this.clientSessionKey = clientSessionKey;
		this.serverSessionKey = serverSessionKey;
	}
	
	/**
	 * Constructs a new {@code Account.java}.
	 * @param username The username.
	 * @param password The password.
	 * @param obj 
	 * @param dateOfBirth2 
	 */
	public Account(Username username, Password password, Rank rank, Email email, Age age, DateOfBirth dateofbirth, Tile tile, ClientScreen screen, JsonObject charFile) {
		//System.out.println("Creating new account for user: "+username.getName());
		this.username = username;
		this.password = password;
		this.rank = rank;
		this.email = email;
		this.age = age;
		this.dateofbirth = dateofbirth;
		this.tile = tile;
		this.channel = null;
		this.screen = screen;
		this.clientSessionKey = 0L;
		this.serverSessionKey = 0L;
		this.charFile = charFile;
	}

	/**
	 * @return the username
	 */
	public Username getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public Password getPassword() {
		return password;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * @return the displayMode
	 */
	public ClientScreen getClientScreen() {
		return screen;
	}

	/**
	 * @param displayMode the displayMode to set
	 */
	/*public void setScreenConfig(ClientScreen screen) {
		this.screen = screen;
	}*/

	/**
	 * @return the clientSessionKey
	 */
	public long getClientSessionKey() {
		return clientSessionKey;
	}

	public void setClientSessionKey(long clientSessionKey) {
		this.clientSessionKey = clientSessionKey;
	}
	
	/**
	 * @return the serverSessionKey
	 */
	public long getServerSessionKey() {
		return serverSessionKey;
	}
	
	public void setServerSessionKey(long serverSessionKey) {
		this.serverSessionKey = serverSessionKey;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * @return the rank
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public Email getEmail() {
		return email;
	}
	
	public Age getAge() {
		return age;
	}
	
	public DateOfBirth getDateOfBirth() {
		return dateofbirth;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public JsonObject getCharFile() {
		return charFile;
	}
	
}
