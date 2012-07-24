package net.myconfig.service.config;

import java.io.File;

import javax.sql.DataSource;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.DEV)
public class DevConfiguration extends CommonConfiguration {

	private static final Logger log = LoggerFactory
			.getLogger(DevConfiguration.class);
	
	@Override
	@Bean
	public ConfigurationService configurationService() {
		return new DefaultConfigurationService(
				MyConfigProfiles.DEV,
				"classpath:log4j_dev.properties");
	}

	@Override
	@Bean
	public DataSource dataSource() throws Exception {
		// Gets the home directory
		File home = HomeSupport.home();
		// Checks for the home
		if (!home.exists()) {
			FileUtils.forceMkdir(home);
		} else if (!home.isDirectory() && !home.canWrite()) {
			throw new IllegalStateException(String.format("The home directory at %s does not exist, is not a directory or cannot be written.", home.getAbsolutePath()));
		}
		// DB URL
		String dbURL = String.format("jdbc:h2:file:%s/db/data;AUTOCOMMIT=OFF;MVCC=true", home.getAbsolutePath());
		log.info("[config] Using database at {}", dbURL);
		// Datasource
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
