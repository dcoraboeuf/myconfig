package net.myconfig.service.config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.apache.commons.io.IOUtils;
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
	
	// FIXME Gets the configuration from outside
	
	@Override
	@Bean
	public ConfigurationService configurationService() throws Exception {
		// Gets the home directory
		File home = HomeSupport.home();
		// Checks for the home
		if (!home.exists() && !home.isDirectory() && !home.canWrite()) {
			throw new IllegalStateException(String.format("The home directory at %s does not exist, is not a directory or cannot be written.", home.getAbsolutePath()));
		}
		// Path to the log configuration
		String logPath = initLoggingConfiguration (home);
		// OK
		return new DefaultConfigurationService (
				MyConfigProfiles.PROD,
				logPath,
				"org.h2.Driver",
				String.format("jdbc:h2:file:%s/myconfig-prod/db/data;AUTOCOMMIT=OFF;MVCC=true", System.getProperty("user.home")),
				"sa",
				"",
				1,
				2);
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
