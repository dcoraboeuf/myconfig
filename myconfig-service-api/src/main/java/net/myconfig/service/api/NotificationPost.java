package net.myconfig.service.api;

import net.myconfig.service.model.Notification;

public interface NotificationPost {

	void post(String user, String email, Notification notification);

}
