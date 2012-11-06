package net.myconfig.service.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import net.myconfig.service.exception.ValueTypeValidationException;

import org.junit.Test;

public class BooleanValueTypeTest {
	
	private BooleanValueType  type = new BooleanValueType();

	@Test
	public void id() {
		assertEquals("boolean", type.getId());
	}

	@Test(expected = ValueTypeValidationException.class)
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

	@Test(expected = ValueTypeValidationException.class)
	public void validate_blankvalue_blankparam() {
		assertNull(type.validate("", ""));
	}

	@Test(expected = ValueTypeValidationException.class)
	public void validate_value_noparam() {
		assertNull(type.validate("zzz", null));
	}
	
	@Test
	public void validate_value_param_true() {
		assertNull(type.validate("true", ""));
	}
	
	@Test
	public void validate_value_param_false() {
		assertNull(type.validate("false", ""));
	}

}
