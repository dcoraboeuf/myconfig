package net.myconfig.web.support;

public class ErrorMessage {

	private final String uuid;
	private final String message;

	public ErrorMessage(String uuid, String message) {
		this.uuid = uuid;
		this.message = message;
	}

	public String getUuid() {
		return uuid;
	}

	public String getMessage() {
		return message;
	}

}
