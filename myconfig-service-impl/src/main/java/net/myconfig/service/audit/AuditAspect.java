package net.myconfig.service.audit;

import net.myconfig.core.model.Event;
import net.myconfig.core.model.EventCategory;
import net.myconfig.service.api.EventService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {
	
	private final Logger logger = LoggerFactory.getLogger(AuditAspect.class);
	
	private final EventService eventService;
	
	@Autowired
	public AuditAspect(EventService eventService) {
		this.eventService = eventService;
	}

	/**
	 * Any method in <code>net.myconfig.service.impl</code> package annotated
	 * with the {@link Audit} annotation.
	 */
	@Pointcut("execution(@net.myconfig.service.audit.Audit * net.myconfig.service.impl.*.*(..))")
	public void auditedMethod() {
	}

	@Around("auditedMethod() && @annotation(audit)")
	public Object audit(ProceedingJoinPoint pjp, Audit audit) throws Throwable {
		logger.debug("[audit] {} audit for {}", audit, pjp);
		// Regular call
		Object retVal = pjp.proceed();
		// TODO If the result must be evaluated for success
		// Category
		EventCategory category = audit.value();
		// TODO Identifier
		String identifier = "";
		// TODO Message
		String message = "";
		// Creates the event
		Event event = new Event(category, identifier, message);
		// Records the event
		eventService.saveEvent(event);
		// Returned value
		return retVal;
	}

}
