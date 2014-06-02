package org.virtue.game.logic.node.entity.player;

import java.util.EnumMap;

import org.virtue.Constants;
import org.virtue.game.config.ClientVarps;
import org.virtue.game.logic.World;
import org.virtue.game.logic.content.combat.ability.AbilityBook;
import org.virtue.game.logic.content.combat.ability.ActionBar;
import org.virtue.game.logic.content.skills.SkillManager;
import org.virtue.game.logic.events.CoordinateEvent;
import org.virtue.game.logic.events.InputEnteredEvent;
import org.virtue.game.logic.events.PlayerActionEvent;
import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.interfaces.InterfaceManager;
import org.virtue.game.logic.node.interfaces.impl.Bank;
import org.virtue.game.logic.node.interfaces.impl.Equipment;
import org.virtue.game.logic.node.interfaces.impl.Inventory;
import org.virtue.game.logic.region.Tile;
import org.virtue.game.logic.social.ChatManager;
import org.virtue.network.io.IOHub;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.EntityOptionMessage;
import org.virtue.network.protocol.messages.VarMessage;
import org.virtue.network.protocol.packet.encoder.PacketDispatcher;
import org.virtue.network.protocol.packet.encoder.impl.GameScreenEncoder;
import org.virtue.network.protocol.packet.encoder.impl.MapSceneEncoder;
import org.virtue.network.protocol.packet.encoder.impl.NPCEncoder;
import org.virtue.network.protocol.packet.encoder.impl.PlayerEncoder;
import org.virtue.utility.DisplayMode;

/**
 * @author Taylor
 * @date Jan 13, 2014
 */
public class Player extends Entity {
	
	private EnumMap<PlayerOption, EntityOptionMessage> playerOptions = new EnumMap<PlayerOption, EntityOptionMessage>(PlayerOption.class);
	
	public enum PlayerStatus {LOGGED_OUT, WORLD, LOBBY, SWITCHING_WORLD}
	
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
	 * Represents the skill manager.
	 */
	private SkillManager skillManager;
	
	/**
	 * Represents the packet dispatcher.
	 */
	private PacketDispatcher packetDispatcher;
	
	/**
	 * Represents the chat manager
	 */
	private ChatManager chatManager;
	
	private int[] clientVarps = ClientVarps.getGameVarps().clone();
	
	private boolean largeSceneView = false;
	
	/**
	 * Represents the event to run when the player reaches a specific location
	 */
	private CoordinateEvent coordinateEvent;
	
	/**
	 * Represents the event to run when the player fills in an input form
	 */
	private InputEnteredEvent inputEvent;
	
	/**
	 * Represents whether the player exists or not (ie whether they are logged in)
	 */
	private boolean exists = true;
	
	private PlayerStatus status = PlayerStatus.SWITCHING_WORLD;
	
	/**
	 * Represents the player's current run energy level
	 */
	private float runEnergy = 100;
	
	/**
	 * Represents whether the player is resting or not
	 */
	private boolean resting;

	/**
	 * The player's ability book.
	 */
	public AbilityBook abilityBook;
	
	/**
	 * The player's action bar.
	 */
	public ActionBar actionBar;
	
	/**
	 * The player's bank
	 */
	private Bank bank;
	
	/**
	 * The current action the player is peforming
	 */
	private PlayerActionEvent currentAction;
	
	private int lockedFor = 0;
	
	/**
	 * Constructs a new {@code Player.java}.
	 * @param account The account
	 */
	public Player(Account account) {
		super();
		//System.out.println("Creating player: "+account.getUsername().getName());
		this.account = account;
		tile = new Tile(account.getTile());
		lastTile = new Tile(getTile());
		lastLoadedRegion = new Tile(lastTile);
		//System.out.println("Stage 2");
		viewport = new Viewport(this);
		interfaceManager = new InterfaceManager(this, account.getClientScreen());
		inventory = new Inventory(this);
		//System.out.println("Stage 3");
		equipment = new Equipment(this);
		bank = new Bank(this);
		abilityBook = new AbilityBook(this);
		actionBar = new ActionBar(this);
		//System.out.println("Stage 4");
		packetDispatcher = new PacketDispatcher(this);
		skillManager = new SkillManager(this);
		//System.out.println("Stage 5");
		chatManager = new ChatManager(this);
		//System.out.println("Stage 6");
	}
	
