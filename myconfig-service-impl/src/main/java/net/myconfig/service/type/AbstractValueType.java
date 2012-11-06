package net.myconfig.service.type;

import org.apache.commons.lang3.StringUtils;

import net.myconfig.core.type.ValueType;
import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;

public abstract class AbstractValueType implements ValueType {

	protected static final String NOPARAMETER = "any.noparameter";

	private final String id;

	public AbstractValueType(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public final Localizable validate(String value, String param) {
		// Parameter validation
		Localizable paramValidation = validateParameter(param);
		if (paramValidation != null) {
			return paramValidation;
		}
		// Value validation
		else if (doValidate(value, param)) {
			return null;
		} else {
			return message (value, param);
		}
	}
	
	@Override
	public boolean acceptParameter() {
		return false;
	}
	
	@Override
	public Localizable validateParameter(String param) {
		return validateParameter (StringUtils.isBlank(param), NOPARAMETER);
	}

	protected abstract boolean doValidate(String value, String param);

	protected Localizable message (String value, String param) {
		String key = String.format("%s.validation", getId());
		return new LocalizableMessage(key, value, param);
	}

	protected Localizable validateParameter(boolean test, String key, Object... parameters) {
		if (test) {
			return null;
		} else {
			return new LocalizableMessage(key, parameters);
		}
	}

}
