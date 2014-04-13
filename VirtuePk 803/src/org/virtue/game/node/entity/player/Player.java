package org.virtue.game.node.entity.player;

import org.virtue.Constants;
import org.virtue.config.ClientVarps;
import org.virtue.config.OutgoingOpcodes;
import org.virtue.game.World;
import org.virtue.game.node.entity.Entity;
import org.virtue.game.node.entity.player.container.Equipment;
import org.virtue.game.node.entity.player.container.Inventory;
import org.virtue.game.node.entity.player.identity.Account;
import org.virtue.game.node.entity.player.screen.InterfaceManager;
import org.virtue.game.social.OnlineStatus;
import org.virtue.network.messages.ClientScriptVar;
import org.virtue.network.messages.EntityOptionMessage;
import org.virtue.network.messages.VarpMessage;
import org.virtue.network.protocol.packet.encoder.PacketDispatcher;
import org.virtue.network.protocol.packet.encoder.impl.PlayerEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.*;
import org.virtue.network.protocol.render.update.UpdateBlockArchive;
import org.virtue.utility.DisplayMode;

/**
 * @author Taylor
 * @date Jan 13, 2014
 */
public class Player extends Entity {
	
	public static final EntityOptionMessage OPTION_FOLLOW = new EntityOptionMessage("Follow", 2, false, -1);
	
	public static final EntityOptionMessage OPTION_TRADE = new EntityOptionMessage("Trade with", 4, false, -1);
	
	/**
	 * Represents this player's account.
	 */
	private Account account;
	
	/**
	 * Represents the viewport.
	 */
	private Viewport viewport;
	
	/**
	 * Represents the interface manager.
	 */
	private InterfaceManager interfaceManager;
	
	/**
	 * Represents the inventory.
	 */
	private Inventory inventory;
	
	/**
	 * Represents the equipment.
	 */
	private Equipment equipment;
	
	/**
	 * Represents the update archive.
	 */
	private UpdateBlockArchive updateArchive;
	
	/**
	 * Represents the packet dispatcher.
	 */
	private PacketDispatcher packetDispatcher;
	
	private boolean destroying = false;
	private boolean inWorld = false;
	
	/**
	 * Constructs a new {@code Player.java}.
	 * @param account The account
	 */
	public Player(Account account) {
		this.account = account;
		tile = Constants.DEFAULT_LOCATION;
		lastTile = getTile();
		viewport = new Viewport(this);
		interfaceManager = new InterfaceManager(this);
		inventory = new Inventory(this);
		equipment = new Equipment(this);
		updateArchive = new UpdateBlockArchive(this);
		packetDispatcher = new PacketDispatcher(this);
	}

	@Override
	public void start() {
		inWorld = true;
		//System.out.println("Sending game information to player...");
		packetDispatcher.dispatchMessage("Welcome to " + Constants.NAME + ".");
		packetDispatcher.dispatchPlayerOption(OPTION_FOLLOW);
		packetDispatcher.dispatchPlayerOption(OPTION_TRADE);
		int[] varps = ClientVarps.getGameVarps();
		for (int i = 0; i < varps.length; i++) {
			int val = varps[i];
			if (val != 0) {
				packetDispatcher.dispatchVarp(new VarpMessage(i, val));
			}
		}
		interfaceManager.sendScreen();
		account.getSession().getTransmitter().send(OnlineStatusEncoder.class, OnlineStatus.EVERYONE);
		account.getSession().getTransmitter().send(EmptyPacketEncoder.class, OutgoingOpcodes.UNLOCK_FRIENDS_LIST);
		packetDispatcher.dispatchRunEnergy(100);//Sends the current run energy level to the player
	}
	
	public void startLobby() {
		//started = true;
		account.getSession().getTransmitter().send(GameScreenEncoder.class, DisplayMode.LOBBY);
	}

