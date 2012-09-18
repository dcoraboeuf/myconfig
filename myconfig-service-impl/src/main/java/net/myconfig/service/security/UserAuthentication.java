package net.myconfig.service.security;

import java.util.Collection;

import net.myconfig.core.MyConfigRoles;
import net.myconfig.service.api.security.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class UserAuthentication implements Authentication {

	private final User user;
	private final Authentication authentication;

	public UserAuthentication(User user, Authentication authentication) {
		this.user = user;
		this.authentication = authentication;
	}

	@Override
	public String getName() {
		return user.getName();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (user != null) {
			if (user.isAdmin()) {
				return AuthorityUtils.createAuthorityList(MyConfigRoles.ADMIN);
			} else {
				return AuthorityUtils.createAuthorityList(MyConfigRoles.USER);
			}
		} else {
			return AuthorityUtils.createAuthorityList(MyConfigRoles.ANONYMOUS);
		}
	}

	@Override
	public Object getCredentials() {
		return authentication.getCredentials();
	}

	@Override
	public Object getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return authentication.getPrincipal();
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
	}

}
