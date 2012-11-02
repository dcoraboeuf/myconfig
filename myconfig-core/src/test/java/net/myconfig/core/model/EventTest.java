package net.myconfig.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EventTest {
	
	@Test
	public void requiredFields() {
		Event e = new Event(EventCategory.CONFIGURATION, EventAction.CREATE);
		assertEquals(EventCategory.CONFIGURATION, e.getCategory());
		assertEquals(EventAction.CREATE, e.getAction());
		assertNull(e.getIdentifier());
		assertNull(e.getMessage());
		assertNull(e.getApplication());
		assertNull(e.getEnvironment());
		assertNull(e.getVersion());
		assertNull(e.getKey());
		assertNull(e.getTargetUser());
		assertNull(e.getFunction());
	}
	
	@Test
	public void withUser() {
		Event e = new Event(EventCategory.CONFIGURATION, EventAction.CREATE).withTargetUser("myuser");
		assertEquals(EventCategory.CONFIGURATION, e.getCategory());
		assertEquals(EventAction.CREATE, e.getAction());
		assertNull(e.getIdentifier());
		assertNull(e.getMessage());
		assertNull(e.getApplication());
		assertNull(e.getEnvironment());
		assertNull(e.getVersion());
		assertNull(e.getKey());
		assertEquals("myuser", e.getTargetUser());
		assertNull(e.getFunction());
	}

}
