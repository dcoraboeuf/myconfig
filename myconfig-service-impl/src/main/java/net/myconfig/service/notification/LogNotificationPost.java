package net.myconfig.service.notification;

import net.myconfig.service.api.NotificationPost;
import net.myconfig.service.model.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogNotificationPost implements NotificationPost {

	private final Logger logger = LoggerFactory.getLogger(LogNotificationPost.class);

	@Override
	public void post(String user, String email, Notification notification) {
		logger.info("[notification] User '{}' receives following notification:\ntitle: {}\ncontent: {}", new Object[] {
				user,
				notification.getTitle(),
				notification.getContent()
		});
	}

}
