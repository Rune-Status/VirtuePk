package org.virtue.network.protocol.packet.encoder.impl;

import java.util.Iterator;
import java.util.LinkedList;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.update.UpdateBlock;
import org.virtue.game.logic.node.entity.player.update.movement.MovementUtils;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;
import org.virtue.utility.EntityList;

public class NPCEncoder implements PacketEncoder<Player> {

	private Player player;
	
	@Override
	public RS3PacketBuilder buildPacket(Player node) {
		player = node;
		RS3PacketBuilder stream = new RS3PacketBuilder();
		RS3PacketBuilder updateBlockData = new RS3PacketBuilder();
		stream.putPacketVarShort(player.hasLargeSceneView() ? 
				OutgoingOpcodes.LARGE_NPC_UPDATE_PACKET : OutgoingOpcodes.NPC_UPDATE_PACKET);
		processLocalNpcsInform(stream, updateBlockData, player.hasLargeSceneView());
		stream.put(updateBlockData.buffer(), 0, updateBlockData.getPosition());
		stream.endPacketVarShort();
		return stream;
	}

	private void processLocalNpcsInform(RS3PacketBuilder stream, RS3PacketBuilder updateBlockData, boolean largeSceneView) {
		stream.syncBits();
		processInScreenNpcs(stream, updateBlockData, largeSceneView);
		addInScreenNPCs(stream, updateBlockData, largeSceneView);
		if (updateBlockData.getPosition() > 0) {
			stream.putBits(15, 32767);
		}
		stream.unSyncBits();
	}

	private void processInScreenNpcs(RS3PacketBuilder stream, RS3PacketBuilder updateBlockData, boolean largeSceneView) {
		stream.putBits(8, player.getViewport().getLocalNPCs().size());
		for (Iterator<NPC> iterator = player.getViewport().getLocalNPCs().iterator(); iterator.hasNext();) {
			NPC npc = iterator.next();
			if (!npc.exists() || !npc.getTile().withinDistance(player.getTile()) || npc.getUpdateArchive().getMovement().hasTeleported()) {
				stream.putBits(1, 1);
				stream.putBits(2, 3);
				iterator.remove();
				continue;
			}
			boolean needUpdate = npc.getUpdateArchive().flagged();
			boolean walkUpdate = npc.getUpdateArchive().getMovement().getNextWalkDirection() != -1;
			stream.putBits(1, (needUpdate || walkUpdate) ? 1 : 0);
			if (walkUpdate) {
				stream.putBits(2, npc.getUpdateArchive().getMovement().getNextRunDirection() == -1 ? 1 : 2);
				if (npc.getUpdateArchive().getMovement().getNextRunDirection() != -1) {
					stream.putBits(1, 1);
				}
				stream.putBits(3, MovementUtils.getNpcMoveDirection(npc.getUpdateArchive().getMovement().getNextWalkDirection()));
				if (npc.getUpdateArchive().getMovement().getNextRunDirection() != -1) {
					stream.putBits(3, MovementUtils.getNpcMoveDirection(npc.getUpdateArchive().getMovement().getNextRunDirection()));
				}
				stream.putBits(1, needUpdate ? 1 : 0);
			} else if (needUpdate) {
				stream.putBits(2, 0);
			}
			if (needUpdate) {
				appendUpdateBlock(npc, updateBlockData);
			}
		}
	}

	private void addInScreenNPCs(RS3PacketBuilder stream, RS3PacketBuilder updateBlockData, boolean largeSceneView) {
		LinkedList<NPC> localNPCs = player.getViewport().getLocalNPCs();
		//for (int regionId : player.getSceneGraph().getRegionIds()) {
			EntityList<NPC> npcs = World.getWorld().getNpcs();
			for (NPC npc : npcs) {
				if (localNPCs.size() == 250) {
					break;
				}
				if (npc == null || !npc.exists() || localNPCs.contains(npc) || !npc.getTile().withinDistance(player.getTile(), 14)) {
					continue;
				}
				stream.putBits(15, npc.getIndex());
				boolean needUpdate = npc.getUpdateArchive().flagged();
				int x = npc.getTile().getX() - player.getTile().getX();
				int y = npc.getTile().getY() - player.getTile().getY();
				if (largeSceneView) {
					if (x < 127) {
						x += 256;
					}
					if (y < 127) {
						y += 256;
					}
				} else {
					if (x < 15) {
						x += 32;
					}
					if (y < 15) {
						y += 32;
					}
				}
				stream.putBits(1, npc.getUpdateArchive().getMovement().hasTeleported() ? 1 : 0);
				stream.putBits(largeSceneView ? 8 : 5, x);
				stream.putBits(1, needUpdate ? 1 : 0);
				stream.putBits(2, npc.getTile().getPlane());
				stream.putBits(15, npc.getID());
				stream.putBits(3, (npc.getDirection() >> 11) - 4);
				stream.putBits(largeSceneView ? 8 : 5, y);
				localNPCs.add(npc);
				if (needUpdate) {
					appendUpdateBlock(npc, updateBlockData);
				}
			}
		//}
	}

	private void appendUpdateBlock(NPC npc, RS3PacketBuilder updateBlockData) {
		int maskData = 0x0;
		for (UpdateBlock block : npc.getUpdateArchive().getLiveBlocks()) {
			if (block == null) {
				continue;
			}
			maskData |= block.getMask();
		}
		if (maskData > 0xff) {
			maskData |= 0x1;
		}
		if (maskData > 0xffff) {
			maskData |= 0x200;
		}
		if (maskData > 0xffffff) {
			maskData |= 0x40000;
		}
		updateBlockData.put(maskData);
		if (maskData > 0xff) {
			updateBlockData.put(maskData >> 8);
		}
		if (maskData > 0xffff) {
			updateBlockData.put(maskData >> 16);
		}
		if (maskData > 0xffffff) {
			updateBlockData.put(maskData >> 24);
		}
		for (UpdateBlock block : npc.getUpdateArchive().getLiveBlocks()) {
			if (block == null) {
				continue;
			}
			block.appendToUpdateBlock(updateBlockData, npc);
		}
	}


}
