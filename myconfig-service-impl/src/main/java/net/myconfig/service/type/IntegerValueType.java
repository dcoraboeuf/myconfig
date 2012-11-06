package net.myconfig.service.type;

import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

@Component
public class IntegerValueType extends AbstractValueType {

	private static final String SEPARATOR = ",";

	public IntegerValueType() {
		super("integer");
	}

	@Override
	public boolean acceptParameter() {
		return true;
	}

	@Override
	public Localizable validateParameter(String param) {
		try {
			getRange(param);
			return null;
		} catch (NumberFormatException ex) {
			return new LocalizableMessage("integer.badparam", param);
		}
	}

	@Override
	protected boolean doValidate(String value, String param) {
		try {
			// Gets the integer value
			int i = getInteger(value, param);
			// Gets the range
			Range<Integer> range = getRange(param);
			// Checks the range
			return range.contains(i);
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	protected int getInteger(String value, String param) throws NumberFormatException {
		return Integer.parseInt(value, 10);
	}

	protected Range<Integer> getRange(String param) {
		if (StringUtils.isBlank(param)) {
			return Ranges.all();
		} else if (!StringUtils.contains(param, SEPARATOR)) {
			return Ranges.closed(1, getInteger(param, param));
		} else {
			int a = getInteger(StringUtils.substringBefore(param, SEPARATOR), param);
			int b = getInteger(StringUtils.substringAfter(param, SEPARATOR), param);
			return Ranges.closed(a, b);
		}
	}

}
