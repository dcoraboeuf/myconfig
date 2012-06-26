package net.myconfig.service.config;

import javax.sql.DataSource;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.TEST)
public class UnitTestConfiguration extends CommonConfiguration {

	private static final Logger log = LoggerFactory
			.getLogger(UnitTestConfiguration.class);
	
	@Override
	@Bean
	public ConfigurationService configurationService() {
		return new DefaultConfigurationService(
				MyConfigProfiles.TEST,
				"classpath:log4j_dev.properties");
	}

	@Override
	@Bean
	public DataSource dataSource() {
		String dbURL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
		log.info("Using database at {}", dbURL);
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl(dbURL);
		ds.setUsername("sa");
		ds.setPassword("");
		ds.setDefaultAutoCommit(false);
		ds.setInitialSize(1);
		ds.setMaxActive(2);
		return ds;
	}

}
