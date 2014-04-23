package org.virtue.game.logic.social;

import org.virtue.game.logic.social.internal.InternalFriendManager;
import org.virtue.game.logic.social.internal.SocialUser;
import org.virtue.Launcher;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.messages.PublicMessage;
import org.virtue.network.protocol.packet.encoder.impl.EmptyPacketEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.OnlineStatusEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.PublicMessageEncoder;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class ChatManager {
	
	/**
	 * Represents the player.
	 */
	private Player player;
	
	/**
	 * Represents the type of message that the next message(s) will be
	 */
	private ChatType chatType;
	
	/**
	 * Represents the current online status of the player
	 */
	private OnlineStatus onlineStatus = OnlineStatus.EVERYONE;
	
	private final FriendManager friendManager;
	
	private static FriendsChatManager friendsChatManager;
	
	private String currentFriendsChat = null;
	
	/**
	 * Constructs a new {@code ChatManager} instance for the specified player
	 * @param player	The player
	 */
	public ChatManager (Player player) {
		this.player = player;
		friendManager = new InternalFriendManager(new SocialUser(player));
	}
	
	/**
	 * Sets the type for the player's chat messages
	 * @param typeCode	The code representing the type
	 */
	public void setChatType (int typeCode) {
		setChatType(ChatType.forCode(typeCode));
	}
	
	/**
	 * Sets the type for the player's chat messages
	 * @param typeCode	The chat type
	 */
	public void setChatType (ChatType type) {
		chatType = type;
	}
	
	/**
	 * Gets the manager for this player's friends and ignores
	 * @return	The friend manager
	 */
	public FriendManager getFriendManager () {
		return friendManager;
	}
	
	/**
	 * Gets the type for the player's chat messages
	 * @return
	 */
	public ChatType getChatType () {
		return chatType;
	}
	
	public void init (boolean lobby) {
		player.getAccount().getSession().getTransmitter().send(OnlineStatusEncoder.class, onlineStatus);
		player.getAccount().getSession().getTransmitter().send(EmptyPacketEncoder.class, OutgoingOpcodes.UNLOCK_FRIENDS_LIST);
		friendManager.init();
	}
	
	/**
	 * Handles the specified public chat message
	 * @param message		The message
	 * @param colourEffect	The colour effect
	 * @param moveEffect	The movement effect
	 */
	public void handlePublicMessage (String message, int colourEffect, int moveEffect) {
		System.out.println("Received message: message="+message+", type="+chatType+", colour="+colourEffect+", move="+moveEffect);
		String formattedMessage = StringUtils.format(message, FormatType.DISPLAY);
		int effects = (colourEffect << 8) | (moveEffect & 0xff);
		
		PublicMessage msgObject = new PublicMessage(formattedMessage, effects, player.getIndex(), player.getAccount().getRank());
		
		for (Player p : World.getWorld().getPlayers()) {
			if (p == null || !p.exists() || p.getViewport().getLocalPlayers()[player.getIndex()] == null) {
				continue;
			}
			p.getAccount().getSession().getTransmitter().send(PublicMessageEncoder.class, msgObject);
		}
	}
	
	public void setOnlineStatus (OnlineStatus status) {
		this.onlineStatus = status;
		friendManager.setOnlineStatus(status);
	}
	
	public static byte[] generateMessageHash () {
		byte[] hash = new byte[5];
		Launcher.getRandom().nextBytes(hash);
		return hash;
	}
}
