package net.myconfig.service.audit;

import static java.util.Arrays.asList;
import static net.myconfig.core.model.EventAction.CREATE;
import static net.myconfig.core.model.EventAction.UPDATE;
import static net.myconfig.core.model.EventCategory.CONFIGURATION;
import static net.myconfig.core.model.EventCategory.USER_FUNCTION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.Event;
import net.myconfig.core.model.Version;
import net.myconfig.service.api.EventService;
import net.myconfig.service.exception.ApplicationNotFoundException;
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
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE).withIdentifier("10"));
	}

	@Test
	public void identifierAndMessage() {
		proxy.identifierAndMessage(10, "My message");
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE).withIdentifier("10").withMessage("My message"));
	}

	@Test
	public void identifierAndEmptyMessage() {
		proxy.identifierAndMessage(10, "");
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE).withIdentifier("10").withMessage(""));
	}

	@Test
	public void identifierAndNullMessage() {
		proxy.identifierAndMessage(10, null);
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE).withIdentifier("10"));
	}

	@Test
	public void identifierAndResultOk() {
		Ack ack = proxy.identifierAndResult(2);
		assertTrue(ack.isSuccess());
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE).withIdentifier("2"));
	}

	@Test
	public void identifierAndResultNotOk() {
		Ack ack = proxy.identifierAndResult(3);
		assertFalse(ack.isSuccess());
		verify(eventService, never()).saveEvent(any(Event.class));
	}

	@Test
	public void allKeys() {
		proxy.allKeys("AA", "myenv", "myversion", "mykey");
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE).withApplication("AA").withEnvironment("myenv").withVersion("myversion").withKey("mykey"));
	}
	
	@Test(expected = IllegalStateException.class)
	public void expressionMismatch() {
		proxy.expressionMismatch("AA");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void withException() {
		proxy.withException("AA");
	}
	
	@Test
	public void withCollection() {
		Ack ack = proxy.withCollection("AA", new CollectionItems(asList(new Version("1"), new Version("2"))));
		assertTrue(ack.isSuccess());
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE).withApplication("AA").withVersion("1"));
		verify(eventService, times(1)).saveEvent(new Event(CONFIGURATION, UPDATE).withApplication("AA").withVersion("2"));
	}
	
	@Test
	public void userAndFunctionOnly() {
		Ack ack = proxy.userAndFunctionOnly("myuser", UserFunction.app_create);
		assertTrue(ack.isSuccess());
		verify(eventService, times(1)).saveEvent(new Event(USER_FUNCTION, CREATE).withTargetUser("myuser").withFunction(UserFunction.app_create.name()));
	}

}
