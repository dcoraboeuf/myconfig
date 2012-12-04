package net.myconfig.service.security.provider;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.security.UserProvider;
import net.myconfig.service.impl.AbstractDaoService;

public abstract class AbstractUserProvider extends AbstractDaoService implements UserProvider {

	protected static final String USER_CREATE = "insert into users (mode, name, displayname, password, admin, email, verified, disabled) values (:mode, :name, :displayName, '', false, :email, :verified, false)";

	private final String id;

	public AbstractUserProvider(DataSource dataSource, Validator validator, String id) {
		super(dataSource, validator);
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public boolean isEnabled() {
		return isConfigured() && isInternalEnabled();
	}

	protected abstract boolean isInternalEnabled();

}
