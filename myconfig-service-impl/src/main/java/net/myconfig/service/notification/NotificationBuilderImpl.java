package net.myconfig.service.notification;

import java.util.Map;

import net.myconfig.service.api.NotificationBuilder;
import net.myconfig.service.api.NotificationTemplate;
import net.myconfig.service.api.NotificationTemplateFactory;
import net.myconfig.service.model.Notification;
import net.myconfig.service.model.NotificationTemplateModel;
import net.myconfig.service.model.NotificationType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationBuilderImpl implements NotificationBuilder {

	private final NotificationTemplateFactory notificationTemplateFactory;

	@Autowired
	public NotificationBuilderImpl(NotificationTemplateFactory notificationTemplateFactory) {
		this.notificationTemplateFactory = notificationTemplateFactory;
	}

	@Override
	public Notification createNotification(NotificationType notificationType, String user, String userDisplayName, Map<String, Object> data) {
		// Gets a template for this notification type
		NotificationTemplate template = notificationTemplateFactory.getTemplate(notificationType);
		// Creates the model
		NotificationTemplateModel model = new NotificationTemplateModel().add("user", user).add("name", userDisplayName).add("data", data);
		// Renders the title
		String title = template.renderTitle(model);
		// Renders the content
		String content = template.renderContent(model);
		// OK
		return new Notification(notificationType, user, title, content);
	}

}
