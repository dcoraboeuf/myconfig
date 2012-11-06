package net.myconfig.service.type;

import net.myconfig.core.InputException;
import net.myconfig.core.type.ValueType;
import net.myconfig.service.exception.ValueTypeValidationException;

public abstract class AbstractValueType implements ValueType {

	private final String id;

	public AbstractValueType(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public final InputException validate(String value, String param) {
		if (doValidate(value, param)) {
			return null;
		} else {
			throw exception (value, param);
		}
	}

	protected abstract boolean doValidate(String value, String param);

	protected ValueTypeValidationException exception (String value, String param) {
		return new ValueTypeValidationException (String.format("%s.validation", getId()), value, param);
	}

}
