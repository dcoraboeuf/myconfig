package net.myconfig.service.impl;

import java.util.Collection;
import java.util.Map;

import net.myconfig.core.type.ValueType;
import net.myconfig.service.api.type.ValueTypeFactory;
import net.myconfig.service.api.type.ValueTypeNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

@Service
public class DefaultValueTypeFactory implements ValueTypeFactory {

	private final Map<String, ValueType> index;

	@Autowired
	public DefaultValueTypeFactory(Collection<ValueType> types) {
		index = Maps.uniqueIndex(types, new Function<ValueType, String>() {
			@Override
			public String apply(ValueType type) {
				return type.getId();
			}
		});
	}

	@Override
	public  ValueType getValueType(String id) {
		if (StringUtils.isBlank(id)) {
			return getValueType("plain");
		} else {
			ValueType type = index.get(id);
			if (type != null) {
				return type;
			} else {
				throw new ValueTypeNotFoundException(id);
			}
		}
	}

}
