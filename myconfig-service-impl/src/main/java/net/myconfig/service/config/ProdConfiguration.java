package net.myconfig.service.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.PROD)
public class ProdConfiguration implements GeneralConfiguration {

	private static final Logger log = LoggerFactory
			.getLogger(ProdConfiguration.class);
	
	private File home;
	
	@Override
	@Bean
	public ConfigurationService configurationService() throws Exception {
		// Gets the home directory
		home = HomeSupport.home();
		// Checks for the home
		if (!home.exists() && !home.isDirectory() && !home.canWrite()) {
			throw new IllegalStateException(String.format("The home directory at %s does not exist, is not a directory or cannot be written.", home.getAbsolutePath()));
		}
		// System configuration
		System.setProperty(HomeSupport.SYSTEM_HOME, home.getAbsolutePath());
		// Path to the log configuration
		String logPath = initLoggingConfiguration (home);
		// Gets the configuration
		Properties properties = initConfiguration (home);
		// OK
		return new DefaultConfigurationService (
				MyConfigProfiles.PROD,
				logPath,
				getProperty (properties, "db.driver"),
				getProperty (properties, "db.url"),
				getProperty (properties, "db.user"),
				getProperty (properties, "db.password"),
				getIntProperty (properties, "db.pool.initial"),
				getIntProperty (properties, "db.pool.max")
				);
	}

	private int getIntProperty(Properties properties, String name) {
		String value = getProperty(properties, name);
		try {
			return Integer.parseInt(value, 10);
		} catch (NumberFormatException ex) {
			throw new IllegalStateException(String.format("Cannot get integer value from configuration property: %s = %s", name, value));
		}
	}

	private String getProperty(Properties properties, String name) {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new IllegalStateException(String.format("Missing configuration property: %s", name));
		} else {
			return expand (value);
		}
	}

	private String expand(String value) {
		return StringUtils.replace(value, "%home%", home.getAbsolutePath());
	}

	private Properties initConfiguration(File home) throws IOException {
		// Configuration file
		File configFile = new File (home, "config.properties");
		String configFilePath = configFile.getAbsolutePath();
		log.info("Initializing configuration for file {}", configFilePath);
		// File already there, does not touch
		if (configFile.exists()) {
			log.info("Configuration already initialized at {}", configFilePath);
		} else {
			log.info("Creating the configuration at {}", configFilePath);
			InputStream in = getClass().getResourceAsStream("/META-INF/config/config-default.properties");
			if (in == null) {
				throw new IllegalStateException("Cannot find the default production configuration");
			} else {
				try {
					OutputStream out = new BufferedOutputStream(new FileOutputStream(configFile));
					try {
						IOUtils.copy(in, out);
					} finally {
						out.close();
					}
				} finally {
					in.close();
				}
			}
		}
		// Loads the properties
		InputStream in = new BufferedInputStream(new FileInputStream(configFile));
		try {
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		} finally {
			in.close();
		}
	}

	private String initLoggingConfiguration(File home) throws IOException {
		// Logging configuration file
		File logConfigFile = new File (home, "log4j.properties");
		String logConfigFilePath = logConfigFile.getAbsolutePath();
		log.info("Initializing logging for file {}", logConfigFilePath);
		// File already there, does not touch
		if (logConfigFile.exists()) {
			log.info("Logging already initialized at {}", logConfigFilePath);
		} else {
			log.info("Creating the logging configuration at {}", logConfigFilePath);
			InputStream in = getClass().getResourceAsStream("/log4j_prod.properties");
			if (in == null) {
				throw new IllegalStateException("Cannot find the default production logging configuration");
			} else {
				try {
					OutputStream out = new BufferedOutputStream(new FileOutputStream(logConfigFile));
					try {
						IOUtils.copy(in, out);
					} finally {
						out.close();
					}
				} finally {
					in.close();
				}
			}
		}
		// OK
		return logConfigFilePath;
	}

}