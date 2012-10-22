package net.myconfig.service.message;

import java.util.LinkedHashMap;
import java.util.Map;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.Message;
import net.myconfig.service.api.message.MessageChannel;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Collects all the messages in the in-memory box, used for test only
 *
 */
@Component
@Profile(MyConfigProfiles.IT)
public class TestPost extends AbstractMessagePost {
	
	private final Map<String, Message> messages = new LinkedHashMap<String, Message>();

	/**
	 * Supports all channels
	 */
	@Override
	public boolean supports(MessageChannel channel) {
		return true;
	}

	@Override
	public synchronized Ack post(Message message, String destination) {
		messages.put(destination, message);
		return Ack.OK;
	}

}
