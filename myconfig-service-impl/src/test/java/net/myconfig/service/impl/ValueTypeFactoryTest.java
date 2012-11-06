package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.myconfig.core.type.ValueType;
import net.myconfig.service.api.type.ValueTypeFactory;
import net.myconfig.service.api.type.ValueTypeNotFoundException;
import net.myconfig.test.AbstractIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ValueTypeFactoryTest extends AbstractIntegrationTest {

	@Autowired
	private ValueTypeFactory factory;

	@Test(expected = ValueTypeNotFoundException.class)
	public void typeNotFound() {
		factory.getValueType("xxx");
	}

	@Test
	public void null_plain() {
		ValueType<Object> type = factory.getValueType(null);
		assertNotNull(type);
		assertEquals("plain", type.getId());
	}

	@Test
	public void null_blank() {
		ValueType<Object> type = factory.getValueType(" ");
		assertNotNull(type);
		assertEquals("plain", type.getId());
	}

}
