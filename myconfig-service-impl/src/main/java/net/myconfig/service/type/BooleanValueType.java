package net.myconfig.service.type;

import org.springframework.stereotype.Component;

@Component
public class BooleanValueType extends AbstractValueType {

	public BooleanValueType() {
		super("boolean");
	}
	
	@Override
	protected boolean doValidate(String value, String param) {
		return "true".equals(value) || "false".equals(value);
	}

}
