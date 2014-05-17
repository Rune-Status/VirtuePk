package org.virtue.network.protocol.packet.encoder.impl;

import java.security.MessageDigest;

import org.virtue.Constants;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.config.UpdateMasks;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.update.UpdateBlock;
import org.virtue.game.logic.node.entity.player.update.blocks.AppearanceBlock;
import org.virtue.game.logic.node.entity.player.update.movement.MovementUtils;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;


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
		renderOutsidePlayers(stream, updateBlockData, true);
		renderOutsidePlayers(stream, updateBlockData, false);
		stream.put(updateBlockData.buffer(), 0, updateBlockData.getPosition());
		stream.endPacketVarShort();
		node.getViewport().repackViewport();
		/*node.getViewport().setTotalRenderDataSentLength(0);
		node.getViewport().setLocalPlayersIndexesCount(0);
		node.getViewport().setOutPlayersIndexesCount(0);
		for (int playerIndex = 1; playerIndex < 2048; playerIndex++) {
			player.getViewport().getSlotFlags()[playerIndex] >>= 1;
			Player player = node.getViewport().getLocalPlayers()[playerIndex];
			if (player == null) {
				node.getViewport().getOutPlayersIndexes()[node.getViewport().getOutPlayersIndexesCount()] = playerIndex;
				node.getViewport().increaseOutsideIndexesCount();//.setOutPlayersIndexesCount(node.getViewport().getOutPlayersIndexesCount() + 1);
			} else {
				node.getViewport().getLocalPlayersIndexes()[node.getViewport().getLocalPlayersIndexesCount()] = playerIndex;
				node.getViewport().increaseLocalIndexesCount();//.setLocalPlayersIndexesCount(node.getViewport().getLocalPlayersIndexesCount() + 1);
			}
		}*/
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
				removePlayer(buffer, playerIndex, localPlayer);
			} else {
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
		for (int counter = 0; counter < player.getViewport().getOutPlayersIndexesCount(); counter++) {
			int playerIndex = player.getViewport().getOutPlayersIndexes()[counter];
			if (nsn2 ? (0x1 & player.getViewport().getSlotFlags()[playerIndex]) == 0 : (0x1 & player.getViewport().getSlotFlags()[playerIndex]) != 0) {
				continue;
			}
			if (skip > 0) {
				skip--;
				player.getViewport().getSlotFlags()[playerIndex] = (byte) (player.getViewport().getSlotFlags()[playerIndex] | 2);
				continue;
			}
			Player globalPlayer = World.getWorld().getPlayer(playerIndex);
			/*if (globalPlayer == null) {
				continue;
			}*/
			if (needsAdd(globalPlayer)) {
				queueOutsidePlayer(buffer, updateBlockData, globalPlayer, playerIndex);
			} else {
				skip = skipOutsidePlayer(buffer, globalPlayer, playerIndex, counter, skip, nsn2);
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
		if (!player.exists() || !this.player.getTile().withinDistance(player.getTile(), 14)) {
			return true;
		}
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
		stream.putBits(2, amount == 0 ? 0 : amount > 255 ? 3 : (amount > 31 ? 2 : 1));
		if (amount > 0) {
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
		buffer.putBits(1, 1);
		buffer.putBits(1, 0);
		buffer.putBits(2, 0);
		player.getViewport().getRegionHashes()[playerIndex] = localPlayer.getLastTile() == null ? localPlayer.getTile().getRegionHash() : localPlayer.getLastTile().getRegionHash();
		int hash = localPlayer.getTile().getRegionHash();
		if (hash == player.getViewport().getRegionHashes()[playerIndex]) {
			buffer.putBits(1, 0);
		} else {
			buffer.putBits(1, 1);
			updateRegionHash(buffer, player.getViewport().getRegionHashes()[playerIndex], hash, localPlayer);
			player.getViewport().getRegionHashes()[playerIndex] = hash;
		}
		player.getViewport().removeLocalPlayer(playerIndex);
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
		boolean needAppearenceUpdate = needAppearenceUpdate(localPlayer.getIndex(), localPlayer.getUpdateArchive().getAppearance().getMD5Hash());
		boolean needUpdate = localPlayer.getUpdateArchive().flagged() || needAppearenceUpdate;
		if (needUpdate) {
			performFlagUpdate(localPlayer, updateBlockData, needAppearenceUpdate, false);
		}
		if (localPlayer.getUpdateArchive().getMovement().hasTeleported()) {
			queueLocalTeleportUpdate(buffer, localPlayer, needUpdate);
		} else if (localPlayer.getUpdateArchive().getMovement().getNextWalkDirection() != -1) {
			queueLocalMovementUpdate(buffer, updateBlockData, localPlayer, needUpdate, needAppearenceUpdate);
		} else if (needUpdate) {
			buffer.putBits(1, 1);
			buffer.putBits(1, 1);
			buffer.putBits(2, 0);
		} else {
			buffer.putBits(1, 0);
			for (int indexCounter = counter + 1; indexCounter < player.getViewport().getLocalPlayersIndexesCount(); indexCounter++) {
				int otherIndex = player.getViewport().getLocalPlayersIndexes()[indexCounter];
				if (nsn0 ? (0x1 & player.getViewport().getSlotFlags()[otherIndex]) != 0 : (0x1 & player.getViewport().getSlotFlags()[otherIndex]) == 0)
					continue;
				Player otherPlayer = player.getViewport().getLocalPlayers()[otherIndex];
				if (needsRemove(otherPlayer) || otherPlayer.getUpdateArchive().getMovement().hasTeleported() || otherPlayer.getUpdateArchive().getMovement().getNextWalkDirection() != -1 || (otherPlayer.getUpdateArchive().flagged() || needAppearenceUpdate(otherPlayer.getIndex(), otherPlayer.getUpdateArchive().getAppearance().getMD5Hash()))) {
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
		buffer.putBits(1, 1);
		buffer.putBits(1, blockUpdate ? 1 : 0);
		buffer.putBits(2, 3);
		int xOffset = localPlayer.getTile().getX() - localPlayer.getLastTile().getX();
		int yOffset = localPlayer.getTile().getY() - localPlayer.getLastTile().getY();
		int planeOffset = localPlayer.getTile().getPlane() - localPlayer.getLastTile().getPlane();
		int unknownValue = localPlayer.getUpdateArchive().getMovement().isRunning() ? 3 : 2;//TODO: Figure out what this is...
		if (Math.abs(localPlayer.getTile().getX() - localPlayer.getLastTile().getX()) <= 14 && Math.abs(localPlayer.getTile().getY() - localPlayer.getLastTile().getY()) <= 14) {
			buffer.putBits(1, 0);
			if (xOffset < 0) {
				xOffset += 32;
			}
			if (yOffset < 0) {
				yOffset += 32;
			}
			buffer.putBits(15, yOffset + (xOffset << 5) + (planeOffset << 10) + (unknownValue << 12));
		} else {
			buffer.putBits(1, 1);
			buffer.putBits(3, unknownValue);
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
		if (localPlayer.getUpdateArchive().getMovement().needsTypeUpdate()) {
			buffer.putBits(1, 1);
			buffer.putBits(1, blockUpdate ? 1 : 0);
			buffer.putBits(2, 3);
			int xOffset = localPlayer.getTile().getX() - localPlayer.getLastTile().getX();
			int yOffset = localPlayer.getTile().getY() - localPlayer.getLastTile().getY();
			int planeOffset = localPlayer.getTile().getPlane() - localPlayer.getLastTile().getPlane();
			buffer.putBits(1, 0);
			if (xOffset < 0) {
				xOffset += 32;
			}
			if (yOffset < 0) {
				yOffset += 32;
			}
			//System.out.println("yOffset="+yOffset+", xOffset="+xOffset+", type="+(localPlayer.getUpdateArchive().getMovement().isRunning() ? 3 : 2));
			//System.out.println("newX="+localPlayer.getTile().getX()+", oldX="+localPlayer.getLastTile().getX()+", newY="+localPlayer.getTile().getY()+", oldY="+localPlayer.getLastTile().getY());
			buffer.putBits(15, (yOffset) + (xOffset << 5) + (planeOffset << 10) + ((localPlayer.getUpdateArchive().getMovement().isRunning() ? 3 : 2) << 12));
			return;
		}
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
		buffer.putBits(1, 1);
		//System.out.println("Moved by x="+dx+", y="+dy+", running="+running+", direction="+opcode);
		if ((dx == 0 && dy == 0)) {
			buffer.putBits(1, 1);
			buffer.putBits(2, 0);
			if (!blockUpdate) {
				performFlagUpdate(localPlayer, updateBlockData, appearanceUpdate, false);
			}
		} else {
			buffer.putBits(1, blockUpdate ? 1 : 0);
			buffer.putBits(2, running ? 2 : 1);
			buffer.putBits(running ? 4 : 3, opcode);
			if (!running) {
				buffer.putBits(1, 0);
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
		buffer.putBits(1, 1);
		buffer.putBits(2, 0);
		int hash = outsidePlayer.getTile().getRegionHash();
		if (hash == player.getViewport().getRegionHashes()[playerIndex]) {
			buffer.putBits(1, 0);
		} else {
			buffer.putBits(1, 1);
			updateRegionHash(buffer, player.getViewport().getRegionHashes()[playerIndex], hash, outsidePlayer);
			player.getViewport().getRegionHashes()[playerIndex] = hash;
		}
		buffer.putBits(6, outsidePlayer.getTile().getXInRegion());
		buffer.putBits(6, outsidePlayer.getTile().getYInRegion());
		boolean needAppearenceUpdate = needAppearenceUpdate(outsidePlayer.getIndex(), outsidePlayer.getUpdateArchive().getAppearance().getMD5Hash());
		performFlagUpdate(outsidePlayer, updateBlockData, needAppearenceUpdate, true);
		buffer.putBits(1, 1);
		player.getViewport().addLocalPlayer(playerIndex, outsidePlayer);
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
			buffer.putBits(1, 1);
			updateRegionHash(buffer, player.getViewport().getRegionHashes()[playerIndex], hash, outsidePlayer);
			player.getViewport().getRegionHashes()[playerIndex] = hash;
		} else {
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
		data.putShort(0);//Useless
		int mask = 0;
		if(needAppearenceUpdate) {
			mask |= UpdateMasks.APPEARANCE;
			if (p.getUpdateArchive().getLiveBlocks()[14] == null) {
				p.getUpdateArchive().queue(AppearanceBlock.class);
			}
		}
		for (UpdateBlock block : p.getUpdateArchive().getLiveBlocks()) {
			if (block == null) {
				continue;
			}
			if (block instanceof AppearanceBlock && needAppearenceUpdate) {
				continue;
			}
			mask |= block.getMask();
		}
		//System.out.println("Mask=0x"+Integer.toHexString(mask));
		if (mask >= 0x100) {
			mask |= 0x8;
			if (mask >= 0x10000) {
				mask |= 0x2000;
				data.put(mask & 0xFF);
				data.put((mask >> 8) & 0xFFFF);
				data.put(mask >> 16);
			} else {
				data.put(mask & 0xFF);
				data.put(mask >> 8);
			}
		} else {
			data.put(mask & 0xFF);
		}
		for (UpdateBlock block : p.getUpdateArchive().getLiveBlocks()) {
			if (block == null) {
				continue;
			}
			int length = block.appendToUpdateBlock(data, p);
			player.getViewport().incrementTotalRenderDataSentLength(length);//.setTotalRenderDataSentLength(p.getViewport().getTotalRenderDataSentLength() + length);
			//System.out.println("Appending update block...");
		}
		if (needAppearenceUpdate) {
			player.getViewport().getCachedAppearencesHashes()[p.getIndex()] = p.getUpdateArchive().getAppearance().getMD5Hash();
		}
		/*if (needAppearenceUpdate) {
			data.put(UpdateMasks.APPEARANCE);
			if (p.getUpdateArchive().getLiveBlocks()[8] == null) {
				p.getUpdateArchive().queue(AppearanceBlock.class);
			}
			int length = p.getUpdateArchive().getLiveBlocks()[8].appendToUpdateBlock(data, p);
			player.getViewport().setTotalRenderDataSentLength(p.getViewport().getTotalRenderDataSentLength() + length);
			player.getViewport().getCachedAppearencesHashes()[p.getIndex()] = p.getUpdateArchive().getAppearance().getMD5Hash();
			//System.out.println("Adding player appearance update to cache: hash="+player.getViewport().getCachedAppearencesHashes()[player.getIndex()]+", player="+player.getIndex());
			System.out.println("Packing appearance update for player: "+p.getAccount().getUsername().getName()+" at index: "+p.getIndex());
			//player.getViewport().getCachedAppearencesHashes()[p.getIndex()] = p.getUpdateArchive().getAppearance().getMD5Hash();
			//p.getUpdateArchive().getLiveBlocks()[8] = null;
		}*/
	}
	
	/**
	 * Updates a player's region hash.
	 * @param buffer The buffer.
	 * @param lastRegionHash The last region hash.
	 * @param currentRegionHash The current region hash.
	 */
	private void updateRegionHash(RS3PacketBuilder buffer, int lastRegionHash, int currentRegionHash, Player p) {
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
			int movementType = (p.getUpdateArchive().getMovement().isRunning() ? 3 : 2);
			buffer.putBits(2, 3);//type=3
			buffer.putBits(20, (yOffset & 0xff) + ((xOffset & 0xff) << 8) + (planeOffset << 16) + (movementType << 18));
		}
	}
}
