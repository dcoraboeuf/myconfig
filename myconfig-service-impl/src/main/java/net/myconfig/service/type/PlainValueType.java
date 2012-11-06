package net.myconfig.service.type;

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
	protected boolean doValidate(String value, String param) {
		return true;
	}

}
