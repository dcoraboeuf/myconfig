package net.myconfig.service.api;

import java.util.Map;

import net.myconfig.service.model.NotificationType;

public interface NotificationService {

	void sendNotification(String user, String userDisplayName, String email, NotificationType notificationType, Map<String, Object> data);

}
