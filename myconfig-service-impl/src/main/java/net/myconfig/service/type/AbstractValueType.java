package net.myconfig.service.type;

import net.myconfig.core.type.ValueType;

public abstract class AbstractValueType implements ValueType {

	private final String id;

	public AbstractValueType(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
