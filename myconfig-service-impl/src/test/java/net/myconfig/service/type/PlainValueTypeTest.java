package net.myconfig.service.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PlainValueTypeTest {
	
	private PlainValueType type = new PlainValueType();

	@Test
	public void id() {
		assertEquals("plain", type.getId());
	}
	
	@Test
	public void validate_novalue_noparam() {
		assertNull(type.validate(null, null));
	}
	
	@Test
	public void validate_novalue_param() {
		assertNull(type.validate(null, "xxx"));
	}
	
	@Test
	public void validate_value_param() {
		assertNull(type.validate("zzz", "xxx"));
	}
	
	@Test
	public void validate_value_noparam() {
		assertNull(type.validate("zzz", null));
	}

}
