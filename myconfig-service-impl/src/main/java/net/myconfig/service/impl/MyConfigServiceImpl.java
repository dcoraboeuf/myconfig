package net.myconfig.service.impl;

import static net.myconfig.service.impl.SQLColumns.APPLICATION;
import static net.myconfig.service.impl.SQLColumns.DESCRIPTION;
import static net.myconfig.service.impl.SQLColumns.ENVIRONMENT;
import static net.myconfig.service.impl.SQLColumns.ID;
import static net.myconfig.service.impl.SQLColumns.KEY;
import static net.myconfig.service.impl.SQLColumns.KEY_NUMBER;
import static net.myconfig.service.impl.SQLColumns.NAME;
import static net.myconfig.service.impl.SQLColumns.VALUE;
import static net.myconfig.service.impl.SQLColumns.VERSION;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.exception.ApplicationNameAlreadyDefinedException;
import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.service.exception.CoreException;
import net.myconfig.service.exception.EnvironmentAlreadyDefinedException;
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
import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.service.model.ConfigurationValue;
import net.myconfig.service.model.EnvironmentConfiguration;
import net.myconfig.service.model.EnvironmentSummary;
import net.myconfig.service.model.Key;
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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

@Service
public class MyConfigServiceImpl extends AbstractDaoService implements MyConfigService {

	private final String versionNumber;

	@Autowired
	public MyConfigServiceImpl(@Value("${app.version}") String versionNumber, DataSource dataSource, Validator validator) {
		super(dataSource, validator);
		this.versionNumber = versionNumber;
	}
	
	@Override
	public String getVersion() {
		return versionNumber;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ApplicationSummary> getApplications() {
		return getJdbcTemplate().query(SQL.APPLICATIONS, new RowMapper<ApplicationSummary>() {

			@Override
			public ApplicationSummary mapRow(ResultSet rs, int i)
					throws SQLException {
				return new ApplicationSummary(rs.getInt(ID), rs.getString(NAME));
			}
		});
	}
	
	@Override
	@Transactional(readOnly = true)
	public ApplicationConfiguration getApplicationConfiguration(int id) {
		NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// ID
		MapSqlParameterSource idCriteria = new MapSqlParameterSource(ID, id);
		// Gets the name
		String name = getApplicationName(id);
		// Versions	
		List<VersionSummary> versionSummaryList = t.query(SQL.VERSION_SUMMARIES, idCriteria, new RowMapper<VersionSummary>(){
			@Override
			public VersionSummary mapRow(ResultSet rs, int i) throws SQLException {
				return new VersionSummary(rs.getString(NAME), rs.getInt(KEY_NUMBER));
			}
		});
		// Environments
		List<EnvironmentSummary> environmentSummaryList = t.query(SQL.ENVIRONMENT_SUMMARIES, idCriteria, new RowMapper<EnvironmentSummary>(){
			@Override
			public EnvironmentSummary mapRow(ResultSet rs, int i) throws SQLException {
				return new EnvironmentSummary(rs.getString(NAME));
			}
		});
		// Keys
		List<KeySummary> keySummaryList = t.query(SQL.KEY_SUMMARIES, idCriteria, new RowMapper<KeySummary>(){
			@Override
			public KeySummary mapRow(ResultSet rs, int i) throws SQLException {
				return new KeySummary(rs.getString(NAME), rs.getString(SQLColumns.DESCRIPTION), rs.getInt(SQLColumns.VERSION_NUMBER));
			}
		});
		// OK
		return new ApplicationConfiguration(id, name,
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
		return new ApplicationSummary(id, name);
	}
	
	@Override
	@Transactional
	public Ack deleteApplication(int id) {
		int count = getNamedParameterJdbcTemplate().update(SQL.APPLICATION_DELETE, new MapSqlParameterSource (ID, id));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
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
	public Ack deleteVersion(int id, String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.VERSION_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
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
	public Ack deleteEnvironment(int id, String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.ENVIRONMENT_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional
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
	public Ack deleteKey(int id, String name) {
		checkApplication(id);
		int count = getNamedParameterJdbcTemplate().update(SQL.KEY_DELETE, idNameSource(id, name));
		return Ack.one (count);
	}
	
	@Override
	@Transactional(readOnly = true)
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
	public VersionConfiguration getVersionConfiguration(int application, String version) {
		checkApplication(application);
		checkVersion(application, version);
		// Application name
		MapSqlParameterSource applicationCriteria = new MapSqlParameterSource("application", application);
		MapSqlParameterSource versionCriteria = applicationCriteria.addValue(VERSION, version);
		String name = getApplicationName(application);
		// List of keys for the version
		List<Key> keyList = getNamedParameterJdbcTemplate().query(SQL.KEYS_FOR_VERSION, versionCriteria, new RowMapper<Key>() {
			@Override
			public Key mapRow(ResultSet rs, int i) throws SQLException {
				return new Key(rs.getString(NAME), rs.getString(DESCRIPTION));
			}
		});
		// Gets the list of values per version x application
		List<Map<String, Object>> valuesMaps = getNamedParameterJdbcTemplate().queryForList(SQL.CONFIG_FOR_VERSION, applicationCriteria);
		Map<String,Map<String,String>> environmentValues = new TreeMap<String, Map<String,String>>();
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
		List<EnvironmentConfiguration> environmentConfigurationList = ImmutableList.copyOf(
				Iterables.transform(environmentValues.entrySet(), new Function<Map.Entry<String,Map<String,String>>, EnvironmentConfiguration>() {
					@Override
					public EnvironmentConfiguration apply(Entry<String, Map<String, String>> entry) {
						return new EnvironmentConfiguration(entry.getKey(), entry.getValue());
					}
				})
			);
		// Previous & next version
		String previousVersion = getFirstItem(SQL.VERSION_PREVIOUS, versionCriteria, String.class);
		String nextVersion = getFirstItem(SQL.VERSION_NEXT, versionCriteria, String.class);
		// OK
		return new VersionConfiguration(application, name, version, previousVersion, nextVersion, keyList, environmentConfigurationList);
	}
	
	@Override
	@Transactional
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
	public ConfigurationSet getEnv(String application, String version, String environment) {
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

	protected void check(String sql,
			SqlParameterSource sqlParameterSource,
			CoreException exception) {
		List<Map<String, Object>> list = getNamedParameterJdbcTemplate().queryForList(sql, sqlParameterSource);
		if (list.isEmpty()) {
			throw exception;
		}
	}

}
