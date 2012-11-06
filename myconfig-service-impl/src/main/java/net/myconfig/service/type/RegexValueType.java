package net.myconfig.service.type;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
	
	@Override
	public boolean acceptParameter() {
		return true;
	}
	
	@Override
	public Localizable validateParameter(String param) {
		LocalizableMessage message = new LocalizableMessage("regex.badparam", param);
		if (StringUtils.isBlank(param)) {
			return message;
		} else {
			// Tries to parse the regex
			try {
				Pattern.compile(param);
				return null;
			} catch (PatternSyntaxException ex) {
				return message;
			}
		}
	}

}
