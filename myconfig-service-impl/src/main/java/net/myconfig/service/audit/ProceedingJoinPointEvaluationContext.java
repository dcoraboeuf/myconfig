package net.myconfig.service.audit;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * See
 * <code>org.springframework.security.access.expression.method.MethodSecurityEvaluationContext</code>
 */
public class ProceedingJoinPointEvaluationContext extends StandardEvaluationContext {

	private static final Logger logger = LoggerFactory.getLogger(ProceedingJoinPointEvaluationContext.class);

	private final ParameterNameDiscoverer parameterNameDiscoverer;
	private final ProceedingJoinPoint pjp;

	private boolean argumentsAdded;

	public ProceedingJoinPointEvaluationContext(ProceedingJoinPoint pjp, ParameterNameDiscoverer parameterNameDiscoverer) {
		this.pjp = pjp;
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}

	@Override
	public Object lookupVariable(String name) {
		Object variable = super.lookupVariable(name);

		if (variable != null) {
			return variable;
		}

		if (!argumentsAdded) {
			addArgumentsAsVariables();
			argumentsAdded = true;
		}

		variable = super.lookupVariable(name);

		if (variable != null) {
			return variable;
		}

		return null;
	}

	private void addArgumentsAsVariables() {
		Object[] args = pjp.getArgs();

		if (args.length == 0) {
			return;
		}

		Object targetObject = pjp.getThis();
		// SEC-1454
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(targetObject);

		if (targetClass == null) {
			targetClass = targetObject.getClass();
		}

		Method pjpMethod = ((MethodSignature) pjp.getSignature()).getMethod();
		Method method = AopUtils.getMostSpecificMethod(pjpMethod, targetClass);
		String[] paramNames = parameterNameDiscoverer.getParameterNames(method);

		if (paramNames == null) {
			logger.warn("Unable to resolve method parameter names for method: " + method + ". Debug symbol information is required if you are using parameter names in expressions.");
			return;
		}

		for (int i = 0; i < args.length; i++) {
			super.setVariable(paramNames[i], args[i]);
		}
	}

}