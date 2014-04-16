package org.virtue.game.node.entity.player.update.blocks;

import org.virtue.config.UpdateMasks;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.update.UpdateBlock;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * @author Taylor
 * @version 1.0
 */
public class AppearanceBlock extends UpdateBlock {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.psyc.net.codec.rs2codec.update.UpdateBlock#getMask()
	 */
	@Override
	public int getMask() {
		return UpdateMasks.APPEARANCE;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.psyc.net.codec.rs2codec.update.UpdateBlock#getBlockPosition()
	 */
	@Override
	public int getBlockPosition() {
		return 8;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.psyc.net.codec.rs2codec.update.UpdateBlock#appendToUpdateBlock(com.psyc.iobuffer.PsycOutBuffer,
	 *      com.psyc.live.node.entity.player.account.Player, java.lang.Object)
	 */
	@Override
	public int appendToUpdateBlock(RS3PacketBuilder buf, Player player) {
		byte[] renderData = player.getUpdateArchive().getAppearance().getBuffer();	
		if (renderData == null) {
			player.getUpdateArchive().getAppearance().packBlock();
			renderData = player.getUpdateArchive().getAppearance().getBuffer();	
		}
		buf.putByteS(renderData.length);
		buf.putReverseA(renderData, 0, renderData.length);
		return renderData.length+1;
	}
}
