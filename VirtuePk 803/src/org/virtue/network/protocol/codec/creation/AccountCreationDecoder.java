package org.virtue.network.protocol.codec.creation;

import java.util.GregorianCalendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Age;
import org.virtue.game.logic.node.entity.player.identity.DateOfBirth;
import org.virtue.game.logic.node.entity.player.identity.Email;
import org.virtue.game.logic.node.entity.player.identity.Password;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.utility.Base37Utils;
import org.virtue.utility.BufferUtils;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 9, 2014
 */
public class AccountCreationDecoder extends FrameDecoder implements ChannelHandler {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() > 6) 
			return null;
		
		int day = buffer.readByte();
		int month = buffer.readByte();
		int year = buffer.readByte();
		int country = buffer.readByte();
		DateOfBirth dob =  new DateOfBirth(new GregorianCalendar(year, month, day));
		Username username = new Username(Base37Utils.decodeBase37(buffer.readLong()));
		Email email = new Email(BufferUtils.readString(buffer));
		Password password = new Password(BufferUtils.readString(buffer), false);
		Age age = new Age(buffer.readInt());
		sendCreationEmail(email, username, password);
		
		return new Account(username, password, Rank.PLAYER, email, age, dob);
	}
	
	private void sendCreationEmail(Email email, Username username, Password password) {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "127.0.0.1");
		Session session = Session.getDefaultInstance(properties);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("noreply@runelove.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getEmail()));
			message.setSubject("Account created for " + username.getAccountName() + "!");
			message.setText("Thank you for creating your account at RuneLove 3! You're gaming experience starts now./n" + "You may now login into the game using:/n" + "Username: " + username.getAccountName() + "/n" + "Password: " + password.getPassword());
			Transport.send(message);
		} catch (MessagingException i) {
			i.printStackTrace();
		}
	}

}
