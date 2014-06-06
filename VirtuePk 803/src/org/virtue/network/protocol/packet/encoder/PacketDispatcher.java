package org.virtue.network.protocol.packet.encoder;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.content.skills.SkillData;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.Viewport;
import org.virtue.game.logic.node.entity.player.container.ItemsContainer;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.EntityOptionMessage;
import org.virtue.network.protocol.messages.GameMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.network.protocol.messages.GroundItemMessage;
import org.virtue.network.protocol.messages.GroundItemMessage.GroundItemType;
import org.virtue.network.protocol.messages.InterfaceMessage;
import org.virtue.network.protocol.messages.ItemsMessage;
import org.virtue.network.protocol.messages.ObjectMessage;
import org.virtue.network.protocol.messages.ObjectMessage.ObjectUpdateType;
import org.virtue.network.protocol.messages.VarcStringMessage;
import org.virtue.network.protocol.messages.VarMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.impl.ClientScriptVarEncoder;
import org.virtue.network.protocol.packet.encoder.impl.GameMessageEncoder;
import org.virtue.network.protocol.packet.encoder.impl.GroundItemEncoder;
import org.virtue.network.protocol.packet.encoder.impl.InterfaceEncoder;
import org.virtue.network.protocol.packet.encoder.impl.ItemsEncoder;
import org.virtue.network.protocol.packet.encoder.impl.LogoutEncoder;
import org.virtue.network.protocol.packet.encoder.impl.MapSceneEncoder;
import org.virtue.network.protocol.packet.encoder.impl.MinimapFlagEncoder;
import org.virtue.network.protocol.packet.encoder.impl.ObjectUpdateEncoder;
import org.virtue.network.protocol.packet.encoder.impl.PlayerOptionEncoder;
import org.virtue.network.protocol.packet.encoder.impl.SkillEncoder;
import org.virtue.network.protocol.packet.encoder.impl.VarcStringEncoder;
import org.virtue.network.protocol.packet.encoder.impl.VarEncoder;

/**
 * A class used for organization of outgoing packets so the transmitter doesn't
 * have to be called directly.
 * 
 * @author Jamie Moore
 * @since April, 13, 2014
 */
public class PacketDispatcher {
	
	/**
	 * Represents the player.
	 */
	private final Player player;
	
	/**
	 * Constructs a new {@code PacketDispatcher.java}.
	 * @param player The player.
	 */
	public PacketDispatcher(Player player) {
		this.player = player;
	}
	
	/**
	 * Dispatches the information for the specified skill to the player
	 * @param skill		The skill data to dispatch
	 */
	public void dispatchSkill (SkillData skill) {
		player.getAccount().getSession().getTransmitter().send(SkillEncoder.class, skill);
	}
	
	/**
	 * Dispatches the items for an interface
	 * @param key		The interface key
	 * @param items		The items
	 */
	public void dispatchItems (int key, ItemsContainer<Item> items, int... slots) {
		Item[] itemsClone = items.getItemsCopy();
		player.getAccount().getSession().getTransmitter().send(ItemsEncoder.class, new ItemsMessage(key, itemsClone, slots));
	}
	
	public void dispatchGroundItem (GroundItem item, GroundItemType type) {
		player.getAccount().getSession().getTransmitter().send(GroundItemEncoder.class, new GroundItemMessage(type, item, player.getViewport().getLastLoadedTile()));
	}
	
	public void dispatchObjectUpdate (RS3Object object, ObjectUpdateType type) {
		player.getAccount().getSession().getTransmitter().send(ObjectUpdateEncoder.class, new ObjectMessage(type, object, player.getViewport().getLastLoadedTile()));
	}
	
