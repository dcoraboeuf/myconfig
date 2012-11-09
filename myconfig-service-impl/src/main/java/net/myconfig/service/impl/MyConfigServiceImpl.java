package net.myconfig.service.impl;

import static net.myconfig.service.db.SQLColumns.APPLICATION;
import static net.myconfig.service.db.SQLColumns.CONFIG_COUNT;
import static net.myconfig.service.db.SQLColumns.DESCRIPTION;
import static net.myconfig.service.db.SQLColumns.DISPLAYNAME;
import static net.myconfig.service.db.SQLColumns.ENVIRONMENT;
import static net.myconfig.service.db.SQLColumns.ENVIRONMENT_COUNT;
import static net.myconfig.service.db.SQLColumns.ID;
import static net.myconfig.service.db.SQLColumns.KEY;
import static net.myconfig.service.db.SQLColumns.KEY_COUNT;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.TYPEID;
import static net.myconfig.service.db.SQLColumns.TYPEPARAM;
import static net.myconfig.service.db.SQLColumns.VALUE;
import static net.myconfig.service.db.SQLColumns.VALUE_COUNT;
import static net.myconfig.service.db.SQLColumns.VERSION;
import static net.myconfig.service.db.SQLColumns.VERSION_COUNT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.CoreException;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ApplicationUserRights;
import net.myconfig.core.model.ApplicationUsers;
import net.myconfig.core.model.ConditionalValue;
import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.core.model.ConfigurationUpdate;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.ConfigurationValue;
import net.myconfig.core.model.Environment;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.EnvironmentSummary;
import net.myconfig.core.model.EnvironmentUserRights;
import net.myconfig.core.model.EnvironmentUsers;
import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.core.model.IndexedValues;
import net.myconfig.core.model.Key;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.KeySummary;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.MatrixVersionConfiguration;
import net.myconfig.core.model.UserApplicationRights;
import net.myconfig.core.model.UserName;
import net.myconfig.core.model.Version;
import net.myconfig.core.model.VersionConfiguration;
import net.myconfig.core.model.VersionSummary;
import net.myconfig.core.type.ValueType;
import net.myconfig.core.type.ValueTypeDescriptions;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.AppGrantParam;
import net.myconfig.service.api.security.EnvGrant;
import net.myconfig.service.api.security.EnvGrantParam;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserGrant;
import net.myconfig.service.api.type.KeyTypeParameterInvalidException;
import net.myconfig.service.api.type.KeyValueFormatException;
import net.myconfig.service.api.type.ValueTypeFactory;
import net.myconfig.service.audit.Audit;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.exception.ApplicationIDAlreadyDefinedException;
import net.myconfig.service.exception.ApplicationNameAlreadyDefinedException;
import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.service.exception.EnvironmentAlreadyDefinedException;
import net.myconfig.service.exception.EnvironmentNotFoundException;
import net.myconfig.service.exception.KeyAlreadyDefinedException;
import net.myconfig.service.exception.KeyAlreadyInVersionException;
import net.myconfig.service.exception.KeyNotFoundException;
import net.myconfig.service.exception.MatrixNotFoundException;
import net.myconfig.service.exception.VersionAlreadyDefinedException;
import net.myconfig.service.exception.VersionNotFoundException;
import net.myconfig.service.validation.ApplicationValidation;
import net.myconfig.service.validation.EnvironmentValidation;
import net.myconfig.service.validation.KeyValidation;
import net.myconfig.service.validation.VersionValidation;
import net.sf.jstring.Localizable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Service
public class MyConfigServiceImpl extends AbstractSecureService implements MyConfigService {

	private final String versionNumber;
	private final ValueTypeFactory valueTypeFactory;

	@Autowired
	public MyConfigServiceImpl(@Value("${app.version}") String versionNumber, DataSource dataSource, Validator validator, SecuritySelector securitySelector, GrantService grantService, ValueTypeFactory valueTypeFactory) {
		super(dataSource, validator, securitySelector, grantService);
		this.versionNumber = versionNumber;
		this.valueTypeFactory = valueTypeFactory;
	}
	
