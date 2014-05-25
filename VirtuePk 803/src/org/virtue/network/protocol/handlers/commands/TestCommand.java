package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.game.logic.social.ChatManager;
import org.virtue.game.logic.social.clans.ClanChannelManager;
import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.game.logic.social.clans.ClanSettings;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class TestCommand implements Command {
	
	Player testRecruiter = new Player(new Account(new Username("TestRecruiter"), null, null, null, 0, 0));
	
	public TestCommand () {
		testRecruiter.getChatManager().setMyClanHash(1004L);
	}

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {		
		if (syntax.equalsIgnoreCase("rank")) {
			//ChatManager.clanManager.getClanData(player.getChatManager().getMyClanHash()).setRank(
			//		player.getAccount().getUsername().getAccountNameAsProtocol(), ClanRank.COORDINATOR);;
		} else {
			//ChatManager.clanManager.getChannelManager().joinGuestChannel(testRecruiter, "test_clan_1");
			//ChatManager.clanManager.joinClan(testRecruiter, player);
			//player.getChatManager().getClanChatManager().joinChannel(player, settings.getClanHash());
			//ChatManager.clanManager.autoSaveClanData();
		}
		//ChatManager..joinChannel(player, 40L);
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
		return new String[] { "test", "rank" };
	}
}