	/**
	 * Dispatches the current run energy level for the player
	 * @param energy	The run energy level (0 <= level <= 100)
	 */
	public void dispatchRunEnergy (float energy) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(OutgoingOpcodes.RUN_ENERGY_PACKET);
		buffer.put((int) energy);
		player.getAccount().getSession().getTransmitter().send(buffer);
	}
	
	/**
	 * Dispatches a default right-click option for all players
	 * @param option	The option being dispatched
	 */
	public void dispatchPlayerOption (EntityOptionMessage option) {
		player.getAccount().getSession().getTransmitter().send(PlayerOptionEncoder.class, option);
	}

	/**
	 * Dispatches a game message to the client. The game message is what is
	 * displayed in the chatbox, or developer console. The message can have
	 * multiple properties and settings that creates a certain type of
	 * style/effect, such as message type duel, or message type trade.
	 * @param message The message being dispatched.
	 */
	public void dispatchMessage(GameMessage message) {
		player.getAccount().getSession().getTransmitter().send(GameMessageEncoder.class, message);
	}
	
	/**
	 * Dispatches a game message to the client. The game message is what is
	 * displayed in the chatbox, or developer console. The message can have
	 * multiple properties and settings that creates a certain type of
	 * style/effect, such as message type duel, or message type trade.
	 * @param message The string of text being dispatched.
	 * @param opcode The operation code of the message format.
	 */
	public void dispatchMessage(String message, MessageOpcode opcode) {
		dispatchMessage(new GameMessage(opcode, message, player));
	}
	
	/**
	 * Dispatches a game message to the client. The game message is what is
	 * displayed in the chatbox, or developer console. The message can have
	 * multiple properties and settings that creates a certain type of
	 * style/effect, such as message type duel, or message type trade.
	 * @param message The string of text being dispatched.
	 */
	public void dispatchMessage(String message) {
		dispatchMessage(new GameMessage(MessageOpcode.CHAT_BOX, message, player));
	}
	
	/**
	 * Dispatches a request for the client to show an interface on the player's
	 * screen.
	 * @param message The interface message to dispatch.
	 */
	public void dispatchInterface(InterfaceMessage message) {
		player.getAccount().getSession().getTransmitter().send(InterfaceEncoder.class, message);
	}
	
	/**
	 * Dispatches a request to perform a safe logout via client operation. The
	 * client will know what to do from here.
	 * @param lobby If the logout is redirected to the lobby.
	 */
	public void dispatchLogout(boolean lobby) {
		player.getAccount().getSession().getTransmitter().send(LogoutEncoder.class, lobby);
	}
	
	/**
	 * Dispatches a scene update. A scene update will allow the client to
	 * re-render the current viewport and scene data, this is used for
	 * asynchronous updating if needed.
	 * @param viewport The viewport to be dispatched.
	 */
	public void dispatchSceneUpdate(Viewport viewport) {
		player.getAccount().getSession().getTransmitter().send(MapSceneEncoder.class, viewport);
	}
	
	/**
	 * Dispatches a scene update. A scene update will allow the client to
	 * re-render the current viewport and scene data, this is used for
	 * asynchronous updating if needed.
	 */
	public void dispatchSceneUpdate() {
		player.getAccount().getSession().getTransmitter().send(MapSceneEncoder.class, player.getViewport());
	}
	
	public void dispatchVarp(int id, int value) {
		dispatchVar(new VarMessage(id, value));
	}
	
	/**
	 * Dispatches a varp to the client. The varp may consist of many different
	 * properties, this is used for specific client events
	 * @param message The varp message to be dispatched.
	 */
	public void dispatchVar(VarMessage message) {
		player.getAccount().getSession().getTransmitter().send(VarEncoder.class, message);
	}
	
	public void dispatchVarcString(VarcStringMessage message) {
		player.getAccount().getSession().getTransmitter().send(VarcStringEncoder.class, message);
	}
	
	public void dispatchClientScriptVar(int scriptID, Object... params) {
		dispatchClientScriptVar(new ClientScriptVar(scriptID, params));
	}

	/**
	 * Dispatches a variable client script. A variable client script has a
	 * dynamic block that allows the client to process the script differently
	 * depending on the parameters. Client scripts have many different functions
	 * in the client.
	 * @param script The script to dispatch.
	 */
	public void dispatchClientScriptVar(ClientScriptVar script) {
		player.getAccount().getSession().getTransmitter().send(ClientScriptVarEncoder.class, script);
	}
	
	public void dispatchMinimapFlag (Tile location) {
		player.getAccount().getSession().getTransmitter().send(MinimapFlagEncoder.class, location);
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
