package net.myconfig.service.impl;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.User;

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
	public User getUserToken(String username, String password) {
		// Gets the user
		User user = getUser(username, password);
		// OK
		return user;
	}

}
