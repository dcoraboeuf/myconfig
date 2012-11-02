package net.myconfig.service.audit;


import java.util.Collection;
import java.util.Collections;

import net.myconfig.core.model.Event;
import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.service.api.EventService;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

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
		// Regular call
		Object retVal = pjp.proceed();
		// Expression evaluation context
		EvaluationContext evaluationContext = getEvaluationContext(pjp);
		// If the result must be evaluated for success
		String result = audit.result();
		if (StringUtils.isNotBlank(result)) {
			boolean resultOK = evaluateResult (result, retVal, evaluationContext);
			if (!resultOK) {
				// No need to register any event
				return retVal;
			}
		}
		// OK
		logger.debug("[audit] {} audit for {}", audit, pjp);
		// Category & action
		EventCategory category = audit.category();
		EventAction action = audit.action();
		// Message
		String message = evaluate(audit.message(), evaluationContext, false);
		// Gets the list of events
		Collection<Event> events = getEvents(audit, evaluationContext, category, action, message);
		// Records the event
		for (Event event : events) {
			eventService.saveEvent(event);
		}
		// Returned value
		return retVal;
	}

	protected Collection<Event> getEvents(final Audit audit, final EvaluationContext evaluationContext, final EventCategory category, final EventAction action, final String message) {
		// Collection?
		String collection = audit.collection();
		if (StringUtils.isNotBlank(collection)) {
			// Evaluate the collection
			Collection<?> items = (Collection<?>) elParser.parseExpression(collection).getValue(evaluationContext);
			// Converts the collection
			return Collections2.transform(items, new Function<Object, Event>() {

				@Override
				public Event apply(Object item) {
					evaluationContext.setVariable("item", item);
					return getSingleEvent(audit, evaluationContext, category, action, message);
				}
			});
		}
		// Single event
		else {
			Event event = getSingleEvent(audit, evaluationContext, category, action, message);
			return Collections.singleton(event);
		}
	}

	protected Event getSingleEvent(Audit audit, EvaluationContext evaluationContext, EventCategory category, EventAction action, String message) {
		Event event;
		// Identifier OR Application
		String identifierExpression = audit.identifier();
		if (StringUtils.isNotBlank(identifierExpression)) {
			String identifier = evaluate(identifierExpression, evaluationContext);
			// Creates the event
			event = new Event(category, action, identifier, message);
		} else {
			// Keys
			String application = evaluate(audit.application(), evaluationContext);
			String environment = evaluate(audit.environment(), evaluationContext);
			String version = evaluate(audit.version(), evaluationContext);
			String key = evaluate(audit.key(), evaluationContext);
			// Creates the event
			event = new Event(category, action, application, environment, version, key, message);
		}
		// User
		String userExpression = audit.user();
		if (StringUtils.isNotBlank(userExpression)) {
			String user = evaluate(userExpression, evaluationContext);
			event = event.withUser(user);
		}
		// Function
		String fnExpression = audit.function();
		if (StringUtils.isNotBlank(fnExpression)) {
			String fn = evaluate(fnExpression, evaluationContext);
			event = event.withFunction(fn);
		}
		// OK
		return event;
	}

	protected boolean evaluateResult(String result, Object retVal, EvaluationContext evaluationContext) {
		// Parsing
		Expression resultExpression = elParser.parseExpression(result);
		// Context
		evaluationContext.setVariable("result", retVal);
		// Evaluation
		return (Boolean) resultExpression.getValue(evaluationContext);
	}

	protected String evaluate(String expression, EvaluationContext evaluationContext) {
		return evaluate (expression, evaluationContext, true);
	}

	protected String evaluate(String expression, EvaluationContext evaluationContext, boolean required) {
		if (StringUtils.isBlank(expression)) {
			return null;
		}
		// Parsing
		Expression parsedExpression = elParser.parseExpression(expression);
		// Evaluation
		Object value = parsedExpression.getValue(evaluationContext);
		String sValue = ObjectUtils.toString(value, null);
		// Checks for empty string
		if (required && StringUtils.isBlank(sValue)) {
			throw new IllegalStateException(String.format("Audit expression returned blank or null: %s", sValue));
		}
		// Conversion to a string
		return sValue;
	}

	protected EvaluationContext getEvaluationContext(ProceedingJoinPoint pjp) {
		return new ProceedingJoinPointEvaluationContext(pjp, parameterNameDiscoverer);
	}

}