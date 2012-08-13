package net.myconfig.service.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.EnvGrant;
import net.myconfig.service.api.security.EnvGrantParam;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserGrant;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class HubAccessDecisionManager implements AccessDecisionManager {

	private final Logger logger = LoggerFactory.getLogger(HubAccessDecisionManager.class);

	private final SecuritySelector securitySelector;

	@Autowired
	public HubAccessDecisionManager(SecuritySelector securitySelector) {
		this.securitySelector = securitySelector;
	}

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		logger.debug("[grant] Authorizing {}...", object);
		// Method to authenticate
		MethodInvocation invocation = (MethodInvocation) object;
		// Checks the grants
		if (userGranted(authentication, invocation) || applicationGranted(authentication, invocation) || environmentGranted(authentication, invocation)) {
			logger.debug("[grant] Granted after authorization.");
		}
		// No control - anomaly
		else {
			String accessDeniedMessage = String.format("%s is under control but no access could be granted.", invocation.getMethod());
			throw new AccessDeniedException(accessDeniedMessage);
		}
	}

	protected boolean userGranted(Authentication authentication, MethodInvocation invocation) {
		UserGrant grant = getAnnotation(invocation, UserGrant.class);
		if (grant != null) {
			return checkUserGrant(authentication, grant.value());
		} else {
			return false;
		}
	}

	protected boolean applicationGranted(Authentication authentication, MethodInvocation invocation) {
		AppGrant grant = getAnnotation(invocation, AppGrant.class);
		if (grant != null) {
			int application = (Integer) invocation.getArguments()[0];
			return checkApplicationGrant(authentication, application, grant.value());
		} else {
			return false;
		}
	}

	protected boolean environmentGranted(Authentication authentication, MethodInvocation invocation) {
		EnvGrant grant = getAnnotation(invocation, EnvGrant.class);
		if (grant != null) {
			int application = (Integer) invocation.getArguments()[0];
			Method method = getTargetMethod(invocation);
			String environment = null;
			Annotation[][] allParamAnnotations = method.getParameterAnnotations();
			for (int i = 1; i < invocation.getArguments().length; i++) {
				Class<?> paramType = method.getParameterTypes()[i];
				if (String.class.isAssignableFrom(paramType)) {
					Annotation[] paramAnnotations = allParamAnnotations[i];
					if (paramAnnotations != null) {
						for (Annotation paramAnnotation : paramAnnotations) {
							if (paramAnnotation instanceof EnvGrantParam) {
								if (environment != null) {
									throw new EnvGrantParamAlreadyDefinedException(method.getName());
								}
								environment = (String) invocation.getArguments()[i];
							}
						}
					}
				}
			}
			if (StringUtils.isBlank(environment)) {
				throw new EnvGrantParamMissingException(invocation.getMethod().getName());
			}
			return checkEnvironmentGrant(authentication, application, environment, grant.value());
		} else {
			return false;
		}
	}

	/**
	 * Checks if the current authentication has access to the environment
	 * function for the given application ID and the environment.
	 */
	protected boolean checkEnvironmentGrant(Authentication authentication, int application, String environment, EnvFunction fn) {
		return securitySelector.hasEnvironmentFunction(authentication, application, environment, fn);
	}

	/**
	 * Checks if the current authentication has access to the application
	 * function for the given application ID.
	 */
	protected boolean checkApplicationGrant(Authentication authentication, int application, AppFunction fn) {
		return securitySelector.hasApplicationFunction(authentication, application, fn);
	}

	/**
	 * Checks if the current authentication has access to the user function.
	 */
	protected boolean checkUserGrant(Authentication authentication, UserFunction fn) {
		return securitySelector.hasUserFunction(authentication, fn);
	}

	protected <A extends Annotation> A getAnnotation(MethodInvocation invocation, Class<A> type) {
		Method method = invocation.getMethod();
		A a = method.getAnnotation(type);
		if (a == null) {
			Method targetMethod = getTargetMethod(invocation);
			return targetMethod.getAnnotation(type);
		} else {
			return a;
		}
	}

	protected Method getTargetMethod(MethodInvocation invocation) {
		Object target = invocation.getThis();
		Method method = invocation.getMethod();
		Method targetMethod;
		try {
			targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
		} catch (Exception e) {
			throw new IllegalStateException("Cannot find target method", e);
		}
		return targetMethod;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return (MethodInvocation.class.isAssignableFrom(clazz));
	}

}
