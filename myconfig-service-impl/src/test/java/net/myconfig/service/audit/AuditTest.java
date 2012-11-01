package net.myconfig.service.audit;

import static net.myconfig.core.model.EventAction.UPDATE;
import static net.myconfig.core.model.EventCategory.CONFIGURATION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.Event;
import net.myconfig.service.api.EventService;
import net.myconfig.service.impl.AuditedImpl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class AuditTest {

	private EventService eventService;
	private AuditedInterface proxy;

	@Before
	public void before() {
		AuditedInterface audited = new AuditedImpl();
		AspectJProxyFactory factory = new AspectJProxyFactory(audited);
		eventService = mock(EventService.class);
		AuditAspect aspect = new AuditAspect(eventService);
		factory.addAspect(aspect);
		proxy = factory.getProxy();
	}

	@Test
	public void identifierOnly() {
		proxy.identifierOnly(10);
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE, "10", null));
	}

	@Test
	public void identifierAndMessage() {
		proxy.identifierAndMessage(10, "My message");
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE, "10", "[10] My message"));
	}

	@Test
	public void identifierAndResultOk() {
		Ack ack = proxy.identifierAndResult(2);
		assertTrue(ack.isSuccess());
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE, "2", null));
	}

	@Test
	public void identifierAndResultNotOk() {
		Ack ack = proxy.identifierAndResult(3);
		assertFalse(ack.isSuccess());
		verify(eventService, never()).saveEvent(any(Event.class));
	}

	@Test
	public void allKeys() {
		proxy.allKeys(10, "myenv", "myversion", "mykey");
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE, "10", "myenv", "myversion", "mykey", null));
	}

}
