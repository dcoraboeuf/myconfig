package net.myconfig.service.api;

import net.myconfig.service.model.NotificationType;

public interface NotificationTemplateFactory {

	NotificationTemplate getTemplate(NotificationType notificationType);

}
