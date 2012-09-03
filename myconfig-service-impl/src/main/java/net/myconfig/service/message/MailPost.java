package net.myconfig.service.message;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.message.Message;
import net.myconfig.service.api.message.MessageChannel;
import net.myconfig.service.model.Ack;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@Profile(MyConfigProfiles.PROD)
public class MailPost extends AbstractMessagePost {

	private final Logger logger = LoggerFactory.getLogger(MailPost.class);

	private final JavaMailSender mailSender;

	@Autowired
	public MailPost(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public boolean supports(MessageChannel channel) {
		return (channel == MessageChannel.EMAIL);
	}

	@Override
	public Ack post(final Message message, final String destination) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			// FIXME Configuration: from email address
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(destination));
				mimeMessage.setFrom(new InternetAddress("mail@mycompany.com"));
				mimeMessage.setSubject(message.getTitle());
				mimeMessage.setText(message.getContent());
			}
		};
		try {
			this.mailSender.send(preparator);
			return Ack.OK;
		} catch (MailException ex) {
			logger.error("[mail] Cannot send mail: {}", ExceptionUtils.getRootCauseMessage(ex));
			return Ack.NOK;
		}
	}

}
