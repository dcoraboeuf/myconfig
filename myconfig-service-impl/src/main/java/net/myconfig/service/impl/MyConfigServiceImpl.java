package net.myconfig.service.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import net.myconfig.service.api.MyConfigService;

@Service
public class MyConfigServiceImpl extends AbstractDaoService implements MyConfigService {

	@Autowired
	public MyConfigServiceImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public String getKey(String application, String version, String environment, String key) {
		return getNamedParameterJdbcTemplate().queryForObject(
				SQL.GET_KEY,
				new MapSqlParameterSource()
					.addValue("application", application)
					.addValue("version", version)
					.addValue("environment", environment)
					.addValue("key", key),
				String.class
				);
	}

}
