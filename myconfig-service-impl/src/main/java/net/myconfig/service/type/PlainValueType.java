package net.myconfig.service.type;

import net.myconfig.core.InputException;
import net.myconfig.core.type.ValueType;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier(ValueType.PLAIN)
public class PlainValueType extends AbstractValueType {

	public PlainValueType() {
		super(ValueType.PLAIN);
	}

	@Override
	public InputException validate(String value, String param) {
		return null;
	}

}
