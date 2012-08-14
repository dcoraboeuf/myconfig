package net.myconfig.service.model;

import java.util.HashMap;
import java.util.Map;

public class NotificationTemplateModel {

	private final Map<String, Object> map = new HashMap<String, Object>();

	public NotificationTemplateModel add(String name, Object value) {
		map.put(name, value);
		return this;
	}

}
