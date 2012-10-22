package net.myconfig.service.message;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.Message;
import net.myconfig.service.api.message.MessageChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({ MyConfigProfiles.DEV, MyConfigProfiles.IT, MyConfigProfiles.TEST })
public class LogPost extends AbstractMessagePost {

	private final Logger logger = LoggerFactory.getLogger(LogPost.class);

	@Override
	public boolean supports(MessageChannel channel) {
		return true;
	}

	@Override
	public Ack post(Message message, String destination) {
		logger.info("[message] Sending message to '{}':\n-----------------\n{}\n\n{}\n-----------------\n", new Object[] { destination, message.getTitle(), message.getContent() });
		return Ack.OK;
	}

}
