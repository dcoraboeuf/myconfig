package net.myconfig.service.security;

import java.util.Collection;

import net.myconfig.core.MyConfigRoles;
import net.myconfig.service.api.security.UserProfile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class UserAuthentication implements Authentication {

	private final UserProfile token;
	private final Authentication authentication;

	public UserAuthentication(UserProfile token, Authentication authentication) {
		this.token = token;
		this.authentication = authentication;
	}

	@Override
	public String getName() {
		return token.getName();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (token != null) {
			if (token.isAdmin()) {
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
		return token;
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
