package net.myconfig.service.notification;

import java.util.List;

import net.myconfig.service.api.NotificationPost;
import net.myconfig.service.model.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("hub")
public class HubNotificationPost implements NotificationPost {

	private final Logger logger = LoggerFactory.getLogger(HubNotificationPost.class);

	private final List<NotificationPost> notificationPosts;

	@Autowired
	public HubNotificationPost(List<NotificationPost> notificationPosts) {
		this.notificationPosts = notificationPosts;
	}

	@Override
	public void post(String user, String email, Notification notification) {
		logger.info("[notification] Sending {} to user {}", notification, user);
		for (NotificationPost notificationPost : notificationPosts) {
			notificationPost.post(user, email, notification);
		}
	}

}
