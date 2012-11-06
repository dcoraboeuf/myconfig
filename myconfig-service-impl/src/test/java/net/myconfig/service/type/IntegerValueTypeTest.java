package net.myconfig.service.type;

import static org.junit.Assert.assertEquals;
import net.myconfig.service.exception.ValueTypeValidationException;

import org.junit.Test;

public class IntegerValueTypeTest {
	
	private IntegerValueType  type = new IntegerValueType();

	@Test
	public void id() {
		assertEquals("integer", type.getId());
	}
	
	@Test(expected = ValueTypeValidationException.class)
	public void not_integer_at_all() {
		type.validate("mm", null);
	}
	
	@Test(expected = ValueTypeValidationException.class)
	public void not_integer() {
		type.validate("12m", null);
	}
	
	@Test
	public void integer_not_bounded() {
		type.validate("12", null);
	}
	
	@Test
	public void integer_min_not_bounded() {
		type.validate(String.valueOf(Integer.MIN_VALUE), null);
	}
	
	@Test
	public void integer_max_not_bounded() {
		type.validate(String.valueOf(Integer.MAX_VALUE), null);
	}
	
	@Test
	public void integer_not_bounded_blank_range() {
		type.validate("12", " ");
	}
	
	@Test
	public void integer_max_bounded() {
		type.validate("12", "20");
	}
	
	@Test
	public void integer_max_bounded_limit() {
		type.validate("20", "20");
	}
	
	@Test
	public void integer_max_bounded_one() {
		type.validate("1", "20");
	}

	@Test(expected = ValueTypeValidationException.class)
	public void integer_max_bounded_limit_zero() {
		type.validate("0", "20");
	}

	@Test(expected = ValueTypeValidationException.class)
	public void integer_max_bounded_limit_more() {
		type.validate("21", "20");
	}
	
	@Test
	public void integer_bounded() {
		type.validate("12", "5,20");
	}
	
	@Test
	public void integer_bounded_limit_max() {
		type.validate("20", "5,20");
	}
	
	@Test
	public void integer_bounded_limit_min() {
		type.validate("5", "5,20");
	}

	@Test(expected = ValueTypeValidationException.class)
	public void integer_bounded_limit_more() {
		type.validate("21", "5,20");
	}

	@Test(expected = ValueTypeValidationException.class)
	public void integer_bounded_limit_less() {
		type.validate("4", "5,20");
	}
	
	@Test
	public void integer_negative_bounded() {
		type.validate("-3", "-5,20");
	}

}
