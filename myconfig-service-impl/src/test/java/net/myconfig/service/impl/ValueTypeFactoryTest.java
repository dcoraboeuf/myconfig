package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.myconfig.core.type.ValueType;
import net.myconfig.core.type.ValueTypeDescription;
import net.myconfig.core.type.ValueTypeDescriptions;
import net.myconfig.service.api.type.ValueTypeFactory;
import net.myconfig.service.api.type.ValueTypeNotFoundException;
import net.myconfig.test.AbstractIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ValueTypeFactoryTest extends AbstractIntegrationTest {

	@Autowired
	private ValueTypeFactory factory;

	@Test
	public void types() {
		ValueTypeDescriptions list = factory.getValueTypeDescriptions();
		assertNotNull(list);
		List<ValueTypeDescription> descriptions = list.getDescriptions();
		assertNotNull(descriptions);
		assertTrue(descriptions.size() > 1);
		assertNotNull(Iterables.find(descriptions, new Predicate<ValueTypeDescription>() {
			@Override
			public boolean apply(ValueTypeDescription o) {
				return "plain".equals(o.getId());
			}
		}));
	}

	@Test(expected = ValueTypeNotFoundException.class)
	public void typeNotFound() {
		factory.getValueType("xxx");
	}

	@Test
	public void null_plain() {
		ValueType type = factory.getValueType(null);
		assertNotNull(type);
		assertEquals("plain", type.getId());
	}

	@Test
	public void null_blank() {
		ValueType type = factory.getValueType(" ");
		assertNotNull(type);
		assertEquals("plain", type.getId());
	}

}
