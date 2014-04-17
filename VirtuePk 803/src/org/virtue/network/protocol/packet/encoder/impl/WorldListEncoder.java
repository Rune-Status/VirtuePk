package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.Lobby;
import org.virtue.game.logic.ServerLocation;
import org.virtue.game.logic.WorldHub;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 8, 2014
 */
public class WorldListEncoder implements PacketEncoder<Integer> {

	@Override
	public RS3PacketBuilder buildPacket(Integer node) {
		boolean fullUpdate = (node == 0);
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.WORLD_LIST_PACKET);
		buffer.put(1);//Must equal 1
		buffer.put(2);//Must equal 2
		buffer.put(fullUpdate ? 1 : 0);
		int size = Lobby.getWorlds().size();
		if (fullUpdate) {
			buffer.putSmart(ServerLocation.locations.size());
			for (ServerLocation loc : ServerLocation.locations) {//Server locations
				buffer.putSmart(loc.getCountryID());
				buffer.putGJString(loc.getName());
			}
			buffer.putSmart(0);
			buffer.putSmart(size + 1);
			buffer.putSmart(size);
			for (WorldHub world : Lobby.getWorlds()) {
				buffer.putSmart(world.getNodeID());//World node ID
				buffer.put(world.getServer().getID());//Physical location 
				int flags = 0;
				if (world.isMembers()) {
					flags |= 0x1;
				}
				flags |= 0x8;//TODO: Shift this method into the world data itself
				buffer.putInt(flags);//World flags
				
				buffer.putSmart(world.getCountry().getID());
				if (world.getCountry().getID() != 0) {
					buffer.putGJString((world.getCountry().getName() == null) ? "" : world.getCountry().getName());
				}
				
				buffer.putGJString(world.getActivity());//World activity
				
				buffer.putGJString(world.getIp());//Endpoint to connect to
			}
			buffer.putInt((int)Math.random()*Integer.MAX_VALUE);//World list hash (fairly sure this can be any non-zero value)
		}
		for (WorldHub world : Lobby.getWorlds()) {//Player counts
			buffer.putSmart(world.getNodeID());
			buffer.putShort(world.getPlayerCount());
		}
		buffer.endPacketVarShort();
		return buffer;
	}
}
