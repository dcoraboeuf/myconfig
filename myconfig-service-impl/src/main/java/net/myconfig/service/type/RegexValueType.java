package net.myconfig.service.type;

import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
public class RegexValueType extends AbstractValueType {

	public RegexValueType() {
		super("regex");
	}
	
	@Override
	protected boolean doValidate(String value, String param) {
		return Pattern.matches(param, ObjectUtils.toString(value, ""));
	}

}
