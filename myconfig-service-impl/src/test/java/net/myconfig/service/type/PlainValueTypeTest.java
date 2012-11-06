package net.myconfig.service.type;

import static net.myconfig.service.type.ValueTypeTestUtils.NO_PARAMETER_REASON;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateNOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateOK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PlainValueTypeTest {
	
	private PlainValueType type = new PlainValueType();

	@Test
	public void id() {
		assertEquals("plain", type.getId());
	}
	
	@Test
	public void no_parameter() {
		assertFalse(type.acceptParameter());
	}
	
	@Test
	public void validate_novalue_noparam() {
		assertValidateOK(type, null, null);
	}
	
	@Test
	public void validate_novalue_param() {
		assertValidateNOK(type, null, "xxx", NO_PARAMETER_REASON);
	}
	
	@Test
	public void validate_value_param() {
		assertValidateNOK(type, "zzz", "xxx", "No parameter is required.");
	}
	
	@Test
	public void validate_value_noparam() {
		assertValidateOK(type, "zzz", null);
	}

}
