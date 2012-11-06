package net.myconfig.service.type;

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
	protected boolean doValidate(String value, String param) {
		// Gets the integer value
		int i = getInteger(value, param);
		// Gets the range
		Range<Integer> range = getRange(param);
		// Checks the range
		return range.contains(i);
	}

	protected int getInteger(String value, String param) {
		try {
			return Integer.parseInt(value, 10);
		} catch (NumberFormatException ex) {
			throw exception(value, param);
		}
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
