package net.myconfig.service.api.type;

import net.myconfig.core.type.ValueType;
import net.myconfig.core.type.ValueTypeDescriptions;

public interface ValueTypeFactory {
	
	ValueType getValueType (String id);

	ValueTypeDescriptions getValueTypeDescriptions();

}
