package net.myconfig.acc.client

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import net.myconfig.client.*

import org.junit.Test

class ITRestTypeValidation extends AbstractClientUseCase {
	
	// Parameters
	
	@Test
	void validate_param_plainasblank_blank() {
		assert client().typeParameterValidate(Locale.ENGLISH, "", "") == ""
	}
	
	@Test
	void validate_param_plainasblank_nonblank() {
		assert client().typeParameterValidate(Locale.ENGLISH, "", "xxx") == "No parameter is required."
	}
	
	@Test
	void validate_param_plain_blank() {
		assert client().typeParameterValidate(Locale.ENGLISH, "plain", "") == ""
	}
	
	@Test
	void validate_param_plain_nonblank() {
		assert client().typeParameterValidate(Locale.ENGLISH, "plain", "xxx") == "No parameter is required."
	}
	
	@Test
	void validate_param_regex_blank() {
		assert client().typeParameterValidate(Locale.ENGLISH, "regex", "") == 'Cannot parse regular expression: ""'
	}
	
	@Test
	void validate_param_regex_nok() {
		assert client().typeParameterValidate(Locale.ENGLISH, "regex", "[0-9*") == 'Cannot parse regular expression: "[0-9*"'
	}
	
	@Test
	void validate_param_regex_ok() {
		assert client().typeParameterValidate(Locale.ENGLISH, "regex", "[0-9]*") == ""
	}
	
	@Test
	void validate_param_boolean_nonblank() {
		assert client().typeParameterValidate(Locale.ENGLISH, "boolean", "xxx") == 'No parameter is required.'
	}
	
	@Test
	void validate_param_boolean_blank() {
		assert client().typeParameterValidate(Locale.ENGLISH, "boolean", "") == ""
	}
	
	// Values
	
	@Test
	void validate_value_plainasblank_blankparam_blank() {
		assert client().typeValueValidate(Locale.ENGLISH, "", "", "") == ""
	}
	
	@Test
	void validate_value_plainasblank_nonblankparam_blank() {
		assert client().typeValueValidate(Locale.ENGLISH, "", "xxx", "") == "No parameter is required."
	}
	
	@Test
	void validate_value_plain_blankparam_blank() {
		assert client().typeValueValidate(Locale.ENGLISH, "plain", "", "") == ""
	}
	
	@Test
	void validate_value_plain_blankparam_nonblank() {
		assert client().typeValueValidate(Locale.ENGLISH, "plain", "", "xxx") == ""
	}
	
	@Test
	void validate_value_plain_nonblankparam_blank() {
		assert client().typeValueValidate(Locale.ENGLISH, "plain", "xxx", "") == "No parameter is required."
	}
	
	@Test
	void validate_value_regex_blankparam_any() {
		assert client().typeValueValidate(Locale.ENGLISH, "regex", "", "xx") == 'Cannot parse regular expression: ""'
	}
	
	@Test
	void validate_value_regex_paramnok_any() {
		assert client().typeValueValidate(Locale.ENGLISH, "regex", "[0-9*", "xx") == 'Cannot parse regular expression: "[0-9*"'
	}
	
	@Test
	void validate_value_regex_paramok_ok() {
		assert client().typeValueValidate(Locale.ENGLISH, "regex", "[0-9]*", "789") == ""
	}
	
	@Test
	void validate_value_regex_paramok_nok() {
		assert client().typeValueValidate(Locale.ENGLISH, "regex", "[0-9]*", "78m") == '"78m" must comply with regular expression "[0-9]*"'
	}
	
	@Test
	void validate_value_boolean_nonblankparam_any() {
		assert client().typeValueValidate(Locale.ENGLISH, "boolean", "xxx", "xxx") == 'No parameter is required.'
	}
	
	@Test
	void validate_value_boolean_blankparam_true() {
		assert client().typeValueValidate(Locale.ENGLISH, "boolean", "", "true") == ""
	}
	
	@Test
	void validate_value_boolean_blankparam_false() {
		assert client().typeValueValidate(Locale.ENGLISH, "boolean", "", "false") == ""
	}
	
	@Test
	void validate_value_boolean_blankparam_nok() {
		assert client().typeValueValidate(Locale.ENGLISH, "boolean", "", "yes") == '"yes" must be a boolean: true or false'
	}

}