	public void setSwitching () {
		status = PlayerStatus.SWITCHING_WORLD;
	}

	@Override
	public void start() {		
		status = PlayerStatus.WORLD;
		
		if (IOHub.getAccountIo().exists(account.getUsername().getAccountName())) {
			skillManager.deserialise(account.getCharFile().get("skills").getAsJsonArray());
			inventory.deserialise(account.getCharFile().get("inventory").getAsJsonArray());
			equipment.deserialise(account.getCharFile().get("equipment").getAsJsonArray());
			chatManager.deserialiseData(account.getCharFile().get("chatData").getAsJsonObject());
			if (account.getCharFile().get("bank") != null) {
				bank.deserialise(account.getCharFile().get("bank").getAsJsonArray());
			}
			if (account.getCharFile().get("runEnergy") != null) {
				setRunEnergy(account.getCharFile().get("runEnergy").getAsInt());
				getUpdateArchive().getMovement().setRunning(account.getCharFile().get("isRunning").getAsBoolean());
			}
		}
		
		this.clientVarps[463] = resting ? 3 : getUpdateArchive().getMovement().isRunning() ? 1 : 0;
		getUpdateArchive().getAppearance().packBlock();
		//System.out.println("Sending game information to player...");
		packetDispatcher.dispatchMessage("Welcome to " + Constants.NAME + ".");
		int[] varps = clientVarps;
		for (int i = 0; i < varps.length; i++) {
			int val = varps[i];
			if (val != 0) {
				packetDispatcher.dispatchVar(new VarMessage(i, val));
			}
		}
		interfaceManager.sendScreen();
		int[] varps2 = ClientVarps.getGameVarps2();
		for (int i = 0; i < varps2.length; i++) {
			int val = varps2[i];
			if (val != 0) {
				packetDispatcher.dispatchVar(new VarMessage(i, val));
			}
		}
		inventory.load();
		equipment.load();
			
		skillManager.sendAllSkills();
		packetDispatcher.dispatchRunEnergy(runEnergy);//Sends the current run energy level to the player
		//sendRunButtonConfig();
		chatManager.init(false);
		World.getWorld().getRegionManager().getRegionByID(getTile().getRegionID()).addPlayer(this);
		//getPacketDispatcher().dispatchInterface(new InterfaceMessage(1252, 65, 1477, true));//Treasure hunter pop-up thing
	}
	
	public void sendDefaultPlayerOptions () {
		EntityOptionMessage followOption = new EntityOptionMessage("Follow", 2, false, -1);
		playerOptions.put(PlayerOption.TWO, followOption);
		packetDispatcher.dispatchPlayerOption(followOption);
		
		EntityOptionMessage tradeOption = new EntityOptionMessage("Trade with", 4, false, -1);
		playerOptions.put(PlayerOption.FOUR, tradeOption);
		packetDispatcher.dispatchPlayerOption(tradeOption);
	}
	
	public boolean hasLargeSceneView () {
		return largeSceneView;
	}
	
	public void startLobby() {
		status = PlayerStatus.LOBBY;
		account.getSession().getTransmitter().send(GameScreenEncoder.class, DisplayMode.LOBBY);
		if (IOHub.getAccountIo().exists(account.getUsername().getAccountName())) {
			chatManager.deserialiseData(account.getCharFile().get("chatData").getAsJsonObject());
		}
		chatManager.init(true);
	}

	@Override
	public void destroy() {
		if (!exists) {
			return;
		}
		exists = false;
		if (World.getWorld().contains(getAccount().getUsername().getAccountName())) {
			World.getWorld().removePlayer(this);
			if (!viewport.isSendGPI()) {
				World.getWorld().getRegionManager().getRegionByID(getTile().getRegionID()).getPlayers().remove(this);
			}
			IOHub.getAccountIo().save(this);
			IOHub.getInterfaceIO().save(this.getAccount().getUsername().getAccountNameAsProtocol(), interfaceManager.getScreen().getLayout());
		}		
		chatManager.disconnect();
		status = PlayerStatus.LOGGED_OUT;
	}

