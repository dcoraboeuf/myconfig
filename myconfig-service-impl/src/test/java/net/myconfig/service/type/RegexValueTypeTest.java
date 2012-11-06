package net.myconfig.service.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import net.myconfig.service.exception.ValueTypeValidationException;

import org.junit.Test;

public class RegexValueTypeTest {
	
	private RegexValueType  type = new RegexValueType();

	@Test
	public void id() {
		assertEquals("regex", type.getId());
	}
	
	@Test(expected = NullPointerException.class)
	public void validate_novalue_noparam() {
		assertNull(type.validate(null, null));
	}

	@Test(expected = ValueTypeValidationException.class)
	public void validate_novalue_param() {
		assertNull(type.validate(null, "xxx"));
	}

	@Test(expected = ValueTypeValidationException.class)
	public void validate_blankvalue_param() {
		assertNull(type.validate("", "xxx"));
	}
	
	@Test
	public void validate_blankvalue_blankparam() {
		assertNull(type.validate("", ""));
	}

	@Test(expected = NullPointerException.class)
	public void validate_value_noparam() {
		assertNull(type.validate("zzz", null));
	}
	
	@Test
	public void validate_value_param_ok() {
		assertNull(type.validate("123", "\\d+"));
	}
	
	@Test(expected = ValueTypeValidationException.class)
	public void validate_value_param_no_match() {
		assertNull(type.validate("12m", "\\d+"));
	}

}
