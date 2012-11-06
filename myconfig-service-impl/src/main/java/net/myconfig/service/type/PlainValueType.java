package net.myconfig.service.type;

import net.myconfig.core.InputException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("plain")
public class PlainValueType extends AbstractValueType {

	public PlainValueType() {
		super("plain");
	}

	@Override
	public InputException validate(String value, String param) {
		return null;
	}

}
