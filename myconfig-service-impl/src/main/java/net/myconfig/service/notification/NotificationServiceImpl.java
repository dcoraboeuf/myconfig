package net.myconfig.service.notification;

import java.util.Map;

import net.myconfig.service.api.NotificationBuilder;
import net.myconfig.service.api.NotificationPost;
import net.myconfig.service.api.NotificationService;
import net.myconfig.service.model.Notification;
import net.myconfig.service.model.NotificationType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

	private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	private final NotificationBuilder notificationBuilder;
	private final NotificationPost notificationPost;

	@Autowired
	public NotificationServiceImpl(NotificationBuilder notificationBuilder, @Qualifier("hub") NotificationPost notificationPost) {
		this.notificationBuilder = notificationBuilder;
		this.notificationPost = notificationPost;
	}

	@Override
	public void sendNotification(String user, String displayName, String email, NotificationType notificationType, Map<String, Object> data) {
		logger.info("[notification] Sending notification {} to user {}", notificationType, user);
		// Builds the notification
		Notification notification = notificationBuilder.createNotification(notificationType, user, displayName, data);
		// Sends the notification
		notificationPost.post (user, email, notification);
	}

}
