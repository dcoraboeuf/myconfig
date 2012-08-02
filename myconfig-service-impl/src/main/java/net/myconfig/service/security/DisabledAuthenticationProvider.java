package net.myconfig.service.security;

import java.util.Collections;
import java.util.Set;

import net.myconfig.core.AppFunction;
import net.myconfig.core.MyConfigRoles;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class DisabledAuthenticationProvider extends AbstractNamedAuthenticationProvider {

	public DisabledAuthenticationProvider() {
		super("disabled");
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return new AnonymousAuthenticationToken(
				"anonymous",
				new UserTokenImpl(
					new User("anonymous", true),
					Collections.<UserFunction>emptyList(),
					Collections.<Integer,Set<AppFunction>>emptyMap()),
				AuthorityUtils.createAuthorityList(MyConfigRoles.ANONYMOUS));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
