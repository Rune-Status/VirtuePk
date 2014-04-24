

package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.Constants;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.network.loginserver.message.LoginResult;
import org.virtue.network.protocol.codec.login.LoginType;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 8, 2014
 */
public class LoginEncoder implements PacketEncoder<Account> {

	@Override
	public RS3PacketBuilder buildPacket(Account node) {
		//TODO Document the rest.
                //System.out.println("Sending response.");
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		if (Constants.LOGIN_SERVER) {
			LoginResult result = node.getFlag("login_result", LoginResult.ERROR);
			if (!result.equals(LoginResult.SUCCESS)) {
				buffer.put(result.getOpcode());
				return buffer;
			}
		}
		buffer.putPacketVarByte(2);
		switch (node.getFlag("login_type", LoginType.WORLD_PART_2)) {
		case LOBBY:
			//System.out.println("Building lobby login response.");
			/*
			 * The rights of the user. Used to first create the client rights
			 * field for later use.
			 */
			buffer.put(node.getRank().getID());
			buffer.put(0);
			buffer.put(0);//Boolean
			buffer.put24BitInteger(0);
			buffer.put(0);//Byte
			buffer.put(0);//Boolean
			buffer.put(0);//Boolean
			buffer.putLong(500);//Subscription end date
			buffer.put5ByteInteger(0);
			/*
			 * A bit mask used to depict the status of the player's payment.
			 * 0x0 - None
			 * 0x1 - Member
			 * 0x2 - Subscription
			 */
			buffer.put(0x1 | 0x2);
			buffer.putInt(0);
			buffer.putInt(400000);
			/*
			 * Depicts if the user has set recovery questions.
			 * (Boolean), 1 - 0
			 */
			buffer.putShort(4000);
			/*
			 * The amount of un-read messages in the user's message center.
			 */
			buffer.putShort(0);
			/*
			 * The date at which the user last logged in at. As a hash.
			 */
			buffer.putShort(0);
			/*
			 * The IP at which the user last logged in from. As a hash.
			 */
			buffer.putInt(0);
			/*
			 * Represents the status of the user's email registration.
			 * 0 - Unregistered.
			 * 1 - Pending parental confirmation.
			 * 2 - Pending default confirmation.
			 * 3 - Registered email.
			 */
			buffer.put(3);
			buffer.putShort(0);
			buffer.putShort(0);
			buffer.put(0);
			/*
			 * The username of the player, used to set the field client sided
			 * for later use.
			 */
			buffer.putGJString(node.getUsername().getName());
			buffer.put(0);
			buffer.putInt(0);
			//buffer.put(0);
			/*
			 * The IP of the default world to login to. Used to create a secure connection
			 * to all Runescape servers.
			 */
			buffer.putShort(1);//Default world node ID
			buffer.putGJString("127.0.0.1");
			buffer.endPacketVarByte();
			return buffer;
		case WORLD_PART_2:
			/*
			 * The rights of the user. Used to first create the client rights
			 * field for later use.
			 */
			buffer.put(node.getRank().ordinal());
			buffer.put(0);//Byte
			buffer.put(0);//Boolean
			buffer.put(0);//Boolean
			buffer.put(1);//Boolean
			buffer.put(0);//Chat disabled
			buffer.putShort(node.getFlag("playerIndex", -1));//Player index
			buffer.put(1);//Is p2p account
			buffer.put24BitInteger(0);
			/*
			 * Signifies that the player is logging into a members world or not.
			 * This is used for enabling member restrictions such as member
			 * items.
			 * 0 - Free
			 * 1 - Members
			 */
			buffer.put(1);//Boolean
			buffer.putString("");//node.getUsername().getAccountName()
			buffer.endPacketVarByte();
			return buffer;
		default:
			return null;
		}
	}
}
