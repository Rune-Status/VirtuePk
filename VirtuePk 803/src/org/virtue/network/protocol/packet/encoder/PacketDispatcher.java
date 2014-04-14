package org.virtue.network.protocol.packet.encoder;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.Viewport;
import org.virtue.game.node.entity.player.skills.Skill;
import org.virtue.game.node.entity.player.skills.SkillData;
import org.virtue.network.messages.ClientScriptVar;
import org.virtue.network.messages.EntityOptionMessage;
import org.virtue.network.messages.GameMessage;
import org.virtue.network.messages.InterfaceMessage;
import org.virtue.network.messages.VarpMessage;
import org.virtue.network.messages.GameMessage.MessageOpcode;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.impl.ClientScriptVarEncoder;
import org.virtue.network.protocol.packet.encoder.impl.GameMessageEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.InterfaceEncoder;
import org.virtue.network.protocol.packet.encoder.impl.LogoutEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.MapSceneEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.PlayerOptionEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.SkillEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.VarpEncoder;

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
	
	public void dispatchSkill (SkillData skill) {
		player.getAccount().getSession().getTransmitter().send(SkillEncoder.class, skill);
	}
	
	/**
	 * Dispatches the current run energy level for the player
	 * @param energy	The run energy level (0 <= level <= 100)
	 */
	public void dispatchRunEnergy (int energy) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(OutgoingOpcodes.RUN_ENERGY_PACKET);
		buffer.put(energy);
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
	
	/**
	 * Dispatches a varp to the client. The varp may consist of many different
	 * properties, this is used for specific client events
	 * @param message The varp message to be dispatched.
	 */
	public void dispatchVarp(VarpMessage message) {
		player.getAccount().getSession().getTransmitter().send(VarpEncoder.class, message);
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
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
