package net.myconfig.service.audit;

import static net.myconfig.core.model.EventAction.UPDATE;
import static net.myconfig.core.model.EventCategory.CONFIGURATION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

}
