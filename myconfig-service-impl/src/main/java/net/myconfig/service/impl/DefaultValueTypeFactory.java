package net.myconfig.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.myconfig.core.type.ValueType;
import net.myconfig.core.type.ValueTypeDescription;
import net.myconfig.core.type.ValueTypeDescriptions;
import net.myconfig.service.api.type.ValueTypeFactory;
import net.myconfig.service.api.type.ValueTypeNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

@Service
public class DefaultValueTypeFactory implements ValueTypeFactory {

	private final Map<String, ValueType> index;

	@Autowired
	public DefaultValueTypeFactory(Collection<ValueType> types) {
		Map<String, ValueType> initialIndex = new HashMap<String, ValueType>(Maps.uniqueIndex(types, new Function<ValueType, String>() {
			@Override
			public String apply(ValueType type) {
				return type.getId();
			}
		}));
		// Plain type
		ValueType plainType = initialIndex.get(ValueType.PLAIN);
		if (plainType == null) {
			throw new IllegalStateException("'plain' value type must be defined");
		}
		initialIndex.remove(ValueType.PLAIN);
		// Result
		Map<String, ValueType> result = new LinkedHashMap<String, ValueType>();
		// Makes sure 'plain' is first
		result.put(plainType.getId(), plainType);
		// Adds the result
		result.putAll(initialIndex);
		// OK
		index = ImmutableMap.copyOf(result);
	}

	@Override
	public  ValueType getValueType(String id) {
		if (StringUtils.isBlank(id)) {
			return getValueType(ValueType.PLAIN);
		} else {
			ValueType type = index.get(id);
			if (type != null) {
				return type;
			} else {
				throw new ValueTypeNotFoundException(id);
			}
		}
	}
	
	@Override
	public ValueTypeDescriptions getValueTypeDescriptions() {
		return new ValueTypeDescriptions(Collections2.transform(index.values(), new Function<ValueType, ValueTypeDescription>() {
			@Override
			public ValueTypeDescription apply (ValueType type) {
				return new ValueTypeDescription(type.getId(), type.acceptParameter());
			}
		}));
	}

}
