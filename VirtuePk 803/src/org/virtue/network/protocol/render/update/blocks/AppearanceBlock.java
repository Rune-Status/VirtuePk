package org.virtue.network.protocol.render.update.blocks;

import org.virtue.game.node.entity.player.Player;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.render.update.UpdateBlock;

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
		return 0x80;
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
	public void appendToUpdateBlock(RS3PacketBuilder buf, Player player) {
		byte[] renderData = player.getUpdateArchive().getAppearance().getBuffer();		
		player.getViewport().setTotalRenderDataSentLength(player.getViewport().getTotalRenderDataSentLength() + renderData.length);
		player.getViewport().getCachedAppearencesHashes()[player.getIndex()] = player.getUpdateArchive().getAppearance().getMD5Hash();
		buf.putByteS(renderData.length);
		buf.putReverseA(renderData, 0, renderData.length);
		//System.out.println("Appearance block size: "+renderData.length);
	}
}
