package net.myconfig.service.type;

import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateNOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateParameterNOK;
import static net.myconfig.service.type.ValueTypeTestUtils.assertValidateParameterOK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DecimalValueTypeTest {
	
	private static final String PARAM_REASON = "\"%s\" does not define a decimal range";
	private static final String REASON = "\"%s\" must be a decimal in the %s range";
	
	private DecimalValueType  type = new DecimalValueType();

	@Test
	public void id() {
		assertEquals("decimal", type.getId());
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
	public void unique_parameter() {
		assertValidateParameterOK(type, "12"); 
	}

	@Test
	public void range_at_most() {
		assertValidateParameterOK(type, "..20"); 
		assertValidateParameterOK(type, "..20)"); 
		assertValidateParameterOK(type, "..20]"); 
	}

	@Test
	public void range_less_than() {
		assertValidateParameterOK(type, "..20["); 
		assertValidateParameterOK(type, "..20("); 
	}

	@Test
	public void range_at_least() {
		assertValidateParameterOK(type, "10.."); 
		assertValidateParameterOK(type, "(10.."); 
		assertValidateParameterOK(type, "[10.."); 
	}

	@Test
	public void range_greater_than() {
		assertValidateParameterOK(type, "]10.."); 
		assertValidateParameterOK(type, ")10.."); 
	}

	@Test
	public void range_closed_closed() {
		assertValidateParameterOK(type, "10..20"); 
		assertValidateParameterOK(type, "[10..20]"); 
	}

	@Test
	public void negative_parameters() {
		assertValidateParameterOK(type, "-5..20"); 
	}

	@Test
	public void invalid_unique_parameter() {
		assertValidateParameterNOK(type, "12m", PARAM_REASON); 
	}

	@Test
	public void invalid_range_parameter() {
		assertValidateParameterNOK(type, "0,12m", PARAM_REASON); 
	}
	
	public void not_decimal_at_all() {
		assertValidateNOK(type, "mm", null, REASON);
	}
	
	@Test
	public void not_decimal() {
		assertValidateNOK(type, "12m", null, REASON);
	}
	
	@Test
	public void decimal_not_bounded() {
		assertValidateOK(type, "12", null);
	}
	
	@Test
	public void decimal_min_not_bounded() {
		assertValidateOK(type, String.valueOf(Integer.MIN_VALUE), null);
	}
	
	@Test
	public void decimal_max_not_bounded() {
		assertValidateOK(type, String.valueOf(Integer.MAX_VALUE), null);
	}
	
	@Test
	public void decimal_not_bounded_blank_range() {
		assertValidateOK(type, "12", "");
	}
	
	@Test
	public void decimal_max_bounded() {
		assertValidateOK(type, "12", "20");
	}
	
	@Test
	public void decimal_max_bounded_limit() {
		assertValidateOK(type, "20", "20");
	}
	
	@Test
	public void decimal_max_bounded_one() {
		assertValidateOK(type, "1", "20");
	}

	@Test
	public void decimal_max_bounded_limit_zero() {
		assertValidateNOK(type, "0", "]0..20", REASON);
	}

	@Test
	public void decimal_max_bounded_limit_more() {
		assertValidateNOK(type, "21", "20", REASON);
	}
	
	@Test
	public void decimal_bounded() {
		assertValidateOK(type, "12", "5..20");
	}
	
	@Test
	public void decimal_bounded_limit_max() {
		assertValidateOK(type, "20", "5..20");
	}
	
	@Test
	public void decimal_bounded_limit_min() {
		assertValidateOK(type, "5", "5..20");
	}

	@Test
	public void decimal_bounded_limit_more() {
		assertValidateNOK(type, "21", "5..20", REASON);
	}

	@Test
	public void decimal_bounded_limit_less() {
		assertValidateNOK(type, "4", "5..20", REASON);
	}
	
	@Test
	public void decimal_negative_bounded() {
		assertValidateOK(type, "-3", "-5..20");
	}
	
	@Test
	public void decimal_closed() {
		assertValidateOK(type, "10.1", "0..10.1");
	}
	
	@Test
	public void decimal_open() {
		assertValidateNOK(type, "10.1", "0..10.1(", REASON);
	}

}
