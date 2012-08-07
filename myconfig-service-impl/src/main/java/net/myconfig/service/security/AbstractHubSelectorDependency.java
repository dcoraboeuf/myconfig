package net.myconfig.service.security;

import net.myconfig.service.api.security.SecuritySelector;

import org.springframework.context.ApplicationContext;

public abstract class AbstractHubSelectorDependency {

	private final ApplicationContext applicationContext;

	private SecuritySelector securitySelector;

	public AbstractHubSelectorDependency(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	protected SecuritySelector getSecuritySelector() {
		init();
		return securitySelector;
	}

	private void init() {
		if (securitySelector == null) {
			load();
		}
	}

	private synchronized void load() {
		if (securitySelector == null) {
			securitySelector = applicationContext.getBean(SecuritySelector.class);
		}
	}

}
