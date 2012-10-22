package net.myconfig.service.api.message;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.Message;


public interface MessageService {

	Ack sendMessage(Message message, MessageDestination messageDestination);

}
