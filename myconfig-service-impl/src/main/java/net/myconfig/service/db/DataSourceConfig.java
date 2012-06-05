package net.myconfig.service.db;

import javax.sql.DataSource;

import net.myconfig.service.api.ConfigurationService;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Bean
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(configurationService.getDBDriver());
		ds.setUrl(configurationService.getDBURL());
		ds.setUsername(configurationService.getDBUser());
		ds.setPassword(configurationService.getDBPassword());
		ds.setDefaultAutoCommit(false);
		ds.setInitialSize(configurationService.getDBPoolInitial());
		ds.setMaxActive(configurationService.getDBPoolMax());
		return ds;
	}

}