	@Override
	public void onCycle() {
		if (runEnergy == 0) {
			getUpdateArchive().getMovement().setRunning(false);
			sendRunButtonConfig();
		}
		if (lockedFor <= 0) {
			if (!viewport.isSendGPI()) {
				getUpdateArchive().getMovement().process();
			}		
			restoreRunEnergy();
			if (coordinateEvent != null && coordinateEvent.processEvent(this)) {
				coordinateEvent = null;
			}
			if (currentAction != null && currentAction.process(this)) {
				clearActionEvent();
			}
		} else {
			lockedFor--;
		}
		//System.out.println("Run direction: "+getUpdateArchive().getMovement().getNextRunDirection());
	}
	
	@Override
	public void loadMapRegion () {
		super.loadMapRegion();
		getAccount().getSession().getTransmitter().send(MapSceneEncoder.class, viewport);
	}

	public void sendLogout (boolean toLobby) {
		packetDispatcher.dispatchLogout(toLobby);
		destroy();
	}

	public void setCoordinateEvent(CoordinateEvent coordinateEvent) {
		this.coordinateEvent = coordinateEvent;
	}
	
	public void setInputEvent (InputEnteredEvent event) {
		this.inputEvent = event;
	}
	
	public InputEnteredEvent getInputEvent () {
		return inputEvent;
	}
	
	public boolean setActionEvent (PlayerActionEvent event) {
		if (!event.start(this)) {
			return false;
		}
		this.currentAction = event;
		return true;
	}
	
	public void clearActionEvent () {
		if (currentAction == null) {
			return;
		}
		currentAction.stop(this);
		this.currentAction = null;
	}
	
	public void stopAll () {
		clearActionEvent();
		interfaceManager.closeTopInterface();
		coordinateEvent = null;
	}
	
	public void teleport (Tile target) {
		getUpdateArchive().getMovement().teleport(target);
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
		if (status.equals(PlayerStatus.WORLD)) {
			account.getSession().getTransmitter().send(PlayerEncoder.class, this);//Send player updates
			account.getSession().getTransmitter().send(NPCEncoder.class, this);//Send NPC updates
		}
	}
	
	@Override
	public void refreshOnDemand() {
		getUpdateArchive().reset();//Refresh update flags
		getUpdateArchive().getMovement().setNeedsTypeUpdate(false);
		getUpdateArchive().getMovement().setTeleported(false);
	}

	@Override
	public boolean exists() {
		return exists;
	}
	
	public boolean isInWorld () {
		return status.equals(PlayerStatus.WORLD);
	}
	
	public int getRunEnergy () {
		return (int) runEnergy;
	}
	
	public void setRunEnergy (float energy) {
		if (energy < 0) {
			energy = 0;
		} else if (energy > 100) {
			energy = 100;
		}
		this.runEnergy = energy;
		packetDispatcher.dispatchRunEnergy(energy);
	}
	
	public void toggleRun () {
		getUpdateArchive().getMovement().swapRunning();
		sendRunButtonConfig();
	}
	
	public void sendRunButtonConfig() {
		packetDispatcher.dispatchVar(new VarMessage(463, resting ? 3 : getUpdateArchive().getMovement().isRunning() ? 1 : 0));
	}
	
	public void drainRunEnergy () {
		setRunEnergy(runEnergy-1);		
	}

	public void restoreRunEnergy() {		
		if (getUpdateArchive().getMovement().getNextRunDirection() == -1 && runEnergy < 100) {
			float increase = 0;
			increase = 0.5f;
			if (resting && runEnergy < 100) {
				increase += 0.5f;
			}
			setRunEnergy(runEnergy+increase);
			//packetDispatcher.dispatchRunEnergy(runEnergy);
		}
	}
	
	/**
	 * @return	The interface manager for the player
	 */
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
	 * @return the bank
	 */
	public Bank getBank () {
		return bank;
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
	 * @return	The chat manager
	 */
	public ChatManager getChatManager () {
		return chatManager;
	}
	
	public SkillManager getSkills () {
		return skillManager;
	}
	
	public ActionBar getActionBar () {
		return actionBar;
	}
	
	public void lock (int ticks) {
		lockedFor = ticks;
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

	@Override
	public int getSize() {
		return 1;
	}
}
