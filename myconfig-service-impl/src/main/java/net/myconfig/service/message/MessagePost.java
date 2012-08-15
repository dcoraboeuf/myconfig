package net.myconfig.service.message;

import net.myconfig.service.api.message.Message;
import net.myconfig.service.api.message.MessageChannel;
import net.myconfig.service.model.Ack;

public interface MessagePost {
	
	boolean supports (MessageChannel channel);
	
	Ack post (Message message, String destination);

}
