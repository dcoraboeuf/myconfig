package net.myconfig.service.security;

import java.lang.annotation.Annotation;
import java.util.Collection;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.UserGrant;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class GrantAccessDecisionManager implements AccessDecisionManager {

	private final Logger logger = LoggerFactory.getLogger(GrantAccessDecisionManager.class);

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
			return checkAplicationGrant(authentication, application, grant.value());
		} else {
			return false;
		}
	}

	/**
	 * Checks if the current authentication has access to the application
	 * function for the given application ID.
	 */
	protected boolean checkAplicationGrant(Authentication authentication, int application, AppFunction fn) {
		UserTokenImpl token = getUserToken(authentication);
		return token != null && token.hasAppFunction(application, fn);
	}

	/**
	 * Checks if the current authentication has access to the user function.
	 */
	protected boolean checkUserGrant(Authentication authentication, UserFunction fn) {
		UserTokenImpl token = getUserToken(authentication);
		return token != null && token.hasUserFunction(fn);
	}

	protected UserTokenImpl getUserToken(Authentication authentication) {
		if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserTokenImpl) {
			return (UserTokenImpl) authentication.getPrincipal();
		} else {
			return null;
		}
	}

	protected <A extends Annotation> A getAnnotation(MethodInvocation invocation, Class<A> type) {
		return invocation.getMethod().getAnnotation(type);
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
