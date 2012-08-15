package net.myconfig.service.message;

import net.myconfig.service.api.message.MessageChannel;

public abstract class AbstractMessagePost implements MessagePost {

	private final MessageChannel channel;

	public AbstractMessagePost(MessageChannel channel) {
		this.channel = channel;
	}

	@Override
	public boolean supports(MessageChannel channel) {
		return this.channel == channel;
	}

}
