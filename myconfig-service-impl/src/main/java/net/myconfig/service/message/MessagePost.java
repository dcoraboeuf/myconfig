package net.myconfig.service.message;

import net.myconfig.core.model.Ack;
import net.myconfig.service.api.message.Message;
import net.myconfig.service.api.message.MessageChannel;

public interface MessagePost {
	
	boolean supports (MessageChannel channel);
	
	Ack post (Message message, String destination);

}
