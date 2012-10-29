package net.myconfig.service.security;

import static net.myconfig.service.db.SQLColumns.APPLICATION;
import static net.myconfig.service.db.SQLColumns.ENVIRONMENT;
import static net.myconfig.service.db.SQLColumns.GRANTEDFUNCTION;
import static net.myconfig.service.db.SQLColumns.USER;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.cache.CacheNames;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.impl.AbstractDaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class GrantServiceImpl extends AbstractDaoService implements GrantService {

	@Autowired
	public GrantServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Cacheable(CacheNames.USER_FUNCTIONS)
	@Transactional(readOnly = true)
	public EnumSet<UserFunction> getUserFunctions(String name) {
		List<UserFunction> fns = Lists.transform(getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTIONS_USER, new MapSqlParameterSource(SQLColumns.USER, name), String.class),
				new Function<String, UserFunction>() {
					@Override
					public UserFunction apply(String name) {
						return UserFunction.valueOf(name);
					}
				});
		if (fns.isEmpty()) {
			return EnumSet.noneOf(UserFunction.class);
		} else {
			return EnumSet.copyOf(fns);
		}
	}

	@Override
	@Cacheable(CacheNames.USER_FUNCTION)
	@Transactional(readOnly = true)
	public boolean hasUserFunction(String name, UserFunction fn) {
		return getFirstItem(SQL.FUNCTION_USER, new MapSqlParameterSource(USER, name).addValue(GRANTEDFUNCTION, fn.name()), String.class) != null;
	}

	@Override
	// FIXME @Cacheable(CacheNames.APP_FUNCTION)
	@Transactional(readOnly = true)
	public boolean hasAppFunction(String name, int application, AppFunction fn) {
		boolean ok = getFirstItem(SQL.FUNCTION_APP, new MapSqlParameterSource(USER, name).addValue(APPLICATION, application).addValue(GRANTEDFUNCTION, fn.name()), String.class) != null;
		if (!ok && fn.hasParents()) {
			for (AppFunction parentFn: fn.getParents()) {
				ok = hasAppFunction(name, application, parentFn);
				if (ok) {
					return true;
				}
			}
			return false;
		} else {
			return ok;
		}
	}

	@Override
	// FIXME @Cacheable(CacheNames.ENV_FUNCTION)
	@Transactional(readOnly = true)
	public boolean hasEnvFunction(String name, int application, String environment, EnvFunction fn) {
		boolean ok = getFirstItem(SQL.FUNCTION_ENV, new MapSqlParameterSource(USER, name).addValue(APPLICATION, application).addValue(ENVIRONMENT, environment).addValue(GRANTEDFUNCTION, fn.name()),
				String.class) != null;
		if (!ok && fn.hasParents()) {
			for (EnvFunction parentFn: fn.getParents()) {
				ok = hasEnvFunction(name, application, environment, parentFn);
				if (ok) {
					return true;
				}
			}
			return false;
		} else {
			return ok;
		}
	}

	@Override
	@Caching(evict = {
			@CacheEvict(CacheNames.USER_FUNCTION),
			@CacheEvict(value = CacheNames.USER_FUNCTIONS, key = "#name")
		})
	@Transactional
	public Ack userFunctionAdd(String name, UserFunction fn) {
		userFunctionRemove(name, fn);
		int count = getNamedParameterJdbcTemplate().update(SQL.FUNCTIONS_USER_ADD, new MapSqlParameterSource().addValue(SQLColumns.USER, name).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(CacheNames.USER_FUNCTION),
			@CacheEvict(value = CacheNames.USER_FUNCTIONS, key = "#name")
		})
	@Transactional
	public Ack userFunctionRemove(String name, UserFunction fn) {
		int count = getNamedParameterJdbcTemplate().update(SQL.FUNCTIONS_USER_REMOVE, new MapSqlParameterSource().addValue(SQLColumns.USER, name).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	public Ack appFunctionAdd(int application, String user, AppFunction fn) {
		appFunctionRemove(application, user, fn);
		int count = getNamedParameterJdbcTemplate().update(
				SQL.GRANT_APP_FUNCTION,
				new MapSqlParameterSource()
					.addValue(APPLICATION, application)
					.addValue(SQLColumns.USER, user)
					.addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	public Ack appFunctionRemove(int application, String user, AppFunction fn) {
		int count = getNamedParameterJdbcTemplate().update(SQL.UNGRANT_APP_FUNCTION, new MapSqlParameterSource().addValue(APPLICATION, application).addValue(SQLColumns.USER, user).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}
	
	@Override
	@Transactional(readOnly = true)
	public EnumSet<AppFunction> getAppFunctions(int application, String name) {
		NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// Set of allowed functions
		Collection<AppFunction> fns = Lists.transform(
				t.queryForList(SQL.FUNCTION_APP_LIST_FOR_USER,
						new MapSqlParameterSource()
							.addValue(APPLICATION, application)
							.addValue(USER, name),
						String.class),
				new Function<String, AppFunction>() {
					@Override
					public AppFunction apply (String value) {
						return AppFunction.valueOf(value);
					}
				});
		// List of functions
		EnumSet<AppFunction> functions;
		if (fns.isEmpty()) {
			functions = EnumSet.noneOf(AppFunction.class);
		} else {
			functions = EnumSet.copyOf(fns);
		}
		// OK
		return functions;
	}
	
	@Override
	@Transactional
	public Ack envFunctionAdd(int application, String user, String environment, EnvFunction fn) {
		envFunctionRemove(application, user, environment, fn);
		int count = getNamedParameterJdbcTemplate().update(SQL.GRANT_ENV_FUNCTION, new MapSqlParameterSource().addValue(APPLICATION, application).addValue(SQLColumns.USER, user).addValue(SQLColumns.ENVIRONMENT, environment).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	public Ack envFunctionRemove(int application, String user, String environment, EnvFunction fn) {
		int count = getNamedParameterJdbcTemplate().update(SQL.UNGRANT_ENV_FUNCTION, new MapSqlParameterSource().addValue(APPLICATION, application).addValue(SQLColumns.USER, user).addValue(SQLColumns.ENVIRONMENT, environment).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}
	
	@Override
	@Transactional(readOnly = true)
	public EnumSet<EnvFunction> getEnvFunctions(int application, String user, String environment) {
		// Set of allowed functions
		Collection<EnvFunction> fns = Lists.transform(
				getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTION_ENV_LIST_FOR_USER_AND_APPLICATION,
						new MapSqlParameterSource()
							.addValue(APPLICATION, application)
							.addValue(USER, user)
							.addValue(ENVIRONMENT, environment),
						String.class),
				new Function<String, EnvFunction>() {
					@Override
					public EnvFunction apply (String value) {
						return EnvFunction.valueOf(value);
					}
				});
		// List of functions
		EnumSet<EnvFunction> functions;
		if (fns.isEmpty()) {
			functions = EnumSet.noneOf(EnvFunction.class);
		} else {
			functions = EnumSet.copyOf(fns);
		}
		// OK
		return functions;
	}

}
