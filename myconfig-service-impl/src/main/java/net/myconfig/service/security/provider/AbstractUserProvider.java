package net.myconfig.service.security.provider;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.security.UserProvider;
import net.myconfig.service.impl.AbstractDaoService;

public abstract class AbstractUserProvider extends AbstractDaoService implements UserProvider {

	private final String id;

	public AbstractUserProvider(DataSource dataSource, Validator validator, String id) {
		super(dataSource, validator);
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
