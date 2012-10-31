package net.myconfig.service.audit;


import net.myconfig.core.model.Event;
import net.myconfig.core.model.EventCategory;
import net.myconfig.service.api.EventService;

import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

	private final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

	private final ExpressionParser elParser = new SpelExpressionParser();
	private final ParameterNameDiscoverer parameterNameDiscoverer;

	private final EventService eventService;

	@Autowired
	public AuditAspect(EventService eventService) {
		this.eventService = eventService;
		this.parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
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
		EventCategory category = audit.category();
		// Identifier
		String identifier = evaluate(pjp, audit.identifier());
		// Message
		String message = evaluate(pjp, audit.message());
		// Creates the event
		Event event = new Event(category, identifier, message);
		// Records the event
		eventService.saveEvent(event);
		// Returned value
		return retVal;
	}

	protected String evaluate(ProceedingJoinPoint pjp, String expression) {
		// Parsing
		Expression parsedExpression = elParser.parseExpression(expression);
		// Context
		EvaluationContext context = getEvaluationContext(pjp);
		// Evaluation
		Object value = parsedExpression.getValue(context);
		// Conversion to a string
		return ObjectUtils.toString(value, null);
	}

	private EvaluationContext getEvaluationContext(ProceedingJoinPoint pjp) {
		return new ProceedingJoinPointEvaluationContext(pjp, parameterNameDiscoverer);
	}

}