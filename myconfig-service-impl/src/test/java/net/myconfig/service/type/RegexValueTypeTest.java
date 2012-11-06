package net.myconfig.service.type;

import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateNOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateParameterNOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateParameterOK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RegexValueTypeTest {
	
	private static final String PARAM_REASON = "Cannot parse regular expression: \"%s\"";
	private static final String REASON = "\"%s\" must comply with regular expression \"%s\"";
	
	private RegexValueType  type = new RegexValueType();

	@Test
	public void id() {
		assertEquals("regex", type.getId());
	}
	
	@Test
	public void parameter() {
		assertTrue(type.acceptParameter());
	}

	@Test
	public void null_parameter() {
		assertValidateParameterNOK(type, null, PARAM_REASON); 
	}

	@Test
	public void blank_parameter() {
		assertValidateParameterNOK(type, "", PARAM_REASON); 
	}

	@Test
	public void bad_regex_parameter() {
		assertValidateParameterNOK(type, "[\\dA-Z+", PARAM_REASON); 
	}

	@Test
	public void good_regex_parameter() {
		assertValidateParameterOK(type, "[\\dA-Z]+"); 
	}
	
	@Test
	public void validate_novalue_noparam() {
		assertValidateNOK(type, null, null, PARAM_REASON); 
	}

	@Test
	public void validate_novalue_param() {
		assertValidateNOK(type, null, "xxx", REASON); 
	}

	@Test
	public void validate_blankvalue_param() {
		assertValidateNOK(type, "", "xxx", REASON);
	}
	
	@Test
	public void validate_blankvalue_blankparam() {
		assertValidateNOK(type, "", "", PARAM_REASON);
	}

	@Test
	public void validate_value_noparam() {
		assertValidateNOK(type, "zzz", "", String.format(PARAM_REASON, ""));
	}
	
	@Test
	public void validate_value_param_ok() {
		assertValidateOK(type, "123", "\\d+");
	}
	
	@Test
	public void validate_value_param_no_match() {
		assertValidateNOK(type, "12m", "\\d+", REASON);
	}

}
