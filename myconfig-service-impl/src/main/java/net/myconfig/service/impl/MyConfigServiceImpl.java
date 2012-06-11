package net.myconfig.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.exception.KeyNotFoundException;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.service.model.ConfigurationValue;
import net.myconfig.service.model.VersionSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyConfigServiceImpl extends AbstractDaoService implements MyConfigService {

	private final String versionNumber;

	@Autowired
	public MyConfigServiceImpl(@Value("${app.version}") String versionNumber, DataSource dataSource) {
		super(dataSource);
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
				return new ApplicationSummary(rs.getInt("id"), rs.getString("name"));
			}
		});
	}
	
	@Override
	@Transactional(readOnly = true)
	public ApplicationConfiguration getApplicationConfiguration(int id) {
		NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// ID
		MapSqlParameterSource idCriteria = new MapSqlParameterSource("id", id);
		// Gets the name
		String name = t.queryForObject(SQL.APPLICATION_NAME, idCriteria, String.class);
		// Versions	
		List<VersionSummary> versionSummaryList = t.query(SQL.VERSIONS, idCriteria, new RowMapper<VersionSummary>(){
			@Override
			public VersionSummary mapRow(ResultSet rs, int i) throws SQLException {
				// FIXME Number of keys	
				return new VersionSummary(rs.getString("name"), 0);
			}
		});
		// OK
		return new ApplicationConfiguration(id, name,
				versionSummaryList);
	}
	
	@Override
	@Transactional
	public ApplicationSummary createApplication(String name) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			getNamedParameterJdbcTemplate().update(
				SQL.APPLICATION_CREATE,
				new MapSqlParameterSource("name", name),
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
		int count = getNamedParameterJdbcTemplate().update(SQL.APPLICATION_DELETE, new MapSqlParameterSource ("id", id));
		return Ack.validate (count == 1);
	}
	
	@Override
	@Transactional
	public Ack createVersion(int id, String name) {
		try {
			int count = getNamedParameterJdbcTemplate().update(SQL.VERSION_CREATE,
				new MapSqlParameterSource()
					.addValue("id", id)
					.addValue("name", name));
			return Ack.validate (count == 1);
		} catch (DuplicateKeyException ex) {
			throw new VersionAlreadyDefinedException (name);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public String getKey(String application, String version, String environment, String key) {
		try {
			return getNamedParameterJdbcTemplate().queryForObject(
				SQL.GET_KEY,
				new MapSqlParameterSource()
					.addValue("application", application)
					.addValue("version", version)
					.addValue("environment", environment)
					.addValue("key", key),
				String.class
				);
		} catch (EmptyResultDataAccessException ex) {
			throw new KeyNotFoundException (application, version, environment, key);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public ConfigurationSet getEnv(String application, String version, String environment) {
		// List of configuration documented values
		List<ConfigurationValue> values = getNamedParameterJdbcTemplate().query(SQL.GET_ENV, 
				new MapSqlParameterSource()
					.addValue("application", application)
					.addValue("version", version)
					.addValue("environment", environment),
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
		return new ConfigurationSet(values);
	}

}