	@Override
	public void destroy() {
		if (destroying) {
			return;
		}
		destroying = true;
		if (World.getWorld().contains(getAccount().getUsername().getAccountName())) {
			World.getWorld().removePlayer(this); 
		}		
	}

	@Override
	public void onCycle() {
	}

	public void sendLogout (boolean toLobby) {
		packetDispatcher.dispatchLogout(toLobby);
		destroy();
	}
	
	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
	
	@Override
	public void update() {
		if (inWorld) {
			account.getSession().getTransmitter().send(PlayerEncoder.class, this);//Send player updates
		}
	}
	
	@Override
	public void refreshOnDemand() {
		updateArchive.reset();//Refresh update flags
	}

	@Override
	public boolean exists() {
		return true;
	}
	
	public InterfaceManager getInterfaces () {
		return interfaceManager;
	}

	/**
	 * @return the viewport
	 */
	public Viewport getViewport() {
		return viewport;
	}

	/**
	 * @param viewport the viewport to set
	 */
	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	/**
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	/**
	 * @param equipment the equipment to set
	 */
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	/**
	 * @return the updateArchive
	 */
	public UpdateBlockArchive getUpdateArchive() {
		return updateArchive;
	}

	/**
	 * @param updateArchive the updateArchive to set
	 */
	public void setUpdateArchive(UpdateBlockArchive updateArchive) {
		this.updateArchive = updateArchive;
	}

	/**
	 * @return the packetDispatcher
	 */
	public PacketDispatcher getPacketDispatcher() {
		return packetDispatcher;
	}

	/**
	 * @param packetDispatcher the packetDispatcher to set
	 */
	public void setPacketDispatcher(PacketDispatcher packetDispatcher) {
		this.packetDispatcher = packetDispatcher;
	}
	
	/**
	 * Sends a packet to the client that opens a small dialogue box, promting to
	 * enter specified data. The {@code PendingEventManager} is then used to
	 * create a delayed event, while the client processes the input and sends it
	 * back to the server. Once packet arival, the event is fired and the input
	 * is returned. And future code in the specific block is not executed until
	 * the client and server finish the data process.
	 * @param dialogue The dialogue to display in the chatbox, promting the user toenter some sort of data.
	 * @return The input from the client.
	 */
	public String requestInput(String dialogue) {
		packetDispatcher.dispatchClientScriptVar(new ClientScriptVar(109, dialogue));
//		return (String) PendingEventManager.registerPendingEvent(new PendingEvent<String>() {
//
//			@Override
//			protected String execute() {
//				String input = getAccount().getFlag("input", "");
//				getAccount().removeFlag("input");
//				getAccount().removeFlag("recent_string_input");
//				return input;
//			}
//
//			@Override
//			protected boolean condition() {
//				return getAccount().getFlag("recent_string_input", false);
//			}
//		});
		return "ERROR: MISSING CODE - player.requestInput(...);";
	}
	
	/**
	 * Sends a packet to the client that opens a small dialogue box, prompting to
	 * enter specified data. The {@code PendingEventManager} is then used to
	 * create a delayed event, while the client processes the input and sends it
	 * back to the server. Once packet arrival, the event is fired and the input
	 * is returned. And future code in the specific block is not executed until
	 * the client and server finish the data process.
	 * @param dialogue The dialogue to display in the chatbox, prompting the user to enter some sort of data.
	 * @return The input from the client.
	 */
	public int requestIntegerInput(String dialogue) {
		packetDispatcher.dispatchClientScriptVar(new ClientScriptVar(108, dialogue));
//		return (int) PendingEventManager.registerPendingEvent(new PendingEvent<Integer>() {
//
//			@Override
//			protected Integer execute() {
//				int input = getAccount().getFlag("input", -1);
//				getAccount().removeFlag("input");
//				getAccount().removeFlag("recent_int_input");
//				return input;
//			}
//
//			@Override
//			protected boolean condition() {
//				return getAccount().getFlag("recent_int_input", false);
//
//			}
//		});
//	}
		return -1;
  }
}
