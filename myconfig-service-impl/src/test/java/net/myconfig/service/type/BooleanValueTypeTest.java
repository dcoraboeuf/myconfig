package net.myconfig.service.type;

import static net.myconfig.service.type.ValueTypeTestUtils.NO_PARAMETER_REASON;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateNOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateOK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class BooleanValueTypeTest {
	
	private static final String REASON = "\"%s\" must be a boolean: true or false";
	
	private BooleanValueType  type = new BooleanValueType();

	@Test
	public void id() {
		assertEquals("boolean", type.getId());
	}
	
	@Test
	public void no_parameter() {
		assertFalse(type.acceptParameter());
	}

	@Test
	public void validate_novalue_noparam() {
		assertValidateNOK(type, null, null, REASON);
	}

	@Test
	public void validate_novalue_param() {
		assertValidateNOK(type, null, "xxx", NO_PARAMETER_REASON);
	}

	@Test
	public void validate_blankvalue_param() {
		assertValidateNOK(type, "", "xxx", NO_PARAMETER_REASON);
	}

	@Test
	public void validate_blankvalue_blankparam() {
		assertValidateNOK(type, "", null, REASON);
	}

	@Test
	public void validate_value_noparam() {
		assertValidateNOK(type, "zzz", null, REASON);
	}
	
	@Test
	public void validate_value_param_true() {
		assertValidateOK(type, "true", null);
	}
	
	@Test
	public void validate_value_param_false() {
		assertValidateOK(type, "false", null);
	}

}
