package net.myconfig.service.type;

import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateNOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateParameterOK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ListValueTypeTest {
	
	private static final String REASON = "\"%s\" must be defined in those values: %s";
	
	private ListValueType  type = new ListValueType();

	@Test
	public void id() {
		assertEquals("list", type.getId());
	}
	
	@Test
	public void parameter() {
		assertTrue(type.acceptParameter());
	}

	@Test
	public void null_parameter() {
		assertValidateParameterOK(type, null); 
	}

	@Test
	public void blank_parameter() {
		assertValidateParameterOK(type, ""); 
	}

	@Test
	public void any_parameter() {
		assertValidateParameterOK(type, "xx"); 
	}

	@Test
	public void list_parameter() {
		assertValidateParameterOK(type, "a,bb,ccc"); 
	}
	
	@Test
	public void validate_novalue_noparam() {
		assertValidateOK(type, null, null); 
	}

	@Test
	public void validate_blankvalue_param() {
		assertValidateNOK(type, "", "xxx", REASON);
	}
	
	@Test
	public void validate_blankvalue_blankparam() {
		assertValidateOK(type, "", "");
	}

	@Test
	public void validate_value_noparam() {
		assertValidateNOK(type, "zzz", "", REASON);
	}
	
	@Test
	public void validate_value_param_ok() {
		assertValidateOK(type, "bb", "a,bb,ccc");
	}
	
	@Test
	public void validate_value_param_no_match() {
		assertValidateNOK(type, "a,bb", "a,bb,ccc", REASON);
	}

}
