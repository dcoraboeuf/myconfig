package net.myconfig.service.security;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.SecurityManagement;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserToken;

@Component
public class HubSecuritySelector implements SecuritySelector {

	public static final String SECURITY_MODE = "security.mode";

	private static final String SECURITY_MODE_DEFAULT = "none";

	private final Logger logger = LoggerFactory.getLogger(HubSecuritySelector.class);

	private final ConfigurationService configurationService;
	private final Map<String, SecurityManagement> managers;

	@Autowired
	public HubSecuritySelector(Collection<SecurityManagement> managers, ConfigurationService configurationService) {
		this.configurationService = configurationService;
		this.managers = Maps.uniqueIndex(managers, new Function<SecurityManagement, String>() {
			@Override
			public String apply(SecurityManagement manager) {
				logger.info("[{}] {}", manager.getId(), manager);
				return manager.getId();
			}
		});
	}
	
	@Override
	public String getSecurityManagementId() {
		return configurationService.getParameter(SECURITY_MODE, SECURITY_MODE_DEFAULT);
	}

	protected SecurityManagement getSecurityManagement() {
		String mode = getSecurityManagementId();
		SecurityManagement management = managers.get(mode);
		if (management != null) {
			return management;
		} else {
			throw new SecurityManagementNotFoundException(mode);
		}
	}

	@Override
	public UserToken authenticate(Authentication authentication) {
		return getSecurityManagement().authenticate(authentication);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return getSecurityManagement().supports(authentication);
	}

	@Override
	public boolean hasUserFunction(Authentication authentication, UserFunction fn) {
		return getSecurityManagement().hasUserFunction(authentication, fn);
	}

	@Override
	public boolean hasApplicationFunction(Authentication authentication, int application, AppFunction fn) {
		return getSecurityManagement().hasApplicationFunction(authentication, application, fn);
	}

	@Override
	public boolean allowLogin() {
		return getSecurityManagement().allowLogin();
	}

}
