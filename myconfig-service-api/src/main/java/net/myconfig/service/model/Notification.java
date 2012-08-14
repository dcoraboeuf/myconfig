package net.myconfig.service.model;

public class Notification {

	private final NotificationType notificationType;
	private final String user;
	private final String title;
	private final String content;

	public Notification(NotificationType notificationType, String user, String title, String content) {
		this.notificationType = notificationType;
		this.user = user;
		this.title = title;
		this.content = content;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public String getUser() {
		return user;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

}
