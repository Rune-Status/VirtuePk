package org.virtue.network.session.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.game.logic.node.entity.player.identity.DateOfBirth;
import org.virtue.game.logic.node.entity.player.identity.Password;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.network.io.IOHub;
import org.virtue.network.protocol.codec.creation.AccountCreationResponse;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.session.Session;
import org.virtue.utility.StringUtils;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 14, 2014
 */
public class AccountCreationSession extends Session {

	/**
	 * Constructs a new {@code AccountCreationSession.java}.
	 * @param context
	 */
	public AccountCreationSession(ChannelHandlerContext context) {
		super(context);
	}

	@Override
	public void decode(Object message) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		if (message instanceof DateOfBirth) {
			DateOfBirth birthDate = (DateOfBirth) message;
			int year = birthDate.getDateOfBirth().get(Calendar.YEAR);
			int currentYear = new GregorianCalendar().get(Calendar.YEAR);
			if (year > currentYear) {
				buffer.put(AccountCreationResponse.STATUS_DOB_FUTURE);
			} else if (year == currentYear) {
				buffer.put(AccountCreationResponse.STATUS_DOB_THIS_YEAR);
			} else if ((year - 1) < currentYear) {
				buffer.put(AccountCreationResponse.STATUS_DOB_LAST_YEAR);
			}			
			getTransmitter().send(buffer);
		} else if (message instanceof Username) {
			Username usern = (Username) message;
			String username = usern.getAccountName();
			if (IOHub.getAccountIo().exists(username)) {
				buffer.put(AccountCreationResponse.STATUS_USERNAME_UNAVAILABLE);
			}
			getTransmitter().send(buffer);
		} else if (message instanceof Password) {
			Password passw = (Password) message;
			String password = passw.getPassword();
			if (password.length() < 8 || password.length() > 32) {
				buffer.put(AccountCreationResponse.STATUS_PASSWORD_INVALID_LENGTH);
			} else if (!(StringUtils.isValidPassword(password))) {
				buffer.put(AccountCreationResponse.STATUS_PASSWORD_INVALID_CHARS);
			}
			passw.applyEncryption();
			getTransmitter().send(buffer);
		}
	}

	@Override
	public void disconnect() {
		
	}

}
