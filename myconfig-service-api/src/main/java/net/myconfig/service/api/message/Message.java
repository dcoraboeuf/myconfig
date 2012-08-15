package net.myconfig.service.api.message;


public class Message {

	private final String title;
	private final String content;

	public Message(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

}
