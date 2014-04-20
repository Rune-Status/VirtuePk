package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.Friend;
import org.virtue.game.logic.social.messages.FriendsMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class FriendEncoder implements PacketEncoder<FriendsMessage> {

	@Override
	public RS3PacketBuilder buildPacket(FriendsMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.FRIENDS_PACKET);
		if (node.isFullUpdate()) {
			for (Friend f : node.getFriends()) {
				packFriend(buffer, f, false);
			}
		} else {
			packFriend(buffer, node.getFriends()[0], node.isNameChange());
		}
		buffer.endPacketVarShort();
		return buffer;
	}
	
	private void packFriend (RS3PacketBuilder buffer, Friend f, boolean isNameChange) {
		boolean putOnline = (f.getWorldNodeID() != 0);
		int flags = 0;
		/*if (friend.isRecruited()) {
			flags |= 0x1;
		}*/
		if (f.isReferred()) {
			flags |= 0x2;
		}
		buffer.put(isNameChange ? 1 : 0);//Is this a notification of a friend name change
		buffer.putString(f.getName());//Current display name
		buffer.putString(f.getPrevName() == null ? "" : f.getPrevName());//Previous display name, or empty string if null
		buffer.putShort(putOnline ? f.getWorldNodeID() : 0);//NodeID (world ID) of friend, or 0 if offline
		buffer.put(f.getFcRank());//Rank in player's friends chat
		buffer.put(flags);//Flags (0x2=referred, 0x1=recruited)
		if (putOnline) {
			buffer.putString(f.getWorldName());//Friend world name
			buffer.put(0);//This always seems to be zero. Possibly physical server location? More info is needed.
			buffer.putInt(f.getWorldFlags());//Friend server flags
		}
		buffer.putString(f.getNote());//Note
	}

}