	@Override
	public String getVersion() {
		return versionNumber;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ApplicationSummaries getApplications() {
		// Gets the raw list of applications
		List<ApplicationSummary> unfilteredApplications = getJdbcTemplate().query(SQL.APPLICATIONS, new RowMapper<ApplicationSummary>() {

			@Override
			public ApplicationSummary mapRow(ResultSet rs, int i)
					throws SQLException {
				return new ApplicationSummary(
						rs.getString(ID),
						rs.getString(NAME),
						rs.getInt(VERSION_COUNT),
						rs.getInt(KEY_COUNT),
						rs.getInt(ENVIRONMENT_COUNT),
						rs.getInt(CONFIG_COUNT),
						rs.getInt(VALUE_COUNT),
						false, false, false, false, false);
			}
		});
		
		// 1) Filter at application level
		List<ApplicationSummary> filteredApplications;
		// If user is granted with app_list, he can see all applications
		if (hasUserAccess(UserFunction.app_list)) {
			filteredApplications = unfilteredApplications;
		}
		// If not, we have to filter on app_view
		else {
			filteredApplications = Lists.newArrayList(Iterables.filter(unfilteredApplications, new Predicate<ApplicationSummary>() {
				@Override
				public boolean apply(ApplicationSummary app) {
					return hasApplicationAccess(app.getId(), AppFunction.app_view);
				}
			}));
		}
		
		// 2) Gets the ACL rights for each remaining application
		List<ApplicationSummary> aclApplications = Lists.transform(filteredApplications, new Function<ApplicationSummary, ApplicationSummary>() {
			@Override
			public ApplicationSummary apply(ApplicationSummary app) {
				String id = app.getId();
				boolean canDelete = hasApplicationAccess(id, AppFunction.app_delete);
				boolean canConfig = hasApplicationAccess(id, AppFunction.app_config);
				boolean canView = hasApplicationAccess(id, AppFunction.app_view);
				boolean canMatrix = hasApplicationAccess(id, AppFunction.app_matrix);
				boolean canUsers = hasApplicationAccess(id, AppFunction.app_users);
				return app.acl (canDelete, canConfig, canView, canMatrix, canUsers);
			}
		});
		
		// OK
		return new ApplicationSummaries(aclApplications);
	}
	
	@Override
	@Transactional(readOnly = true)
	@AppGrant(AppFunction.app_users)
	public ApplicationUsers getApplicationUsers(@AppGrantParam final String id) {
		// Gets the application name
		String name = getApplicationName(id);
		// List of users
		List<UserName> userNames = getJdbcTemplate().query(SQL.USER_NAMES, new RowMapper<UserName>() {

			@Override
			public UserName mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new UserName(rs.getString(NAME), rs.getString(DISPLAYNAME));
			}
			
		});
		// Gets the application rights for each user
		List<ApplicationUserRights> users = Lists.transform(userNames, new Function<UserName, ApplicationUserRights>() {
			@Override
			public ApplicationUserRights apply (UserName user) {
				EnumSet<AppFunction> functions= grantService.getAppFunctions (id, user.getName());
				// OK
				return new ApplicationUserRights(user.getName(), user.getDisplayName(), functions);
			}
		});
		// OK
		return new ApplicationUsers(id, name, users);
	}
	
