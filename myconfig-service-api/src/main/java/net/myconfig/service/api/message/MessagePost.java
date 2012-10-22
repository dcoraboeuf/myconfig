package net.myconfig.service.api.message;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.Message;

public interface MessagePost {
	
	boolean supports (MessageChannel channel);
	
	Ack post (Message message, String destination);

}
