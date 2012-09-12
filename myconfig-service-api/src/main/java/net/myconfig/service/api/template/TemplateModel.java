package net.myconfig.service.api.template;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class TemplateModel {

	private final Map<String, Object> map = new HashMap<String, Object>();

	public TemplateModel add(String name, Object value) {
		map.put(name, value);
		return this;
	}

	public Map<String, Object> toMap() {
		return ImmutableMap.copyOf(map);
	}

}