	@Override
	@Transactional(readOnly = true)
	@EnvGrant(EnvFunction.env_users)
	public EnvironmentUsers getEnvironmentUsers(@AppGrantParam final String id, @EnvGrantParam final String environment) {
		// Gets the application name
		String applicationName = getApplicationName(id);
		// List of users
		List<UserName> userNames = getJdbcTemplate().query(SQL.USER_NAMES, new RowMapper<UserName>() {

			@Override
			public UserName mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new UserName(rs.getString(NAME), rs.getString(DISPLAYNAME));
			}
			
		});
		// Gets the environment rights for each user and this applications
		List<EnvironmentUserRights> users = Lists.transform(userNames, new Function<UserName, EnvironmentUserRights>() {
			@Override
			public EnvironmentUserRights apply (UserName user) {
				EnumSet<EnvFunction> functions = grantService.getEnvFunctions(id, user.getName(), environment);
				// OK
				return new EnvironmentUserRights(user.getName(), user.getDisplayName(), functions);
			}
		});
		// OK
		return new EnvironmentUsers(id, applicationName, environment, users);
	}
	
	@Override
	@Transactional(readOnly = true)
	@AppGrant(AppFunction.app_view)
	public ApplicationConfiguration getApplicationConfiguration(@AppGrantParam String id) {
		NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// ID
		MapSqlParameterSource applicationCriteria = new MapSqlParameterSource(APPLICATION, id);
		// Gets the name
		String name = getApplicationName(id);
		// Versions	
		List<VersionSummary> versionSummaryList = t.query(SQL.VERSION_SUMMARIES, applicationCriteria, new RowMapper<VersionSummary>(){
			@Override
			public VersionSummary mapRow(ResultSet rs, int i) throws SQLException {
				String version = rs.getString(NAME);
				int keyCount = rs.getInt(KEY_COUNT);
				int valueCount = rs.getInt(VALUE_COUNT);
				int environmentCount = rs.getInt(ENVIRONMENT_COUNT);
				int configCount = keyCount * environmentCount;
				return new VersionSummary(
						version,
						keyCount,
						configCount,
						valueCount);
			}
		});
		// Environments
		List<EnvironmentSummary> environmentSummaryList = t.query(SQL.ENVIRONMENT_SUMMARIES, applicationCriteria, new RowMapper<EnvironmentSummary>(){
			@Override
			public EnvironmentSummary mapRow(ResultSet rs, int i) throws SQLException {
				String environment = rs.getString(NAME);
				int configCount = rs.getInt(KEY_COUNT);
				int valueCount = rs.getInt(VALUE_COUNT);
				return new EnvironmentSummary(environment, configCount, valueCount);
			}
		});
		// Keys
		List<KeySummary> keySummaryList = t.query(SQL.KEY_SUMMARIES, applicationCriteria, new RowMapper<KeySummary>(){
			@Override
			public KeySummary mapRow(ResultSet rs, int i) throws SQLException {
				String key = rs.getString(NAME);
				String description = rs.getString(SQLColumns.DESCRIPTION);
				String typeId = rs.getString(SQLColumns.TYPEID);
				if (StringUtils.isBlank(typeId)) {
					typeId = ValueType.PLAIN;
				}
				String typeParam = rs.getString(SQLColumns.TYPEPARAM);
				int versionCount = rs.getInt(SQLColumns.VERSION_COUNT);
				int environmentCount = rs.getInt(SQLColumns.ENVIRONMENT_COUNT);
				int valueCount = rs.getInt(SQLColumns.VALUE_COUNT);
				int configCount = versionCount * environmentCount;
				return new KeySummary(key, description, typeId, typeParam, versionCount, configCount, valueCount);
			}
		});
		// OK
		return new ApplicationConfiguration(id, name,
				versionSummaryList, environmentSummaryList, keySummaryList);
	}

	protected String getApplicationName(String id) {
		String name;
		try {
			name = getNamedParameterJdbcTemplate().queryForObject(SQL.APPLICATION_NAME, new MapSqlParameterSource("id", id), String.class);
		} catch (EmptyResultDataAccessException ex) {
			throw new ApplicationNotFoundException(id);
		}
		return name;
	}
	
	@Override
	@Transactional
	@UserGrant(UserFunction.app_create)
	@Audit(category = EventCategory.APPLICATION, action = EventAction.CREATE, application = "#id", message = "#name") 
	public ApplicationSummary createApplication(String id, String name) {
		validate(ApplicationValidation.class, ID, id);
		validate(ApplicationValidation.class, NAME, name);
		// Checks for uniqueness
		checkNotExist (SQL.APPLICATION_NAME, new MapSqlParameterSource(ID, id), new ApplicationIDAlreadyDefinedException(id));
		checkNotExist (SQL.APPLICATION_ID, new MapSqlParameterSource(NAME, name), new ApplicationNameAlreadyDefinedException(name));
		// Inserts
		getNamedParameterJdbcTemplate().update(
			SQL.APPLICATION_CREATE,
			new MapSqlParameterSource(ID,id).addValue(NAME, name));
		// Initial grants
		for (AppFunction fn : AppFunction.values()) {
			grantAppFunction (id, fn);
		}
		// OK
		return new ApplicationSummary(id, name, 0, 0, 0, 0, 0, true, true, true, true, true);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_delete)
	@Audit(category = EventCategory.APPLICATION, action = EventAction.DELETE, application = "#id", result = "#result.success")
	public Ack deleteApplication(@AppGrantParam String id) {
		int count = getNamedParameterJdbcTemplate().update(SQL.APPLICATION_DELETE, new MapSqlParameterSource (ID, id));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	@Audit(category = EventCategory.VERSION, action = EventAction.CREATE, application = "#id", version = "#name", result = "#result.success")
	public Ack createVersion(@AppGrantParam String id, String name) {
		validate(VersionValidation.class, NAME, name);
		checkApplication(id);
		try {
			int count = getNamedParameterJdbcTemplate().update(SQL.VERSION_CREATE,
				idNameSource(id, name));
			return Ack.one (count);
		} catch (DuplicateKeyException ex) {
			throw new VersionAlreadyDefinedException (id, name);
		}
	}

	protected MapSqlParameterSource idNameSource(String id, String name) {
		return new MapSqlParameterSource()
			.addValue(ID, id)
			.addValue(NAME, name);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	@Audit(category = EventCategory.VERSION, action = EventAction.DELETE, application = "#id", version = "#name", result = "#result.success")
	public Ack deleteVersion(@AppGrantParam String id, String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.VERSION_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_envcreate)
	@Audit(category = EventCategory.ENVIRONMENT, action = EventAction.CREATE, application = "#id", environment = "#name", result = "#result.success") 
	public Ack createEnvironment(@AppGrantParam String id, String name) {
		validate(EnvironmentValidation.class, NAME, name);
		checkApplication(id);
		try {
			int count = getNamedParameterJdbcTemplate().update(SQL.ENVIRONMENT_CREATE,
				idNameSource(id, name));
			if (count == 1) {
				// Initial grants
				for (EnvFunction fn : EnvFunction.values()) {
					grantEnvFunction (id, name, fn);
				}
				return Ack.OK;
			} else {
				return Ack.NOK;
			}
		} catch (DuplicateKeyException ex) {
			throw new EnvironmentAlreadyDefinedException(id, name);
		}
	}
	
	@Override
	@Transactional
	@EnvGrant(EnvFunction.env_delete)
	@Audit(category = EventCategory.ENVIRONMENT, action = EventAction.DELETE, application = "#id", environment = "#name", result = "#result.success")
	public Ack deleteEnvironment(@AppGrantParam String id, @EnvGrantParam String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.ENVIRONMENT_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	@Audit(category = EventCategory.KEY, action = EventAction.CREATE, application = "#id", key = "#name", message = "#description", result = "#result.success") 
	public Ack createKey(@AppGrantParam String id, String name, String description, String typeId, String typeParam) {
		validate(KeyValidation.class, NAME, name);
		validate(KeyValidation.class, DESCRIPTION, description);
		checkApplication(id);
		// Validates the parameter type
		validateType (typeId, typeParam);
		// OK
		try {
			int count = getNamedParameterJdbcTemplate().update(SQL.KEY_CREATE,
				idNameSource(id, name)
				.addValue(DESCRIPTION, description)
				.addValue(TYPEID, typeId)
				.addValue(TYPEPARAM, typeParam));
			return Ack.one (count);
		} catch (DuplicateKeyException ex) {
			throw new KeyAlreadyDefinedException (id, name);
		}
	}
	
	protected void validateValue(String application, String keyName, String value) {
		// Loads the key
		Key key = getNamedParameterJdbcTemplate().queryForObject(SQL.KEY, new MapSqlParameterSource(APPLICATION, application).addValue(KEY, keyName), new KeyRowMapper());
		// Gets its type & format
		String typeId = key.getTypeId();
		String typeParam = key.getTypeParam();
		// Validation
		Localizable message = validateTypeValue(typeId, typeParam, value);
		if (message != null) {
			throw new KeyValueFormatException(keyName, typeId, typeParam, value, message);
		}
	}
	
	protected ValueType validateType(String typeId, String typeParam) {
		// Validates the type
		ValueType valueType = valueTypeFactory.getValueType(typeId);
		// Validates the parameter
		Localizable message = valueType.validateParameter(typeParam);
		if (message != null) {
			throw new KeyTypeParameterInvalidException(typeId, typeParam, message);
		}
		// OK
		return valueType;
	}

	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	@Audit(category = EventCategory.KEY, action = EventAction.UPDATE, application = "#application", key = "#name", message = "#description", result = "#result.success")
	public Ack updateKey(@AppGrantParam String application, String name, String description) {
		validate(KeyValidation.class, DESCRIPTION, description);
		checkApplication(application);
		checkKey(application, name);
		getNamedParameterJdbcTemplate().update(
				SQL.KEY_UPDATE,
				new MapSqlParameterSource()
					.addValue(APPLICATION, application)
					.addValue(NAME, name)
					.addValue(DESCRIPTION, description));
		return Ack.OK;
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	@Audit(category = EventCategory.KEY, action = EventAction.DELETE, application = "#id", key = "#name", result = "#result.success")
	public Ack deleteKey(@AppGrantParam String id, String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.KEY_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional(readOnly = true)
	@AppGrant(AppFunction.app_matrix)
	public MatrixConfiguration keyVersionConfiguration(@AppGrantParam String id) {
		checkApplication(id);
		// Criteria
		MapSqlParameterSource idCriteria = new MapSqlParameterSource("application", id);
		String name = getApplicationName(id);
		// List of keys		
		List<Key> keyList = getNamedParameterJdbcTemplate().query(SQL.KEYS, idCriteria, new KeyRowMapper());
		// List of versions
		List<Version> versionList = getNamedParameterJdbcTemplate().query(SQL.VERSIONS, idCriteria, new RowMapper<Version>() {
			@Override
			public Version mapRow(ResultSet rs, int i) throws SQLException {
				return new Version(rs.getString(NAME));
			}
		});
		// Configuration list for versions
		List<MatrixVersionConfiguration> versionConfigurationList = new ArrayList<MatrixVersionConfiguration>();
		for (Version version : versionList) {
			// Gets the list of keys for this version
			List<String> keys = getNamedParameterJdbcTemplate().queryForList(
					SQL.VERSION_KEYS,
					idCriteria.addValue(VERSION, version.getName()),
					String.class);
			// Version configuration
			MatrixVersionConfiguration versionConfiguration = new MatrixVersionConfiguration(version.getName(), keys);
			// Adds to the list
			versionConfigurationList.add(versionConfiguration);
		}
		// OK
		return new MatrixConfiguration(id, name, versionConfigurationList, keyList);
	}
	
	@Override
	@Transactional(readOnly = true)
	@AppGrant(AppFunction.app_view)
	public VersionConfiguration getVersionConfiguration(@AppGrantParam String application, String version) {
		checkApplication(application);
		checkVersion(application, version);
		// Application name
		MapSqlParameterSource applicationCriteria = new MapSqlParameterSource(APPLICATION, application);
		MapSqlParameterSource versionCriteria = applicationCriteria.addValue(VERSION, version);
		String name = getApplicationName(application);
		// List of environments
		List<Environment> environments = getNamedParameterJdbcTemplate().query(SQL.ENVIRONMENTS, applicationCriteria, new RowMapper<Environment>() {
			@Override
			public Environment mapRow(ResultSet rs, int i) throws SQLException {
				return new Environment(rs.getString(NAME));
			}
		});
		// Filters the list of environments
		environments = filterEnvironments(application, environments);
		// List of keys for the version
		List<Key> keyList = getNamedParameterJdbcTemplate().query(SQL.KEYS_FOR_VERSION, versionCriteria, new KeyRowMapper());
		// Gets the list of values per version x application
		List<Map<String, Object>> valuesMaps = getNamedParameterJdbcTemplate().queryForList(SQL.CONFIG_FOR_VERSION, applicationCriteria);
		final Map<String,Map<String,String>> environmentValues = new TreeMap<String, Map<String,String>>();
		for (Map<String, Object> values : valuesMaps) {
			String environment = (String) values.get(ENVIRONMENT);
			String key = (String) values.get(KEY);
			String value = (String) values.get(VALUE);
			Map<String, String> environmentConfiguration = environmentValues.get(environment);
			if (environmentConfiguration == null) {
				environmentConfiguration = new TreeMap<String, String>();
				environmentValues.put(environment, environmentConfiguration);
			}
			environmentConfiguration.put(key, value);
		}
		// List of values per environment x key x version
		List<IndexedValues<String>> environmentConfigurationList = Lists.transform(environments, new Function<Environment, IndexedValues<String>>() {

			@Override
			public IndexedValues<String> apply(Environment environment) {
				String name = environment.getName();
				Map<String, String> values = environmentValues.get(name);
				if (values == null) {
					values = Collections.emptyMap();
				}
				return new IndexedValues<String>(name, values);
			}
		});
		// Previous & next version
		String previousVersion = getFirstItem(SQL.VERSION_PREVIOUS, versionCriteria, String.class);
		String nextVersion = getFirstItem(SQL.VERSION_NEXT, versionCriteria, String.class);
		// OK
		return new VersionConfiguration(application, name, version, previousVersion, nextVersion, keyList, environmentConfigurationList);
	}
	
	@Override
	@Transactional(readOnly = true)
	@EnvGrant(EnvFunction.env_config)
	public EnvironmentConfiguration getEnvironmentConfiguration(@AppGrantParam String application, @EnvGrantParam String environment) {
		checkApplication(application);
		checkEnvironment(application, environment);
		// Application name
		MapSqlParameterSource applicationCriteria = new MapSqlParameterSource(APPLICATION, application);
		MapSqlParameterSource environmentCriteria = applicationCriteria.addValue(ENVIRONMENT, environment);
		String name = getApplicationName(application);
		// List of version
		List<Version> versions = getNamedParameterJdbcTemplate().query(SQL.VERSIONS, applicationCriteria, new RowMapper<Version>() {
			@Override
			public Version mapRow(ResultSet rs, int i) throws SQLException {
				return new Version(rs.getString(NAME));
			}
		});
		// List of keys for the environment
		/*
		 * The query does not depend on the environment.
		 */
		List<Key> keyList = getNamedParameterJdbcTemplate().query(SQL.KEYS_FOR_ENVIRONMENT, environmentCriteria, new KeyRowMapper());
		// Gets the matrix of key x version
		MatrixConfiguration matrix = keyVersionConfiguration(application);
		// Gets the list of values per environment x application
		List<Map<String, Object>> valuesMaps = getNamedParameterJdbcTemplate().queryForList(SQL.CONFIG_FOR_ENVIRONMENT, environmentCriteria);
		final Map<String,Map<String,String>> versionValues = new TreeMap<String, Map<String,String>>();
		for (Map<String, Object> values : valuesMaps) {
			String version = (String) values.get(VERSION);
			String key = (String) values.get(KEY);
			String value = (String) values.get(VALUE);
			Map<String, String> versionConfiguration = versionValues.get(version);
			if (versionConfiguration == null) {
				versionConfiguration = new TreeMap<String, String>();
				versionValues.put(version, versionConfiguration);
			}
			versionConfiguration.put(key, value);
		}
		
		// List of conditional values per key x version
		List<IndexedValues<ConditionalValue>> versionConfigurationList = new ArrayList<IndexedValues<ConditionalValue>>();
		// .. for each version
		for (Version version: versions) {
			String versionName = version.getName();
			Map<String, ConditionalValue> cValues = new TreeMap<String, ConditionalValue>();
			// ... for each key
			for (Key key: keyList) {
				String keyName = key.getName();
				// Is the key activated for this version?
				boolean enabled = matrix.isEnabled (versionName, keyName);
				// Gets the value for an enabled key
				String value = "";
				if (enabled) {
					Map<String, String> valuePerKey = versionValues.get(versionName);
					if (valuePerKey != null && valuePerKey.containsKey(keyName)) {
						value = valuePerKey.get(keyName);
					}
				}
				// Conditional value
				ConditionalValue cValue = new ConditionalValue(enabled, value);
				cValues.put(keyName, cValue);
			}
			// OK for this version
			IndexedValues<ConditionalValue> versionConditionalValues = new IndexedValues<ConditionalValue>(versionName, cValues);
			versionConfigurationList.add(versionConditionalValues);
		}
		
		// Previous version
		String previousEnvironment = getFirstItem(SQL.ENVIRONMENT_PREVIOUS, environmentCriteria, String.class);
		while (previousEnvironment != null && !hasEnvironmentAccess(application, previousEnvironment, EnvFunction.env_config)) {
			previousEnvironment = getFirstItem(SQL.ENVIRONMENT_PREVIOUS, environmentCriteria.addValue(ENVIRONMENT, previousEnvironment), String.class);
		}
		// Next version
		environmentCriteria.addValue(ENVIRONMENT, environment);
		String nextEnvironment = getFirstItem(SQL.ENVIRONMENT_NEXT, environmentCriteria, String.class);
		while (nextEnvironment != null && !hasEnvironmentAccess(application, nextEnvironment, EnvFunction.env_config)) {
			nextEnvironment = getFirstItem(SQL.ENVIRONMENT_NEXT, environmentCriteria.addValue(ENVIRONMENT, nextEnvironment), String.class);
		}
		// OK
		return new EnvironmentConfiguration(application, name, environment, previousEnvironment, nextEnvironment, keyList, versionConfigurationList);
	}
	
	@Override
	@Transactional(readOnly = true)
	@AppGrant(AppFunction.app_view)
	public KeyConfiguration getKeyConfiguration(@AppGrantParam String application, String keyName) {
		checkApplication(application);
		checkKey(application, keyName);
		
		// Application name
		MapSqlParameterSource applicationCriteria = new MapSqlParameterSource(APPLICATION, application);
		MapSqlParameterSource keyCriteria = applicationCriteria.addValue(KEY, keyName);
		String name = getApplicationName(application);
		
		// Key information
		Key key = getNamedParameterJdbcTemplate().queryForObject(SQL.KEY, keyCriteria, new KeyRowMapper());
		
		// List of environments
		List<Environment> environments = getNamedParameterJdbcTemplate().query(SQL.ENVIRONMENTS, applicationCriteria, new RowMapper<Environment>() {
			@Override
			public Environment mapRow(ResultSet rs, int i) throws SQLException {
				return new Environment(rs.getString(NAME));
			}
		});
		
		// Filtering
		environments = filterEnvironments(application, environments);
		
		// List of versions for the key
		List<Version> versionList = getNamedParameterJdbcTemplate().query(SQL.VERSIONS_FOR_KEY, keyCriteria, new RowMapper<Version>() {
			@Override
			public Version mapRow(ResultSet rs, int i) throws SQLException {
				return new Version(rs.getString(VERSION));
			}
		});
		// Gets the list of values per version x environment
		List<Map<String, Object>> valuesMaps = getNamedParameterJdbcTemplate().queryForList(SQL.CONFIG_FOR_KEY, keyCriteria);
		final Map<String,Map<String,String>> environmentValues = new TreeMap<String, Map<String,String>>();
		for (Map<String, Object> values : valuesMaps) {
			String environment = (String) values.get(ENVIRONMENT);
			String version = (String) values.get(VERSION);
			String value = (String) values.get(VALUE);
			Map<String, String> environmentConfiguration = environmentValues.get(environment);
			if (environmentConfiguration == null) {
				environmentConfiguration = new TreeMap<String, String>();
				environmentValues.put(environment, environmentConfiguration);
			}
			environmentConfiguration.put(version, value);
		}
		// List of values per environment x key x version
		List<IndexedValues<String>> environmentConfigurationList = Lists.transform(environments, new Function<Environment, IndexedValues<String>>() {

			@Override
			public IndexedValues<String> apply(Environment environment) {
				String name = environment.getName();
				Map<String, String> values = environmentValues.get(name);
				if (values == null) {
					values = Collections.emptyMap();
				}
				return new IndexedValues<String>(name, values);
			}
		});
		
		// Previous & next version
		String previousKey = getFirstItem(SQL.KEY_PREVIOUS, keyCriteria, String.class);
		String nextKey= getFirstItem(SQL.KEY_NEXT, keyCriteria, String.class);
		
		// OK
		return new KeyConfiguration(application, name, key, previousKey, nextKey, versionList, environmentConfigurationList);
	}
	
	@Override
	@Transactional
	@Audit(category = EventCategory.CONFIG_VALUE, action = EventAction.UPDATE,
		collection = "#updates.updates",
		application = "#application",
		environment = "#item.environment",
		version = "#item.version",
		key = "#item.key",
		result = "#result.success")	
	public Ack updateConfiguration(String application, ConfigurationUpdates updates) {
		NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// Checks
		checkApplication(application);
		// Main criteria
		MapSqlParameterSource appCriteria = new MapSqlParameterSource(APPLICATION, application);
		// Updates
		for (ConfigurationUpdate update : updates.getUpdates()) {
			String environment = update.getEnvironment();
			String version = update.getVersion();
			String key = update.getKey();
			String value = update.getValue();
			// Value controls
			checkEnvironment(application, environment);
			checkVersion(application, version);
			checkKey(application, key);
			checkMatrix(application, version, key);
			// Security check
			checkEnvironmentAccess (application, environment, EnvFunction.env_config);
			// Key value validation
			validateValue(application, key, value);
			// Criteria
			MapSqlParameterSource criteria = appCriteria
					.addValue(ENVIRONMENT, environment)
					.addValue(VERSION, version)
					.addValue(KEY, key);
			// Deletion
			t.update(SQL.CONFIG_REMOVE_VALUE, criteria);
			// Update
			t.update(SQL.CONFIG_INSERT_VALUE, criteria.addValue(VALUE, value));
		}
		// OK
		return Ack.OK;
	}

	@Override
	@Transactional
	@AppGrant(AppFunction.app_matrix)
	@Audit(category = EventCategory.MATRIX, action = EventAction.CREATE, application= "#application", version = "#version", key = "#key", result = "#result.success")
	public Ack addKeyVersion(@AppGrantParam String application, String version, String key) {
		checkApplication(application);
		checkVersion(application, version);
		checkKey(application, key);
		try {
			int count = getNamedParameterJdbcTemplate().update(
					SQL.VERSION_KEY_ADD,
					new MapSqlParameterSource()
						.addValue(APPLICATION, application)
						.addValue(VERSION, version)
						.addValue(KEY, key)
					);
			return Ack.one(count);
		} catch (DuplicateKeyException ex) {
			throw new KeyAlreadyInVersionException(application, version, key);
		}
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_matrix)
	@Audit(category = EventCategory.MATRIX, action = EventAction.DELETE, application= "#application", version = "#version", key = "#key", result = "#result.success")
	public Ack removeKeyVersion(@AppGrantParam String application, String version, String key) {
		checkApplication(application);
		checkVersion(application, version);
		checkKey(application, key);
		int count = getNamedParameterJdbcTemplate().update(
				SQL.VERSION_KEY_REMOVE,
				new MapSqlParameterSource()
					.addValue(APPLICATION, application)
					.addValue(VERSION, version)
					.addValue(KEY, key)
				);
		return Ack.one(count);
	}

	@Override
	@Transactional(readOnly = true)
	public String getKey(String id, String version, String environment, String key) {
		// Checks for existing data
		checkApplication (id);
		checkVersion (id, version);
		checkEnvironment (id, environment);
		// Security check
		checkEnvironmentAccess(id, environment, EnvFunction.env_view);
		// Query
		try {
			return getNamedParameterJdbcTemplate().queryForObject(
				SQL.GET_KEY,
				new MapSqlParameterSource()
					.addValue(APPLICATION, id)
					.addValue(VERSION, version)
					.addValue(ENVIRONMENT, environment)
					.addValue(KEY, key),
				String.class
				);
		} catch (EmptyResultDataAccessException ex) {
			throw new KeyNotFoundException (id, key);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public ConfigurationSet getEnv(String id, String version, String environment) {
		// Checks for existing data
		checkApplication (id);
		checkVersion (id, version);
		checkEnvironment (id, environment);
		// Application name
		String name = getApplicationName(id);
		// Checks for security
		checkEnvironmentAccess(id, environment, EnvFunction.env_view);
		// List of configuration documented values
		List<ConfigurationValue> values = getNamedParameterJdbcTemplate().query(SQL.GET_ENV, 
				new MapSqlParameterSource()
					.addValue(APPLICATION, id)
					.addValue(VERSION, version)
					.addValue(ENVIRONMENT, environment),
				new RowMapper<ConfigurationValue> () {
					@Override
					public ConfigurationValue mapRow(ResultSet rs, int index) throws SQLException {
						return new ConfigurationValue(
								rs.getString(1),
								rs.getString(2),
								rs.getString(3));
					}
				});
		// OK
		return new ConfigurationSet(id, name, environment, version, values);
	}
	
	protected void checkNotExist (String sql, MapSqlParameterSource params, CoreException ex) {
		List<Map<String, Object>> list = getNamedParameterJdbcTemplate().queryForList(sql, params);
		if (!list.isEmpty()) {
			throw ex;
		}
	}

	protected void checkApplication(String id) {
		check (
				SQL.APPLICATION_NAME,
				new MapSqlParameterSource(ID, id),
				new ApplicationNotFoundException(id));
	}

	protected void checkVersion(String application, String version) {
		check (
				SQL.VERSION_EXISTS,
				new MapSqlParameterSource(NAME, version).addValue(APPLICATION, application),
				new VersionNotFoundException(application, version));
	}

	protected void checkKey(String application, String key) {
		check (
				SQL.KEY_EXISTS_BY_ID,
				new MapSqlParameterSource(NAME, key).addValue(APPLICATION, application),
				new KeyNotFoundException(application, key));
	}

	protected void checkEnvironment(String application, String environment) {
		check (
				SQL.ENVIRONMENT_EXISTS,
				new MapSqlParameterSource(NAME, environment).addValue(APPLICATION, application),
				new EnvironmentNotFoundException(application, environment));
	}

	protected void checkMatrix(String application, String version, String key) {
		check (
				SQL.MATRIX_EXISTS,
				new MapSqlParameterSource(APPLICATION, application).addValue(VERSION, version).addValue(KEY, key),
				new MatrixNotFoundException(application, version, key));
	}

	protected void check(String sql,
			SqlParameterSource sqlParameterSource,
			CoreException exception) {
		List<Map<String, Object>> list = getNamedParameterJdbcTemplate().queryForList(sql, sqlParameterSource);
		if (list.isEmpty()) {
			throw exception;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserApplicationRights> getUserApplicationRights(String user) {
		List<UserApplicationRights> result = new ArrayList<UserApplicationRights>();
		// Gets the list of applications as id:name pairs
		List<Map<String, Object>> idNameList = getJdbcTemplate().queryForList(SQL.APPLICATIONS_FOR_USER_RIGHTS);
		for (Map<String, Object> idName : idNameList) {
			String id = (String) idName.get(ID);
			String name = (String) idName.get(NAME);
			// Is this application allowed for administration of users?
			if (hasApplicationAccess(id, AppFunction.app_users)) {
				EnumSet<AppFunction> fns = grantService.getAppFunctions(id, user);
				result.add(new UserApplicationRights(id, name, fns));
			}
		}
		// OK
		return result;
	}
	
	@Override
	public Localizable validateTypeParameter(String typeId, String parameter) {
		return valueTypeFactory.getValueType(typeId).validateParameter(parameter);
	}
	
	@Override
	public Localizable validateTypeValue(String typeId, String parameter, String value) {
		return valueTypeFactory.getValueType(typeId).validate(value, parameter);
	}
	
	@Override
	public ValueTypeDescriptions getValueTypes() {
		return valueTypeFactory.getValueTypeDescriptions();
	}

}
