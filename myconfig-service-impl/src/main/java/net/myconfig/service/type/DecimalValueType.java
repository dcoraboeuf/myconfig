package net.myconfig.service.type;

import java.math.BigDecimal;

import net.myconfig.core.utils.RangeUtils;
import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;

import org.springframework.stereotype.Component;

import com.google.common.collect.Range;

@Component
public class DecimalValueType extends AbstractValueType {

	public DecimalValueType() {
		super("decimal");
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
			return new LocalizableMessage("decimal.badparam", param);
		}
	}

	@Override
	protected boolean doValidate(String value, String param) {
		try {
			// Gets the decimal value
			BigDecimal i = getDecimal(value, param);
			// Gets the range
			Range<BigDecimal> range = getRange(param);
			// Checks the range
			return range.contains(i);
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	protected BigDecimal getDecimal(String value, String param) throws NumberFormatException {
		return new BigDecimal(value);
	}

	protected Range<BigDecimal> getRange(String param) {
		return RangeUtils.parse(param, RangeUtils.DECIMAL);
	}

}
