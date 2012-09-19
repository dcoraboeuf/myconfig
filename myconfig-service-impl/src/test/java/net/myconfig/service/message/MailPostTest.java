package net.myconfig.service.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.message.Message;
import net.myconfig.service.api.message.MessageChannel;
import net.myconfig.service.model.Ack;

import org.junit.Test;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class MailPostTest {

	@Test
	public void supports() {
		MailPost post = new MailPost(null, null);
		assertTrue(post.supports(MessageChannel.EMAIL));
	}

	@Test
	public void supports_other() {
		MailPost post = new MailPost(null, null);
		assertFalse(post.supports(null));
	}

	@Test
	public void post() {
		// Mail sender
		JavaMailSender sender = mock(JavaMailSender.class);
		// Configuration
		ConfigurationService configurationService = mock(ConfigurationService.class);
		// Service
		MailPost post = new MailPost(sender, configurationService);
		// Message
		Message message = new Message("My title", "My content");
		// Sends
		Ack ack = post.post(message, "destination");
		// Checks
		assertTrue(ack.isSuccess());
		// Calls
		verify(configurationService, times(1)).getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS);
		verify(sender, times(1)).send(any(MimeMessagePreparator.class));
	}

	@Test
	public void post_mail_exception() {
		// Mail sender
		JavaMailSender sender = mock(JavaMailSender.class);
		doThrow(new MailException("Mail cannot be sent") {
		}).when(sender).send(any(MimeMessagePreparator.class));
		// Configuration
		ConfigurationService configurationService = mock(ConfigurationService.class);
		// Service
		MailPost post = new MailPost(sender, configurationService);
		// Message
		Message message = new Message("My title", "My content");
		// Sends
		Ack ack = post.post(message, "destination");
		// Checks
		assertFalse(ack.isSuccess());
		// Calls
		verify(configurationService, times(1)).getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS);
		verify(sender, times(1)).send(any(MimeMessagePreparator.class));
	}
	
	@Test
	public void prepareMessage() throws AddressException, MessagingException {
		Message message = new Message("My title", "My content");
		String destination = "destination";
		String replyToAddress = "replyToAddress";
		MailPost post = new MailPost(null, null);
		MimeMessage m = mock(MimeMessage.class);
		post.prepareMessage(m, message, destination, replyToAddress);
		verify(m, times(1)).setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(destination));
		verify(m, times(1)).setFrom(new InternetAddress(replyToAddress));
		verify(m, times(1)).setSubject("My title");
		verify(m, times(1)).setText("My content");
	}

}
