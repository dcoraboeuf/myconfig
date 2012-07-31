package net.myconfig.service.security;

import java.util.List;

import net.myconfig.core.MyConfigFunctions;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public abstract class AbstractFunctionAuthenticationProvider extends AbstractNamedAuthenticationProvider {

	private final SecurityService securityService;

	public AbstractFunctionAuthenticationProvider(String id, SecurityService securityService) {
		super(id);
		this.securityService = securityService;
	}
	
	protected SecurityService getSecurityService() {
		return securityService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken o = (UsernamePasswordAuthenticationToken) authentication;
			String username = o.getName();
			String password = (String) authentication.getCredentials();
			// Gets the user
			User user = getUser(username, password);
			if (user == null) {
				return null;
			}
			// Gets the general granted functions
			List<MyConfigFunctions> functions = getFunctions(username);
			// Converts them to authorities
			List<GrantedAuthority> authorities = Lists.transform(functions, new Function<MyConfigFunctions, GrantedAuthority>() {
				@Override
				public GrantedAuthority apply(MyConfigFunctions function) {
					return new SimpleGrantedAuthority(function.name());
				}
			});
			// OK
			return new UsernamePasswordAuthenticationToken(user, password, authorities);
		} else {
			return null;
		}
	}

	protected List<MyConfigFunctions> getFunctions(String username) {
		return securityService.getUserFunctions(username);
	}

	protected abstract User getUser(String username, String password);

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
