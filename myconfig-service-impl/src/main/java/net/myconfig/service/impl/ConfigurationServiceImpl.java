package net.myconfig.service.impl;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.model.EventCategory;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.audit.Audit;
import net.myconfig.service.cache.CacheNames;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
	@Cacheable(CacheNames.CONFIGURATION)
	@Transactional(readOnly = true)
	public String getParameter(ConfigurationKey configurationKey) {
		String value = getFirstItem(SQL.CONFIGURATION_VALUE, new MapSqlParameterSource(SQLColumns.NAME, configurationKey.getKey()), String.class);
		if (value == null) {
			return configurationKey.getDefault();
		} else {
			return value;
		}
	}

	@Override
	@CacheEvict(value = CacheNames.CONFIGURATION, key = "#configurationKey")
	@Transactional
	@Audit(EventCategory.CONFIGURATION_SET)
	public void setParameter(ConfigurationKey configurationKey, String value) {
		String name = configurationKey.getKey();
		String existingValue = getFirstItem(SQL.CONFIGURATION_VALUE, new MapSqlParameterSource(SQLColumns.NAME, name), String.class);
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue(SQLColumns.NAME, name).addValue(SQLColumns.VALUE, value);
		if (existingValue == null) {
			getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_ADD, parameters);
		} else {
			getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_UPDATE, parameters);
		}
	}

}
