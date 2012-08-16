package net.myconfig.service.impl;

import static net.myconfig.service.db.SQLColumns.APPLICATION;
import static net.myconfig.service.db.SQLColumns.CONFIG_COUNT;
import static net.myconfig.service.db.SQLColumns.DESCRIPTION;
import static net.myconfig.service.db.SQLColumns.ENVIRONMENT;
import static net.myconfig.service.db.SQLColumns.ENVIRONMENT_COUNT;
import static net.myconfig.service.db.SQLColumns.ID;
import static net.myconfig.service.db.SQLColumns.KEY;
import static net.myconfig.service.db.SQLColumns.KEY_COUNT;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.VALUE;
import static net.myconfig.service.db.SQLColumns.VALUE_COUNT;
import static net.myconfig.service.db.SQLColumns.VERSION;
import static net.myconfig.service.db.SQLColumns.VERSION_COUNT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.EnvGrant;
import net.myconfig.service.api.security.EnvGrantParam;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserGrant;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.exception.ApplicationNameAlreadyDefinedException;
import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.service.exception.CoreException;
import net.myconfig.service.exception.EnvironmentAlreadyDefinedException;
import net.myconfig.service.exception.EnvironmentNotDefinedException;
import net.myconfig.service.exception.EnvironmentNotFoundException;
import net.myconfig.service.exception.KeyAlreadyDefinedException;
import net.myconfig.service.exception.KeyAlreadyInVersionException;
import net.myconfig.service.exception.KeyNotDefinedException;
import net.myconfig.service.exception.KeyNotFoundException;
import net.myconfig.service.exception.VersionAlreadyDefinedException;
import net.myconfig.service.exception.VersionNotDefinedException;
import net.myconfig.service.exception.VersionNotFoundException;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.ConditionalValue;
import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.service.model.ConfigurationUpdate;
import net.myconfig.service.model.ConfigurationUpdates;
import net.myconfig.service.model.ConfigurationValue;
import net.myconfig.service.model.Environment;
import net.myconfig.service.model.EnvironmentConfiguration;
import net.myconfig.service.model.EnvironmentSummary;
import net.myconfig.service.model.IndexedValues;
import net.myconfig.service.model.Key;
import net.myconfig.service.model.KeyConfiguration;
import net.myconfig.service.model.KeySummary;
import net.myconfig.service.model.MatrixConfiguration;
import net.myconfig.service.model.MatrixVersionConfiguration;
import net.myconfig.service.model.Version;
import net.myconfig.service.model.VersionConfiguration;
import net.myconfig.service.model.VersionSummary;
import net.myconfig.service.validation.ApplicationValidation;
import net.myconfig.service.validation.EnvironmentValidation;
import net.myconfig.service.validation.KeyValidation;
import net.myconfig.service.validation.VersionValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class MyConfigServiceImpl extends AbstractSecureService implements MyConfigService {

	private final String versionNumber;

	@Autowired
	public MyConfigServiceImpl(@Value("${app.version}") String versionNumber, DataSource dataSource, Validator validator, SecuritySelector securitySelector) {
		super(dataSource, validator, securitySelector);
		this.versionNumber = versionNumber;
	}
	
	@Override
	public String getVersion() {
		return versionNumber;
	}
	
	@Override
	@Transactional(readOnly = true)
	@UserGrant(UserFunction.app_list)
	public List<ApplicationSummary> getApplications() {
		return getJdbcTemplate().query(SQL.APPLICATIONS, new RowMapper<ApplicationSummary>() {

			@Override
			public ApplicationSummary mapRow(ResultSet rs, int i)
					throws SQLException {
				return new ApplicationSummary(
						rs.getInt(ID),
						rs.getString(NAME),
						rs.getInt(VERSION_COUNT),
						rs.getInt(KEY_COUNT),
						rs.getInt(ENVIRONMENT_COUNT),
						rs.getInt(CONFIG_COUNT),
						rs.getInt(VALUE_COUNT));
			}
		});
	}
	
	@Override
	@Transactional(readOnly = true)
	@AppGrant(AppFunction.app_view)
	public ApplicationConfiguration getApplicationConfiguration(int application) {
		NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// ID
		MapSqlParameterSource applicationCriteria = new MapSqlParameterSource(APPLICATION, application);
		// Gets the name
		String name = getApplicationName(application);
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
				int versionCount = rs.getInt(SQLColumns.VERSION_COUNT);
				int environmentCount = rs.getInt(SQLColumns.ENVIRONMENT_COUNT);
				int valueCount = rs.getInt(SQLColumns.VALUE_COUNT);
				int configCount = versionCount * environmentCount;
				return new KeySummary(key, description, versionCount, configCount, valueCount);
			}
		});
		// OK
		return new ApplicationConfiguration(application, name,
				versionSummaryList, environmentSummaryList, keySummaryList);
	}

	protected String getApplicationName(int id) {
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
	public ApplicationSummary createApplication(String name) {
		validate(ApplicationValidation.class, NAME, name);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			getNamedParameterJdbcTemplate().update(
				SQL.APPLICATION_CREATE,
				new MapSqlParameterSource(NAME, name),
				keyHolder);
		} catch (DuplicateKeyException ex) {
			throw new ApplicationNameAlreadyDefinedException (name);
		}
		int id = keyHolder.getKey().intValue();
		// Initial grants
		for (AppFunction fn : AppFunction.values()) {
			grantAppFunction (id, fn);
		}
		// OK
		return new ApplicationSummary(id, name, 0, 0, 0, 0, 0);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_delete)
	public Ack deleteApplication(int id) {
		int count = getNamedParameterJdbcTemplate().update(SQL.APPLICATION_DELETE, new MapSqlParameterSource (ID, id));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	public Ack createVersion(int id, String name) {
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

	protected MapSqlParameterSource idNameSource(int id, String name) {
		return new MapSqlParameterSource()
			.addValue(ID, id)
			.addValue(NAME, name);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	public Ack deleteVersion(int id, String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.VERSION_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	public Ack createEnvironment(int id, String name) {
		validate(EnvironmentValidation.class, NAME, name);
		checkApplication(id);
		try {
			int count = getNamedParameterJdbcTemplate().update(SQL.ENVIRONMENT_CREATE,
				idNameSource(id, name));
			return Ack.one (count);
		} catch (DuplicateKeyException ex) {
			throw new EnvironmentAlreadyDefinedException(id, name);
		}
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	public Ack deleteEnvironment(int id, String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.ENVIRONMENT_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	public Ack createKey(int id, String name, String description) {
		validate(KeyValidation.class, NAME, name);
		validate(KeyValidation.class, DESCRIPTION, description);
		checkApplication(id);
		try {
			int count = getNamedParameterJdbcTemplate().update(SQL.KEY_CREATE,
				idNameSource(id, name).addValue(DESCRIPTION, description));
			return Ack.one (count);
		} catch (DuplicateKeyException ex) {
			throw new KeyAlreadyDefinedException (id, name);
		}
	}
	
	@Override
	@Transactional
	@AppGrant(AppFunction.app_config)
	public Ack updateKey(int application, String name, String description) {
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
	public Ack deleteKey(int id, String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.KEY_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional(readOnly = true)
	@AppGrant(AppFunction.app_view)
	public MatrixConfiguration keyVersionConfiguration(int id) {
		checkApplication(id);
		// Criteria
		MapSqlParameterSource idCriteria = new MapSqlParameterSource("application", id);
		String name = getApplicationName(id);
		// List of keys		
		List<Key> keyList = getNamedParameterJdbcTemplate().query(SQL.KEYS, idCriteria, new RowMapper<Key>() {
			@Override
			public Key mapRow(ResultSet rs, int i) throws SQLException {
				return new Key(rs.getString(NAME), rs.getString(DESCRIPTION));
			}
		});
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
	public VersionConfiguration getVersionConfiguration(int application, String version) {
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
		List<Key> keyList = getNamedParameterJdbcTemplate().query(SQL.KEYS_FOR_VERSION, versionCriteria, new RowMapper<Key>() {
			@Override
			public Key mapRow(ResultSet rs, int i) throws SQLException {
				return new Key(rs.getString(NAME), rs.getString(DESCRIPTION));
			}
		});
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
	@EnvGrant(EnvFunction.env_view)
	public EnvironmentConfiguration getEnvironmentConfiguration(int application, @EnvGrantParam String environment) {
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
		List<Key> keyList = getNamedParameterJdbcTemplate().query(SQL.KEYS_FOR_ENVIRONMENT, environmentCriteria, new RowMapper<Key>() {
			@Override
			public Key mapRow(ResultSet rs, int i) throws SQLException {
				return new Key(rs.getString(NAME), rs.getString(DESCRIPTION));
			}
		});
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
		
		// Previous & next version
		String previousEnvironment = getFirstItem(SQL.ENVIRONMENT_PREVIOUS, environmentCriteria, String.class);
		String nextEnvironment = getFirstItem(SQL.ENVIRONMENT_NEXT, environmentCriteria, String.class);
		// OK
		return new EnvironmentConfiguration(application, name, environment, previousEnvironment, nextEnvironment, keyList, versionConfigurationList);
	}
	
	@Override
	@Transactional(readOnly = true)
	@AppGrant(AppFunction.app_view)
	public KeyConfiguration getKeyConfiguration(int application, String keyName) {
		checkApplication(application);
		checkKey(application, keyName);
		
		// Application name
		MapSqlParameterSource applicationCriteria = new MapSqlParameterSource(APPLICATION, application);
		MapSqlParameterSource keyCriteria = applicationCriteria.addValue(KEY, keyName);
		String name = getApplicationName(application);
		
		// Key information
		Key key = getNamedParameterJdbcTemplate().queryForObject(SQL.KEY, keyCriteria, new RowMapper<Key>() {
			@Override
			public Key mapRow(ResultSet rs, int index) throws SQLException {
				return new Key(rs.getString(NAME), rs.getString(DESCRIPTION));
			}
		});
		
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
	public Ack updateConfiguration(int application, ConfigurationUpdates updates) {
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
			// FIXME checkMatrix(application, version, key);
			// Security check
			checkEnvironmentAccess (application, environment, EnvFunction.env_config);
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
	public Ack addKeyVersion(int application, String version, String key) {
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
	public Ack removeKeyVersion(int application, String version, String key) {
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
	@EnvGrant(EnvFunction.env_view)
	public String getKey(String application, String version, String environment, String key) {
		// Checks for existing data
		checkApplication (application);
		checkVersion (application, version);
		checkEnvironment (application, environment);
		// Query
		try {
			return getNamedParameterJdbcTemplate().queryForObject(
				SQL.GET_KEY,
				new MapSqlParameterSource()
					.addValue(APPLICATION, application)
					.addValue(VERSION, version)
					.addValue(ENVIRONMENT, environment)
					.addValue(KEY, key),
				String.class
				);
		} catch (EmptyResultDataAccessException ex) {
			throw new KeyNotFoundException (application, version, environment, key);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	@EnvGrant(EnvFunction.env_view)
	public ConfigurationSet getEnv(String application, String version, @EnvGrantParam String environment) {
		// Checks for existing data
		checkApplication (application);
		checkVersion (application, version);
		checkEnvironment (application, environment);
		// List of configuration documented values
		List<ConfigurationValue> values = getNamedParameterJdbcTemplate().query(SQL.GET_ENV, 
				new MapSqlParameterSource()
					.addValue(APPLICATION, application)
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
		return new ConfigurationSet(application, environment, version, values);
	}

	protected void checkApplication(String application) {
		check (
				SQL.APPLICATION_EXISTS,
				new MapSqlParameterSource(NAME, application),
				new ApplicationNotFoundException(application));
	}

	protected void checkApplication(int id) {
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

	protected void checkVersion(int application, String version) {
		check (
				SQL.VERSION_EXISTS_BY_ID,
				new MapSqlParameterSource(NAME, version).addValue(APPLICATION, application),
				new VersionNotDefinedException(application, version));
	}

	protected void checkKey(int application, String key) {
		check (
				SQL.KEY_EXISTS_BY_ID,
				new MapSqlParameterSource(NAME, key).addValue(APPLICATION, application),
				new KeyNotDefinedException(application, key));
	}

	protected void checkEnvironment(String application, String environment) {
		check (
				SQL.ENVIRONMENT_EXISTS,
				new MapSqlParameterSource(NAME, environment).addValue(APPLICATION, application),
				new EnvironmentNotFoundException(application, environment));
	}

	protected void checkEnvironment(int application, String environment) {
		check (
				SQL.ENVIRONMENT_EXISTS_BY_ID,
				new MapSqlParameterSource(NAME, environment).addValue(APPLICATION, application),
				new EnvironmentNotDefinedException(application, environment));
	}

	protected void check(String sql,
			SqlParameterSource sqlParameterSource,
			CoreException exception) {
		List<Map<String, Object>> list = getNamedParameterJdbcTemplate().queryForList(sql, sqlParameterSource);
		if (list.isEmpty()) {
			throw exception;
		}
	}

}
