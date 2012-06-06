package net.myconfig.service.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.exception.KeyNotFoundException;

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

}
