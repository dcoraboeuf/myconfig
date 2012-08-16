package net.myconfig.service.impl;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigurationServiceImpl extends AbstractDaoService implements ConfigurationService {

	@Autowired
	public ConfigurationServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional(readOnly = true)
	public String getParameter(String name, String defaultValue) {
		String value = getFirstItem(SQL.CONFIGURATION_VALUE, new MapSqlParameterSource(SQLColumns.NAME, name), String.class);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}
	
	@Override
	@Transactional
	public void setParameter(String name, String value) {
		String existingValue = getFirstItem(SQL.CONFIGURATION_VALUE, new MapSqlParameterSource(SQLColumns.NAME, name), String.class);
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue(SQLColumns.NAME, name).addValue(SQLColumns.VALUE, value);
		if (existingValue == null) {
			getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_ADD, parameters);
		} else {
			getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_UPDATE, parameters);
		}
	}

}
