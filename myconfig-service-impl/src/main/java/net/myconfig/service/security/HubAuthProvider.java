package net.myconfig.service.security;

import java.util.Collection;
import java.util.Map;

import net.myconfig.service.api.AuthProviderSelector;
import net.myconfig.service.api.NamedAuthenticationProvider;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

@Component
public class HubAuthProvider implements AuthenticationProvider {

	private final Logger logger = LoggerFactory.getLogger(HubAuthProvider.class);

	private final Map<String, NamedAuthenticationProvider> providers;
	private final AuthProviderSelector authProviderSelector;

	@Autowired
	public HubAuthProvider(Collection<NamedAuthenticationProvider> providers, AuthProviderSelector authProviderSelector) {
		this.providers = Maps.uniqueIndex(providers, new Function<NamedAuthenticationProvider, String>() {
			@Override
			public String apply(NamedAuthenticationProvider provider) {
				return provider.getId();
			}
		});
		// Logging
		logger.info("List of authentication providers:");
		for (NamedAuthenticationProvider provider : providers) {
			logger.info("[{}] {}", provider.getId(), provider);
		}
		// OK
		this.authProviderSelector = authProviderSelector;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// Gets the provider
		AuthenticationProvider selectedProvider = getSelectedProvider();
		// Delegates
		return selectedProvider.authenticate(authentication);
	}

	protected AuthenticationProvider getSelectedProvider() {
		logger.debug("Selecting the provider...");
		// Gets the selected provider ID
		String selectedProviderId = getSelectedProviderId();
		logger.debug("Selecting the provider with ID = {}...", selectedProviderId);
		// Gets the selected provider
		AuthenticationProvider selectedProvider = providers.get(selectedProviderId);
		if (selectedProvider == null) {
			throw new IllegalStateException("Unknown provider: " + selectedProviderId);
		}
		// OK
		logger.debug("Selected provider is {}...", selectedProvider);
		return selectedProvider;
	}

	protected String getSelectedProviderId() {
		String id = authProviderSelector.getSelectedAuthProviderId();
		if (StringUtils.isBlank(id)) {
			throw new IllegalStateException("Selected provider is blank or null");
		} else {
			return id;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// Gets the provider
		AuthenticationProvider selectedProvider = getSelectedProvider();
		// Tests
		return selectedProvider.supports(authentication);
	}

}
