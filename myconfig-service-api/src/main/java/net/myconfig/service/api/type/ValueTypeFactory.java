package net.myconfig.service.api.type;

import net.myconfig.core.type.ValueType;

public interface ValueTypeFactory {
	
	<T> ValueType<T> getValueType (String id);

}
