package net.myconfig.service.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class TrimmedValidatorTest {

	private final TrimmedValidator validator = new TrimmedValidator();

	@Test
	public void init() {
		Trimmed annotation = mock(Trimmed.class);
		validator.initialize(annotation);
	}

	@Test
	public void value_null() {
		assertTrue(validator.isValid(null, null));
	}

	@Test
	public void value_empty() {
		assertTrue(validator.isValid("", null));
	}

	@Test
	public void value_blank_one() {
		assertTrue(validator.isValid(" ", null));
	}

	@Test
	public void value_blank_multi() {
		assertTrue(validator.isValid("   ", null));
	}

	@Test
	public void value_left() {
		assertFalse(validator.isValid("   test", null));
	}

	@Test
	public void value_right() {
		assertFalse(validator.isValid("test  ", null));
	}

	@Test
	public void value_both() {
		assertFalse(validator.isValid("   test  ", null));
	}

	@Test
	public void value_ok() {
		assertTrue(validator.isValid("test", null));
	}

}
