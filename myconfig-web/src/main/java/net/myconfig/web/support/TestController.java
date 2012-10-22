package net.myconfig.web.support;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.Message;
import net.myconfig.service.api.message.MessageChannel;
import net.myconfig.service.api.message.MessagePost;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This controllers is used by the integration tests
 * 
 */
@Controller
@RequestMapping("/test")
@Profile(MyConfigProfiles.IT)
public class TestController implements MessagePost {

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

	/**
	 * Collects the message for a user
	 */
	@RequestMapping("/message/{email:.*}")
	public synchronized @ResponseBody Message message(HttpServletResponse response, @PathVariable String email) throws IOException {
		Message latestMessage = messages.get(email);
		if (latestMessage != null) {
			return latestMessage;
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

}
