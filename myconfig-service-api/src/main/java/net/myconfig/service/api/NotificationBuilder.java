package net.myconfig.service.api;

import java.util.Map;

import net.myconfig.service.model.Notification;
import net.myconfig.service.model.NotificationType;

public interface NotificationBuilder {

	Notification createNotification(NotificationType notificationType, String user, String userDisplayName, Map<String, Object> data);

}
