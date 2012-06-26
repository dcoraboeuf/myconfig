package net.myconfig.service.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class NotBlankValidatorTest {

	private final NotBlankValidator validator = new NotBlankValidator();

	@Test
	public void init() {
		NotBlank annotation = mock(NotBlank.class);
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
		assertFalse(validator.isValid(" ", null));
	}

	@Test
	public void value_blank_multi() {
		assertFalse(validator.isValid("   ", null));
	}

	@Test
	public void value_not_blank() {
		assertTrue(validator.isValid("   test ", null));
	}

}
