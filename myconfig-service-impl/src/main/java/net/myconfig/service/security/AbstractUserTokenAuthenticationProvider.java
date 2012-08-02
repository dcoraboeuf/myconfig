package net.myconfig.service.security;

import java.util.List;

import net.myconfig.core.MyConfigRoles;
import net.myconfig.service.api.security.UserToken;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public abstract class AbstractUserTokenAuthenticationProvider extends AbstractNamedAuthenticationProvider {

	public AbstractUserTokenAuthenticationProvider(String id) {
		super(id);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken o = (UsernamePasswordAuthenticationToken) authentication;
			String username = o.getName();
			String password = (String) authentication.getCredentials();
			// Gets the user
			UserToken user = getUserToken(username, password);
			if (user == null) {
				return null;
			}
			// Convert the user to a list of authorities
			List<GrantedAuthority> authorities = getAuthorities(user);
			// OK
			return new UsernamePasswordAuthenticationToken(user, password, authorities);
		} else {
			return null;
		}
	}

	protected List<GrantedAuthority> getAuthorities(UserToken user) {
		if (user.getUser().isAdmin()) {
			return AuthorityUtils.createAuthorityList(MyConfigRoles.ADMIN);
		} else {
			return AuthorityUtils.createAuthorityList(MyConfigRoles.USER);
		}
	}

	protected abstract UserToken getUserToken(String username, String password);

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
