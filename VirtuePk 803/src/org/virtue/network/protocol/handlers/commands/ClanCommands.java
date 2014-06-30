package org.virtue.network.protocol.handlers.commands;

import org.virtue.Launcher;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.game.logic.social.clans.internal.ClanSettings;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class ClanCommands implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		switch (syntax.toLowerCase()) {
		case "setrank":
			return processSetRank(player, args);
		case "kickclan":
			return processKick(player, args);
		case "makeclan":
			return makeClan(player, args);
		}
		return false;
	}
	
	private boolean makeClan (Player player, String...args) {
		if (player.getChatManager().getMyClanHash() != 0L) {
			player.getPacketDispatcher().dispatchMessage("You must leave your current clan first.", MessageOpcode.CONSOLE);
			return false;
		}
		if (args.length < 1) {
			player.getPacketDispatcher().dispatchMessage("You must specify a name for your new clan.", MessageOpcode.CONSOLE);
			return false;
		}
		String clanName = "";
		for (String arg : args) {
			clanName+= " "+arg;
		}
		String formattedName = StringUtils.format(clanName.trim(), FormatType.NAME);
		if (Launcher.getClanManager().createClan(formattedName, player.getChatManager().getSocialUser())) {
			player.getPacketDispatcher().dispatchMessage("Clan could not be created - Name already in use.", MessageOpcode.CONSOLE);
			return false;
		}
		return false;
	}
	
	private boolean processKick (Player player, String...args) {
		if (!player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			player.getPacketDispatcher().dispatchMessage("You must be an administrator to use this command.", MessageOpcode.CONSOLE);
			return false;
		}
		if (args.length < 2) {
			player.getPacketDispatcher().dispatchMessage("You must specify a clan hash and player name.", MessageOpcode.CONSOLE);
			return false;
		}
		long clanHash;
		String memberName;
		try {
			clanHash = Long.parseLong(args[0]);
			memberName = args[1];
		} catch (NumberFormatException ex) {
			player.getPacketDispatcher().dispatchMessage("You must specify a clan hash and player name.", MessageOpcode.CONSOLE);
			return false;
		}
		if (!Launcher.getClanManager().clanExists(clanHash)) {
			player.getPacketDispatcher().dispatchMessage("The clan you have specified does not exist.", MessageOpcode.CONSOLE);
			return false;
		}
		if (Launcher.getClanManager().kickClanMember(clanHash, player.getChatManager().getSocialUser(), memberName)) {
			player.getPacketDispatcher().dispatchMessage("Successfully kicked "+memberName+" from their clan.", MessageOpcode.CONSOLE);
			return true;
		} else {
			player.getPacketDispatcher().dispatchMessage("Failed to kick "+memberName+" from their clan.", MessageOpcode.CONSOLE);
			return false;
		}
	}
	
	private boolean processSetRank (Player player, String... args) {
		if (!player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			player.getPacketDispatcher().dispatchMessage("You must be an administrator to use this command.", MessageOpcode.CONSOLE);
			return false;
		}
		long clanHash;
		String playerName;
		ClanRank rank;
		if (args.length < 3) {
			player.getPacketDispatcher().dispatchMessage("You must specify a clan hash, player name and a rank ID.", MessageOpcode.CONSOLE);
			return false;
		}
		try {
			clanHash = Long.parseLong(args[0]);
			playerName = args[1];
			rank = ClanRank.forID(Integer.parseInt(args[2]));
		} catch (NumberFormatException ex) {
			player.getPacketDispatcher().dispatchMessage("You must specify a clan hash, player name and a rank ID.", MessageOpcode.CONSOLE);
			return false;
		}
		if (rank == null) {
			player.getPacketDispatcher().dispatchMessage("You must specify a valid rank ID (recruit=0, general=5, admin=100, overseer=103, deputy owner=125).", MessageOpcode.CONSOLE);
			return false;
		}
		if (!Launcher.getClanManager().clanExists(clanHash)) {
			player.getPacketDispatcher().dispatchMessage("The clan you have specified does not exist.", MessageOpcode.CONSOLE);
			return false;
		}
		if (Launcher.getClanManager().setRank(clanHash, player.getChatManager().getSocialUser(), playerName, rank)) {
			player.getPacketDispatcher().dispatchMessage("Successfully changed the rank of "+playerName+" to "+rank.getName()+".", MessageOpcode.CONSOLE);
			return true;
		} else {
			player.getPacketDispatcher().dispatchMessage("Failed to change the rank of "+playerName+".", MessageOpcode.CONSOLE);
			return false;
		}
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] {"setrank", "kickclan", "makeclan"};
	}

}
