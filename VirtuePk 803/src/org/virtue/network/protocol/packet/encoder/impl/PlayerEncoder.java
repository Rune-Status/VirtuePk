package org.virtue.network.protocol.packet.encoder.impl;

import java.security.MessageDigest;
import java.util.Arrays;

import org.virtue.Constants;
import org.virtue.config.OutgoingOpcodes;
import org.virtue.config.UpdateMasks;
import org.virtue.game.World;
import org.virtue.game.node.entity.player.Player;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;
import org.virtue.network.protocol.render.update.blocks.AppearanceBlock;
import org.virtue.network.protocol.render.update.movement.MovementUtils;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class PlayerEncoder implements PacketEncoder<Player> {
	
	/**
	 * Represents the player.
	 */
	private Player player;

	@Override
	public RS3PacketBuilder buildPacket(Player node) {
		player = node;
		if (player.getViewport().isSendGPI()) {
			throw new RuntimeException("The global player initialisation must be sent first!");
			//player.getViewport().loadGlobalPlayers(new RS3PacketBuilder());
		}
		RS3PacketBuilder stream = new RS3PacketBuilder();
		RS3PacketBuilder updateBlockData = new RS3PacketBuilder();
		stream.putPacketVarShort(OutgoingOpcodes.PLAYER_UPDATE_PACKET);
		renderLocalPlayers(stream, updateBlockData, true);
		renderLocalPlayers(stream, updateBlockData, false);
		//System.out.println("After local players: index="+stream.getPosition()+", data="+Arrays.toString(stream.buffer()));
		renderOutsidePlayers(stream, updateBlockData, true);
		renderOutsidePlayers(stream, updateBlockData, false);
		//System.out.println("After global players: index="+stream.getPosition()+", data="+Arrays.toString(stream.buffer()));
		stream.put(updateBlockData.buffer(), 0, updateBlockData.getPosition());
		stream.endPacketVarShort();
		node.getViewport().setTotalRenderDataSentLength(0);
		node.getViewport().setLocalPlayersIndexesCount(0);
		node.getViewport().setOutPlayersIndexesCount(0);
		for (int playerIndex = 1; playerIndex < 2048; playerIndex++) {
			player.getViewport().getSlotFlags()[playerIndex] >>= 1;
			Player player = node.getViewport().getLocalPlayers()[playerIndex];
			if (player == null) {
				node.getViewport().setOutPlayersIndexesCount(node.getViewport().getOutPlayersIndexesCount() + 1);
				node.getViewport().getOutPlayersIndexes()[node.getViewport().getOutPlayersIndexesCount()] = playerIndex;
			} else {
				node.getViewport().setLocalPlayersIndexesCount(node.getViewport().getLocalPlayersIndexesCount() + 1);
				node.getViewport().getLocalPlayersIndexes()[node.getViewport().getLocalPlayersIndexesCount()] = playerIndex;
			}
		}
		return stream;
	}
	
	/**
	 * Renders the players in the local player radius.
	 * @param buffer The buffer.
	 * @param updateBlockData The flag buffer.
	 * @param nsn0 NSN type.
	 */
	public void renderLocalPlayers(RS3PacketBuilder buffer, RS3PacketBuilder updateBlockData, boolean nsn0) {
		buffer.syncBits();
		int skip = 0;
		//System.out.println(player.getViewport().getLocalPlayersIndexesCount()+" local players.");
		for (int i = 0; i < player.getViewport().getLocalPlayersIndexesCount(); i++) {
			int playerIndex = player.getViewport().getLocalPlayersIndexes()[i];
			if (nsn0 ? (0x1 & player.getViewport().getSlotFlags()[playerIndex]) != 0 : (0x1 & player.getViewport().getSlotFlags()[playerIndex]) == 0) {
				continue;
			}
			if (skip > 0) {
				skip--;
				player.getViewport().getSlotFlags()[playerIndex] = (byte) (player.getViewport().getSlotFlags()[playerIndex] | 2);
				continue;
			}
			Player localPlayer = player.getViewport().getLocalPlayers()[playerIndex];
			if (needsRemove(localPlayer)) {
				System.out.println("[Local] Removing " + localPlayer.getAccount().getUsername().getName()+" (index="+playerIndex+")");
				removePlayer(buffer, playerIndex, localPlayer);
			} else {
				System.out.println("[Local] Updating " + localPlayer.getAccount().getUsername().getName()+" (index="+playerIndex+")");
				skip = updatePlayer(buffer, updateBlockData, localPlayer, i, skip, playerIndex, nsn0);
			}
		}
		buffer.unSyncBits();
	}
	
	/**
	 * Renders the players outside of the local player radius.
	 * @param buffer The buffer.
	 * @param updateBlockData The flag buffer.
	 * @param nsn2 NSN type.
	 */
	public void renderOutsidePlayers(RS3PacketBuilder buffer, RS3PacketBuilder updateBlockData, boolean nsn2) {
		buffer.syncBits();
		int skip = 0;
		player.getViewport().setLocalAddedPlayers(0);
		//System.out.println(player.getViewport().getOutPlayersIndexesCount()+" outside players.");
		for (int counter = 0; counter < player.getViewport().getOutPlayersIndexesCount(); counter++) {
			int playerIndex = player.getViewport().getOutPlayersIndexes()[counter];
			if (nsn2 ? (0x1 & player.getViewport().getSlotFlags()[playerIndex]) == 0 : (0x1 & player.getViewport().getSlotFlags()[playerIndex]) != 0) {
				continue;
			}
			if (skip > 0) {
				skip--;
				//System.err.println("Skip " + skip);
				player.getViewport().getSlotFlags()[playerIndex] = (byte) (player.getViewport().getSlotFlags()[playerIndex] | 2);
				continue;
			}
			Player globalPlayer = World.getWorld().getPlayer(playerIndex);
			/*if (globalPlayer == null) {
				continue;
			}*/
			if (needsAdd(globalPlayer)) {
				System.out.println("[Global] Adding " + globalPlayer.getAccount().getUsername().getName()+" (index="+playerIndex+")");
				queueOutsidePlayer(buffer, updateBlockData, globalPlayer, playerIndex);
			} else {
				skip = skipOutsidePlayer(buffer, globalPlayer, playerIndex, counter, skip, nsn2);
				System.out.println("[Global] Skipping from" + (globalPlayer == null ? "[null]" : globalPlayer.getAccount().getUsername().getName())+" (index="+playerIndex+", skipped="+skip+")");
			}
		}
		buffer.unSyncBits();
	}
	
	
	/**
	 * Verifies that a player needs to be removed from the update list.
	 * @param player The player.
	 * @return True if so; false otherwise.
	 */
	private boolean needsRemove(Player player) {
		return false;
	}

	/**
	 * Verifies that a player needs to be added to the update list.
	 * @param player The player to check.
	 * @return True if so; false otherwise.
	 */
	private boolean needsAdd(Player player) {
		return player != null && player.exists() && this.player.getTile().withinDistance(player.getTile(), 14) && this.player.getViewport().getLocalAddedPlayers() < 15;
	}

	/**
	 * Checks if a player needs an appearance update.
	 * @param index The index to check.
	 * @param hash The hash.
	 * @return True if so; false otherwise.
	 */
	private boolean needAppearenceUpdate(int index, byte[] hash) {
		if (player.getViewport().getTotalRenderDataSentLength() > ((Constants.PACKET_SIZE_LIMIT - 500) / 2) || hash == null)
			return false;
		return player.getViewport().getCachedAppearencesHashes()[index] == null || !MessageDigest.isEqual(player.getViewport().getCachedAppearencesHashes()[index], hash);
	}
	
	/**
	 * Skips a player in the update list.
	 * @param stream The buffer.
	 * @param amount The skip amount.
	 */
	private void skipPlayers(RS3PacketBuilder stream, int amount) {
		/*
		 * Signifies a skip is happening.
		 */
		//System.out.println("Packing skip for "+amount+" players...");
		stream.putBits(2, amount == 0 ? 0 : amount > 255 ? 3 : (amount > 31 ? 2 : 1));
		if (amount > 0) {
			/*
			 * Signifies the skip.
			 */
			stream.putBits(amount > 255 ? 11 : (amount > 31 ? 8 : 5), amount);
		}
	}
	
	/**
	 * Removes a player from the update queue.
	 * @param buffer The buffer.
	 * @param playerIndex The index.
	 * @param localPlayer The player.
	 */
	private void removePlayer(RS3PacketBuilder buffer, int playerIndex, Player localPlayer) {
		/*
		 * Signifies an update.
		 */
		buffer.putBits(1, 1);//update=true
		/*
		 * Signifies a removal.
		 */
		buffer.putBits(1, 0);//maskUpdate=false
		/*
		 * Signifies nothing happened.
		 */
		buffer.putBits(2, 0);//type=0
		player.getViewport().getRegionHashes()[playerIndex] = localPlayer.getLastTile() == null ? localPlayer.getTile().getRegionHash() : localPlayer.getLastTile().getRegionHash();
		int hash = localPlayer.getTile().getRegionHash();
		if (hash == player.getViewport().getRegionHashes()[playerIndex]) {
			/*
			 * Signifies no update is required.
			 */
			buffer.putBits(1, 0);//regionUpdate=false
		} else {
			/*
			 * Signifies an update is required.
			 */
			buffer.putBits(1, 1);//regionUpdate=true
			updateRegionHash(buffer, player.getViewport().getRegionHashes()[playerIndex], hash);
			player.getViewport().getRegionHashes()[playerIndex] = hash;
		}
		player.getViewport().getLocalPlayers()[playerIndex] = null;
	}
	
	/**
	 * Updates a local player.
	 * @param buffer The buffer.
	 * @param updateBlockData The update block data.
	 * @param localPlayer The local player.
	 * @param counter The counter.
	 * @param skip The skip.
	 * @param playerIndex The index.
	 * @param nsn0 NSN type.
	 */
	public int updatePlayer(RS3PacketBuilder buffer, RS3PacketBuilder updateBlockData, Player localPlayer, int counter, int skip, int playerIndex, boolean nsn0) {
		boolean needAppearenceUpdate = needAppearenceUpdate(localPlayer.getIndex(), localPlayer.getUpdateArchive().getAppearance().getEncryptedBuffer());
		boolean needUpdate = localPlayer.getUpdateArchive().flagged() || needAppearenceUpdate;
		needUpdate = false;//TODO: Remove this when ready to debug the update block(s)
		if (needUpdate) {
			/*
			 * A flag update is dispatched to render player's physical properties.
			 */
			System.out.println("Mask updated needed for "+playerIndex);
			performFlagUpdate(localPlayer, updateBlockData, needAppearenceUpdate, false);
		}
		if (localPlayer.getUpdateArchive().getMovement().hasTeleported()) {
			/*
			 * Gives the client proper instructions on how to teleport the local
			 * player and update it's position.
			 */
			queueLocalTeleportUpdate(buffer, localPlayer, needUpdate);
		} else if (localPlayer.getUpdateArchive().getMovement().getNextWalkDirection() != -1) {
			/*
			 * Gives the client proper instructions on how to move a local
			 * character and update it's position.
			 */
			queueLocalMovementUpdate(buffer, updateBlockData, localPlayer, needUpdate, needAppearenceUpdate);
		} else if (needUpdate) {
			/*
			 * Signies an update is needed.
			 */
			buffer.putBits(1, 1);
			/*
			 * Lets the client know to remove the walking queue.
			 */
			buffer.putBits(1, 1);
			/*
			 * Signies that the local player did not move.
			 */
			buffer.putBits(2, 0);
		} else {
			buffer.putBits(1, 0);//No update needed
			for (int indexCounter = counter + 1; indexCounter < player.getViewport().getLocalPlayersIndexesCount(); indexCounter++) {
				int otherIndex = player.getViewport().getLocalPlayersIndexes()[indexCounter];
				if (nsn0 ? (0x1 & player.getViewport().getSlotFlags()[otherIndex]) != 0 : (0x1 & player.getViewport().getSlotFlags()[otherIndex]) == 0)
					continue;
				Player otherPlayer = player.getViewport().getLocalPlayers()[otherIndex];
				if (needsRemove(otherPlayer) || otherPlayer.getUpdateArchive().getMovement().hasTeleported() || otherPlayer.getUpdateArchive().getMovement().getNextWalkDirection() != -1 || (otherPlayer.getUpdateArchive().flagged() || needAppearenceUpdate(otherPlayer.getIndex(), otherPlayer.getUpdateArchive().getAppearance().getEncryptedBuffer()))) {
					break;
				}
				skip++;
			}
			skipPlayers(buffer, skip);
			player.getViewport().getSlotFlags()[playerIndex] = (byte) (player.getViewport().getSlotFlags()[playerIndex] | 2);
		}
		return skip;
	}
	
	/**
	 * Appends a teleport update to the client .
	 * @param buffelayer The r The buffer being written.
	 * @param localPplayer being updated
	 * @param blockUpdate If a block update is happening.
	 */
	private void queueLocalTeleportUpdate(RS3PacketBuilder buffer, Player localPlayer, boolean blockUpdate) {
		/*
		 * Signifies a update requirement.
		 */
		buffer.putBits(1, 1);
		/*
		 * Signifies a block update was executed.
		 */
		buffer.putBits(1, blockUpdate ? 1 : 0);
		/*
		 * Signifies a teleport update was executed.
		 */
		buffer.putBits(2, 3);
		int xOffset = localPlayer.getTile().getX() - localPlayer.getLastTile().getX();
		int yOffset = localPlayer.getTile().getY() - localPlayer.getLastTile().getY();
		int planeOffset = localPlayer.getTile().getPlane() - localPlayer.getLastTile().getPlane();
		int unknownValue = 0;//TODO: Figure out what this is...
		if (Math.abs(localPlayer.getTile().getX() - localPlayer.getLastTile().getX()) <= 14 && Math.abs(localPlayer.getTile().getY() - localPlayer.getLastTile().getY()) <= 14) {
			/*
			 * Signifies X & Y distance if out of local range
			 */
			buffer.putBits(1, 0);
			if (xOffset < 0) {
				xOffset += 32;
			}
			if (yOffset < 0) {
				yOffset += 32;
			}
			/*
			 * Signifies the new coordinates data.
			 */
			buffer.putBits(15, yOffset + (xOffset << 5) + (planeOffset << 10) + (unknownValue << 12));
		} else {
			/*
			 * Signifies the update was within range.
			 */
			buffer.putBits(1, 1);
			buffer.putBits(3, unknownValue);
			/*
			 * Signifies the new coordinates data.
			 */
			buffer.putBits(30, (yOffset & 0x3fff) + ((xOffset & 0x3fff) << 14) + ((planeOffset & 0x3) << 28));
		}
	}
	
	/**
	 * Appends a local movement update.
	 * @param buffer The buffer.
	 * @param updateBlockData The data.
	 * @param localPlayer The player.
	 * @param blockUpdate If block update.
	 * @param appearanceUpdate If appearance update.
	 */
	private void queueLocalMovementUpdate(RS3PacketBuilder buffer, RS3PacketBuilder updateBlockData, Player localPlayer, boolean blockUpdate, boolean appearanceUpdate) {
		int dx = MovementUtils.DIRECTION_DELTA_X[localPlayer.getUpdateArchive().getMovement().getNextWalkDirection()];
		int dy = MovementUtils.DIRECTION_DELTA_Y[localPlayer.getUpdateArchive().getMovement().getNextWalkDirection()];
		boolean running;
		int opcode;
		if (localPlayer.getUpdateArchive().getMovement().getNextRunDirection() != -1) {
			dx += MovementUtils.DIRECTION_DELTA_X[localPlayer.getUpdateArchive().getMovement().getNextRunDirection()];
			dy += MovementUtils.DIRECTION_DELTA_Y[localPlayer.getUpdateArchive().getMovement().getNextRunDirection()];
			opcode = MovementUtils.getPlayerRunningDirection(dx, dy);
			if (opcode == -1) {
				running = false;
				opcode = MovementUtils.getPlayerWalkingDirection(dx, dy);
			} else
				running = true;
		} else {
			running = false;
			opcode = MovementUtils.getPlayerWalkingDirection(dx, dy);
		}
		/*
		 * Signifies that an update is required.
		 */
		buffer.putBits(1, 1);
		if ((dx == 0 && dy == 0)) {
			/*
			 * Signifies nothing has changed.
			 */
			buffer.putBits(1, 1);
			/*
			 * Signifies that the player didn't move.
			 */
			buffer.putBits(2, 0);
			if (!blockUpdate) {
				performFlagUpdate(localPlayer, updateBlockData, appearanceUpdate, false);
			}
		} else {
			/*
			 * Signifies a block update.
			 */
			buffer.putBits(1, blockUpdate ? 1 : 0);
			/*
			 * Signifies the movement type 
			 */
			buffer.putBits(2, running ? 2 : 1);
			/*
			 * Lets the client know how to move the character
			 */
			buffer.putBits(running ? 4 : 3, opcode);
			
			if (!running) {
				buffer.putBits(1, 0);//Not really sure what this does; leaving it off to be safe
			}
		}
	}
	
	/**
	 * Queues an outside player to the local player list.
	 * @param buffer The buffer.
	 * @param updateBlockData The update buffer.
	 * @param outsidePlayer The outside player.
	 * @param playerIndex The player index.
	 */
	private void queueOutsidePlayer(RS3PacketBuilder buffer, RS3PacketBuilder updateBlockData, Player outsidePlayer, int playerIndex) {
		/*
		 * Signifies that an update happened.
		 */
		buffer.putBits(1, 1);
		/*
		 * Signifies an outer player update.
		 */
		buffer.putBits(2, 0);
		int hash = outsidePlayer.getTile().getRegionHash();
		if (hash == player.getViewport().getRegionHashes()[playerIndex]) {
			/*
			 * Signifies no update is happening.
			 */
			buffer.putBits(1, 0);
		} else {
			/*
			 * Signifies an update is happening.
			 */
			buffer.putBits(1, 1);
			updateRegionHash(buffer, player.getViewport().getRegionHashes()[playerIndex], hash);
			player.getViewport().getRegionHashes()[playerIndex] = hash;
		}
		/*
		 * Flags the region X coordinate for the client to update the position.
		 */
		buffer.putBits(6, outsidePlayer.getTile().getXInRegion());
		/*
		 * Flags the region Y coordinate for the client to update the position.
		 */
		buffer.putBits(6, outsidePlayer.getTile().getYInRegion());
		boolean needAppearenceUpdate = needAppearenceUpdate(outsidePlayer.getIndex(), outsidePlayer.getUpdateArchive().getAppearance().getEncryptedBuffer());
		performFlagUpdate(outsidePlayer, updateBlockData, needAppearenceUpdate, true);
		/*
		 * Signifies an update has happened.
		 */
		buffer.putBits(1, 1);
		player.getViewport().setLocalAddedPlayers(player.getViewport().getLocalAddedPlayers() + 1);
		player.getViewport().getLocalPlayers()[outsidePlayer.getIndex()] = outsidePlayer;
		player.getViewport().getSlotFlags()[playerIndex] = (byte) (player.getViewport().getSlotFlags()[playerIndex] | 2);
	}

	/**
	 * Skips an outside player due to the lack of update requirements,
	 * @param buffer The buffer.
	 * @param outsidePlayer The player.
	 * @param playerIndex The index.
	 * @param counter The counter.
	 * @param skip The skip.
	 * @param nsn2 NSN type.
	 */
	public int skipOutsidePlayer(RS3PacketBuilder buffer, Player outsidePlayer, int playerIndex, int counter, int skip, boolean nsn2) {
		int hash = outsidePlayer == null ? player.getViewport().getRegionHashes()[playerIndex] : outsidePlayer.getTile().getRegionHash();
		if (outsidePlayer != null && hash != player.getViewport().getRegionHashes()[playerIndex]) {
			/*
			 * Signifies an update is required.
			 */
			buffer.putBits(1, 1);
			updateRegionHash(buffer, player.getViewport().getRegionHashes()[playerIndex], hash);
			player.getViewport().getRegionHashes()[playerIndex] = hash;
		} else {
			/*
			 * Signifies there is no update needed.
			 */
			buffer.putBits(1, 0);
			for (int otherCounter = counter + 1; otherCounter < player.getViewport().getOutPlayersIndexesCount(); otherCounter++) {
				int otherIndex = player.getViewport().getOutPlayersIndexes()[otherCounter];
				if (nsn2 ? (0x1 & player.getViewport().getSlotFlags()[otherIndex]) == 0 : (0x1 & player.getViewport().getSlotFlags()[otherIndex]) != 0) {
					continue;
				}
				Player otherPlayer = World.getWorld().getPlayer(otherIndex);
				if (needsAdd(otherPlayer) || (otherPlayer != null && otherPlayer.getTile().getRegionHash() != player.getViewport().getRegionHashes()[otherIndex])) {
					break;
				}
				skip++;
				player.getViewport().getSlotFlags()[playerIndex] = (byte) (player.getViewport().getSlotFlags()[playerIndex] | 2);
			}
			skipPlayers(buffer, skip);
		}
		return skip;
	}
	
	/**
	 * Performs the flag update.
	 * @param p The player.
	 * @param data The data.
	 * @param needAppearenceUpdate If appearance update.
	 * @param added If added.
	 */
	private void performFlagUpdate(Player p, RS3PacketBuilder data, boolean needAppearenceUpdate, boolean added) {
		if (needAppearenceUpdate) {
			data.put(UpdateMasks.APPEARANCE);
			p.getUpdateArchive().getAppearance().load();
			if (p.getUpdateArchive().getLiveBlocks()[8] == null) {
				//p.getUpdateArchive().queue(AppearanceBlock.class);
			}
			//p.getUpdateArchive().getLiveBlocks()[8].appendToUpdateBlock(data, p);
		}
	}
	
	/**
	 * Updates a player's region hash.
	 * @param buffer The buffer.
	 * @param lastRegionHash The last region hash.
	 * @param currentRegionHash The current region hash.
	 */
	private void updateRegionHash(RS3PacketBuilder buffer, int lastRegionHash, int currentRegionHash) {
		int lastRegionX = lastRegionHash >> 8;
		int lastRegionY = 0xff & lastRegionHash;
		int lastPlane = lastRegionHash >> 16;
		int currentRegionX = currentRegionHash >> 8;
		int currentRegionY = 0xff & currentRegionHash;
		int currentPlane = currentRegionHash >> 16;
		int planeOffset = currentPlane - lastPlane;
		if (lastRegionX == currentRegionX && lastRegionY == currentRegionY) {
			buffer.putBits(2, 1);//type=1
			buffer.putBits(2, planeOffset);
		} else if (Math.abs(currentRegionX - lastRegionX) <= 1 && Math.abs(currentRegionY - lastRegionY) <= 1) {
			int opcode;
			int dx = currentRegionX - lastRegionX;
			int dy = currentRegionY - lastRegionY;
			if (dx == -1 && dy == -1)
				opcode = 0;
			else if (dx == 1 && dy == -1)
				opcode = 2;
			else if (dx == -1 && dy == 1)
				opcode = 5;
			else if (dx == 1 && dy == 1)
				opcode = 7;
			else if (dy == -1)
				opcode = 1;
			else if (dx == -1)
				opcode = 3;
			else if (dx == 1)
				opcode = 4;
			else
				opcode = 6;
			buffer.putBits(2, 2);//type=2
			buffer.putBits(5, (planeOffset << 3) + (opcode & 0x7));
		} else {
			int xOffset = currentRegionX - lastRegionX;
			int yOffset = currentRegionY - lastRegionY;
			int unknownValue = 0;//TODO: Find out what this is...
			buffer.putBits(2, 3);//type=3
			buffer.putBits(20, (yOffset & 0xff) + ((xOffset & 0xff) << 8) + (planeOffset << 16) + (unknownValue << 18));
		}
	}
}
