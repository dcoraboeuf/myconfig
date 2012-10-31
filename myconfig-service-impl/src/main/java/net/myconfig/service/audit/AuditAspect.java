package net.myconfig.service.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {
	
	private final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

	/**
	 * Any method in <code>net.myconfig.service.impl</code> package annotated
	 * with the {@link Audit} annotation.
	 */
	@Pointcut("execution(@net.myconfig.service.audit.Audit * net.myconfig.service.impl.*.*(..))")
	public void auditedMethod() {
	}

	@Around("auditedMethod()")
	public Object audit(ProceedingJoinPoint pjp) throws Throwable {
		logger.debug("[audit] Audit for {}", pjp);
		// Call
		Object retVal = pjp.proceed();
		// Returned value
		return retVal;
	}

}
