package org.virtue.game.logic.node.entity.player.update.blocks;

import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.update.UpdateBlock;
import org.virtue.game.logic.node.entity.player.update.masks.Graphics;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * 
 * @author Tyler Telis <tyler@xlitersps.com>
 * @author Thomas Le Godais <thomaslegodais@live.com>
 * @author Taylor Moon <tatormoon@yahoo.com>
 * @since Aug 9, 2013
 * @version 1.0
 */
public class GraphicsBlock extends UpdateBlock {

	/**
	 * Represents the type.
	 */
	private int type;

	/**
	 * Constructs a new {@code GraphicsBlock.java}.
	 * @param type The type.
	 */
	public GraphicsBlock(int type) {
		this.type = type;
	}
	
	@Override
	public int getMask() {
		switch (type) {
		case 1:
			return 0x1;
		case 2:
			return 0x8000;
		case 3:
			return 0x400;
		case 4:
			return 0x800000;
		case 5:
			return 0x400000;
		default:
			return 0;
		}
	}

	@Override
	public int getBlockPosition() {
		switch (type) {
		case 1:
			return 11;
		case 2:
			return 5;
		case 3:
			return 8;
		case 4:
			return 18;
		case 5:
			return 2;
		default:
			return -1;
		}
	}

	@Override
	public int appendToUpdateBlock(RS3PacketBuilder buffer, Entity player) {
		int start = buffer.getPosition();
		switch (type) {
		case 1:
			Graphics gfx1 = player.getUpdateArchive().getGraphics()[0];
			buffer.putShort(gfx1.getId());
			buffer.putIntV1(gfx1.getSettingsHash());
			buffer.putByteS(gfx1.getRotationHash());
			break;
		case 2:
			Graphics gfx2 = player.getUpdateArchive().getGraphics()[1];
			buffer.putLEShortA(gfx2.getId());
			buffer.putIntV2(gfx2.getSettingsHash());
			buffer.putByteC(gfx2.getRotationHash());
			break;
		case 3:
			Graphics gfx3 = player.getUpdateArchive().getGraphics()[2];
			buffer.putLEShort(gfx3.getId());
			buffer.putIntV1(gfx3.getSettingsHash());
			buffer.putByteS(gfx3.getRotationHash());
			break;
		case 4:
			Graphics gfx4 = player.getUpdateArchive().getGraphics()[3];
			buffer.putShort(gfx4.getId());
			buffer.putInt(gfx4.getSettingsHash());
			buffer.putByteS(gfx4.getRotationHash());
			break;
		case 5:
			Graphics gfx5 = player.getUpdateArchive().getGraphics()[4];
			buffer.putShort(gfx5.getId());
			buffer.putIntV2(gfx5.getSettingsHash());
			buffer.putByteA(gfx5.getRotationHash());
			break;
		}
		//System.out.println("Appended graphic type="+type+", mask="+Integer.toHexString(getMask()));
		return buffer.getPosition() - start;
	}

}
