package net.myconfig.service.api.message;

import net.myconfig.service.model.Ack;


public interface MessageService {

	Ack sendMessage(Message message, MessageDestination messageDestination);

}
