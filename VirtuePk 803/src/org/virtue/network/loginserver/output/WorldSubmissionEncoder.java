package org.virtue.network.loginserver.output;

import org.virtue.Constants;
import org.virtue.game.World;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Taylor
 * @date Jan 14, 2014
 */
public class WorldSubmissionEncoder implements PacketEncoder<Object> {

	@Override
	public RS3PacketBuilder buildPacket(Object node) {//TODO: Is this even used?
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.put(0);
		buffer.putInt(Constants.WORLD_ID);
		buffer.putString(World.getWorld().getActivity());
		buffer.putInt(World.getWorld().getServer().getID());//Server location
		buffer.putInt(World.getWorld().getFlag());
		buffer.putString(World.getWorld().getRegion());
		buffer.put(World.getWorld().getCountry().ordinal());
		buffer.putString(World.getWorld().getIp());
		//buffer.putInt(World.getWorld().getLocation());
		buffer.putInt(World.getWorld().getPlayerCount());
		buffer.put(World.getWorld().isMembers() ? 1 : 0);
		buffer.put(World.getWorld().isOnline() ? 1 : 0);
		return buffer;
	}
}
