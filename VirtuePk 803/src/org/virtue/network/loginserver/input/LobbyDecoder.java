package org.virtue.network.loginserver.input;

import java.util.ArrayList;
import java.util.List;

import org.virtue.game.Country;
import org.virtue.game.ServerLocation;
import org.virtue.game.WorldHub;
import org.virtue.network.loginserver.handlers.LobbyHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;


/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class LobbyDecoder implements PacketDecoder<LobbyHandler> {

	@Override
	public LobbyHandler decodePacket(RS3PacketReader buffer, Session session, int opcode) {
		LobbyHandler handler = new LobbyHandler();
		List<WorldHub> worlds = new ArrayList<>();
		int size = buffer.get();
		for (int cycle = 0; cycle < size; cycle++) {
			final int id = buffer.getInt();
			final String name = buffer.getString();
			final ServerLocation server = ServerLocation.locations.get(buffer.getInt());
			final int flag = buffer.getInt();
			final String region = buffer.getString();
			final Country country = Country.values()[buffer.get()];
			final String ip = buffer.getString();
			//final int location = buffer.getInt();
			final int playerCount = buffer.getInt();
			final boolean members = buffer.get() == 1;
			final boolean online = buffer.get() == 1;
			worlds.add(new WorldHub(id, name, server, flag, region, country, ip, playerCount, members, online));
		}
		handler.putFlag("worlds", worlds);
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { 3 };
	}
}
