package net.myconfig.service.notification;

import java.util.Map;

import net.myconfig.service.api.NotificationBuilder;
import net.myconfig.service.model.Notification;
import net.myconfig.service.model.NotificationType;

import org.springframework.stereotype.Service;

@Service
public class NotificationBuilderImpl implements NotificationBuilder {

	@Override
	public Notification createNotification(NotificationType notificationType, String user, String userDisplayName, Map<String, Object> data) {
		// TODO Gets a template for this notification type
		// TODO Creates the model
		// TODO Renders the title
		String title = "Hey " + userDisplayName;
		// TODO Renders the content
		String content = "Here is for " + user + ".";
		// OK
		return new Notification(notificationType, user, title, content);
	}

}
