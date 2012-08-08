package net.myconfig.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserProfile;
import net.myconfig.service.security.UserProfileImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationServiceImpl extends AbstractSecurityService implements AuthenticationService {

	public static void main(String[] args) {
		for (String password : args) {
			System.out.format("%s ==> %s%n", password, digest(password));
		}
	}

	@Autowired
	public AuthenticationServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional(readOnly = true)
	public UserProfile getUserToken(String username, String password) {
		// Gets the user
		User user = getUser(username, password);
		if (user == null) {
			return null;
		}
		// User functions
		List<UserFunction> userFunctions = getUserFunctions(user);
		// Application functions
		Map<Integer, Set<AppFunction>> appFunctions = getAppFunctions(user);
		// OK
		return new UserProfileImpl(user, userFunctions, appFunctions);
	}

}
