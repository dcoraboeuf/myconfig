package net.myconfig.service.api;

import net.myconfig.service.model.NotificationTemplateModel;

public interface NotificationTemplate {

	String renderTitle(NotificationTemplateModel model);

	String renderContent(NotificationTemplateModel model);

}
