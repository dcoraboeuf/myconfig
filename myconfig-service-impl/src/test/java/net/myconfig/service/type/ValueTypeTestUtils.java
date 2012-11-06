package net.myconfig.service.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import net.myconfig.core.type.ValueType;
import net.sf.jstring.Localizable;
import net.sf.jstring.Strings;

public final class ValueTypeTestUtils {
	
	public static String NO_PARAMETER_REASON = "No parameter is required.";
	
	private static Strings strings = new Strings();
	
	static {
		strings.add("META-INF.resources.types");
	}
	
	public static void assertValidateOK (ValueType type, String value, String param) {
		assertNull(type.validate(value, param));
	}
	
	public static void assertValidateNOK (ValueType type, String value, String param, String reason) {
		Localizable message = type.validate(value, param);
		assertNotNull(message);
		assertEquals(String.format(reason, value, param), message.getLocalizedMessage(strings, Locale.ENGLISH));
	}
	
	public static void assertValidateParameterNOK (ValueType type, String param, String reason) {
		Localizable message = type.validateParameter(param);
		assertNotNull(message);
		assertEquals(String.format(reason, param), message.getLocalizedMessage(strings, Locale.ENGLISH));
	}
	
	public static void assertValidateParameterOK (ValueType type, String param) {
		Localizable message = type.validateParameter(param);
		assertNull(message);
	}
	
	private ValueTypeTestUtils() {
	}

}
