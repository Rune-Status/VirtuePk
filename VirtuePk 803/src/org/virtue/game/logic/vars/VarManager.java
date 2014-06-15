package org.virtue.game.logic.vars;

import java.io.IOException;

import org.virtue.Launcher;
import org.virtue.cache.def.CacheIndex;
import org.virtue.cache.def.VarBitType;
import org.virtue.cache.def.VarBitTypeLoader;
import org.virtue.game.config.Js5ConfigGroup;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.VarMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.impl.VarEncoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class VarManager {
	
	public static int VARP_COUNT;
	public static int VARC_COUNT;
	
	static {
		try {
			VARP_COUNT = Launcher.getCache().getContainerCount(CacheIndex.CONFIG, Js5ConfigGroup.VAR_PLAYER);
			VARC_COUNT = Launcher.getCache().getContainerCount(CacheIndex.CONFIG, Js5ConfigGroup.VAR_CLIENT);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private final Player player;
	
	private final int[] varps;
	
	private boolean ready = false;
	
	public VarManager (Player player) {
		this.player = player;
		this.varps = new int[VARP_COUNT];
	}
	
	public void clearVarps () {
		for (int i=0;i<varps.length;i++) {
			varps[i] = 0;
		}
	}
	
	public void setVarps (int[] varps) {
		for (int i=0;i<varps.length;i++) {
			if (varps[i] != 0) {
				setVarPlayer(i, varps[i]);
			}
		}
	}
	
	public JsonArray serialiseVarps () {
		JsonArray array = new JsonArray();
		for (int i=0;i<varps.length;i++) {
			if (varps[i] == 0) {
				continue;
			}
			JsonObject entry = new JsonObject();
			entry.addProperty("key", i);
			entry.addProperty("value", varps[i]);
			array.add(entry);
		}
		return array;
	}
	
	public void sendAllVarps () {
		RS3PacketBuilder packet = new RS3PacketBuilder();
		packet.putPacket(OutgoingOpcodes.RESET_VARP_CACHE);
		player.getAccount().getSession().getTransmitter().send(packet);
		ready = true;
		for (int i = 0; i < varps.length; i++) {
			if (varps[i] != 0) {
				sendVar(new VarMessage(VarDomainType.PLAYER, i, varps[i]));
			}
		}		
	}
	
	private void sendVar (VarMessage message) {
		if (!ready) {
			return;
		}
		player.getAccount().getSession().getTransmitter().send(VarEncoder.class, message);
	}
	
	/**
	 * Sets whether or not the var updates are ready to be sent to the player
	 * @param ready		True if the var updates can be sent to the player, false if they should only be updated internally
	 */
	public void setReady (boolean ready) {
		this.ready = ready;
	}

	/**
	 * Sets the varPlayer value for this player to the specified value. Also sends the new value to the player's client
	 * @param key		The varp key
	 * @param value		The value to set
	 * @throws IndexOutOfBoundsException	If the varp key is not valid
	 */
	
	public void setVarPlayer (int key, int value) throws IndexOutOfBoundsException {
		if (key >= VARP_COUNT || key < 0) {
			throw new IndexOutOfBoundsException("The varp key of "+key+" is invalid. Varp keys must be between 0 and "+VARP_COUNT);
		}
		varps[key] = value;
		if (ready) {
			sendVar(new VarMessage(VarDomainType.PLAYER, key, value));
		}
	}
	
	/**
	 * Returns the value currently assigned to the specified player var
	 * @param key	The varp key
	 * @return		The varp value
	 */
	public int getVarPlayer (int key) {
		return varps[key];
	}
	
	public void setVarBit (int key, int value) throws NullPointerException {
		VarBitType type = VarBitTypeLoader.forID(key);
		if (type == null) {
			throw new NullPointerException("Varbit "+key+" does not exist");
		}		
		if (type.getDomainType().equals(VarDomainType.PLAYER)) {
			varps[key] = type.setVarbitValue(varps[key], value);//TODO: Ensure that the value is within the valid range
		}
		sendVar(new VarMessage(type.getDomainType(), key, value, true));
	}
	
	public int getVarBit (int key) throws NullPointerException {
		VarBitType type = VarBitTypeLoader.forID(key);
		if (type == null) {
			throw new NullPointerException("Varbit "+key+" does not exist");
		}
		if (type.getDomainType().equals(VarDomainType.PLAYER)) {
			int fullValue = getVarPlayer(type.getBaseVarKey());
			return type.getVarbitValue(fullValue);
		} else {
			return -1;
		}
	}
	
	public void setVarClient (int key, Object value) {
		sendVar(new VarMessage(VarDomainType.CLIENT, key, value));
	}
}
