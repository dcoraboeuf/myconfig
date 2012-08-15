package net.myconfig.service.api.message;

public class MessageDestination {

	private final MessageChannel channel;
	private final String destination;

	public MessageDestination(MessageChannel channel, String destination) {
		super();
		this.channel = channel;
		this.destination = destination;
	}

	public MessageChannel getChannel() {
		return channel;
	}

	public String getDestination() {
		return destination;
	}

}
