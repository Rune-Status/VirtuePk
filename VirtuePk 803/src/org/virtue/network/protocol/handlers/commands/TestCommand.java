package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.clans.ClanChannelManager;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class TestCommand implements Command {
	
	private ClanChannelManager mng = new ClanChannelManager();

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		mng.joinChannel(player, 40L);
		/*ClanSettings settings = new ClanSettings(12L);
		settings.setName("A clan");
		ClanChannel channel = new ClanChannel(settings);
		channel.join(new SocialUser(player), false);*/
		
		//player.loadMapRegion();
//		player.getUpdateArchive().queueAnimation(918);
//		String text = player.requestInput("Enter Amount:");
//		player.getPacketDispatcher().dispatchMessage(text);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "test" };
	}
}
