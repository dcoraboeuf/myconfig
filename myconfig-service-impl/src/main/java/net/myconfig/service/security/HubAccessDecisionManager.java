package net.myconfig.service.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.UserGrant;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class HubAccessDecisionManager extends AbstractHubSelectorDependency implements AccessDecisionManager {

	private final Logger logger = LoggerFactory.getLogger(HubAccessDecisionManager.class);

	@Autowired
	public HubAccessDecisionManager(ApplicationContext applicationContext) {
		super(applicationContext);
	}

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		logger.debug("[grant] Authorizing {}...", object);
		// Method to authenticate
		MethodInvocation invocation = (MethodInvocation) object;
		// Checks the grants
		if (userGranted(authentication, invocation) || applicationGranted(authentication, invocation)) {
			logger.debug("[grant] Granted after authorization.");
		}
		// FIXME Environment level
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

	/**
	 * Checks if the current authentication has access to the application
	 * function for the given application ID.
	 */
	protected boolean checkApplicationGrant(Authentication authentication, int application, AppFunction fn) {
		return getSecuritySelector().hasApplicationFunction(authentication, application, fn);
	}

	/**
	 * Checks if the current authentication has access to the user function.
	 */
	protected boolean checkUserGrant(Authentication authentication, UserFunction fn) {
		return getSecuritySelector().hasUserFunction(authentication, fn);
	}

	protected <A extends Annotation> A getAnnotation(MethodInvocation invocation, Class<A> type) {
		Method method = invocation.getMethod();
		A a = method.getAnnotation(type);
		if (a == null) {
			Object target = invocation.getThis();
			Method targetMethod;
			try {
				targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
			} catch (Exception e) {
				throw new IllegalStateException("Cannot find target method", e);
			}
			return targetMethod.getAnnotation(type);
		} else {
			return a;
		}
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
