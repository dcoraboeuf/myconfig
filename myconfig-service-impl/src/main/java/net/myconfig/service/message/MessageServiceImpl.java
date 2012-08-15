package net.myconfig.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.myconfig.service.api.message.Message;
import net.myconfig.service.api.message.MessageChannel;
import net.myconfig.service.api.message.MessageDestination;
import net.myconfig.service.api.message.MessageService;
import net.myconfig.service.model.Ack;

@Service
public class MessageServiceImpl implements MessageService {

	private final List<MessagePost> posts;

	@Autowired
	public MessageServiceImpl(List<MessagePost> posts) {
		this.posts = posts;
	}

	@Override
	public Ack sendMessage(Message message, MessageDestination messageDestination) {
		MessageChannel channel = messageDestination.getChannel();
		String destination = messageDestination.getDestination();
		Ack ack = Ack.NOK;
		for (MessagePost post : posts) {
			if (post.supports(channel)) {
				Ack postAck = post.post(message, destination);
				ack = ack.or(postAck);
			}
		}
		return ack;
	}

}
