package net.myconfig.service.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.PROD)
public class ProdConfiguration extends CommonConfiguration {

	private static final String JNDI_NAME = "java:comp/env/jdbc/myconfig";
	
	private static final Logger log = LoggerFactory.getLogger(ProdConfiguration.class);

	@Override
	@Bean
	public ConfigurationService configurationService() throws Exception {
		// No logging file
		return new DefaultConfigurationService(MyConfigProfiles.PROD, null);
	}

	@Override
	@Bean
	public DataSource dataSource() throws Exception {
			log.info("[db] Using database at JNDI {}", JNDI_NAME);
			// Creates the JNDI naming context
			Context ctx = new InitialContext();
			// Look-up
			return (DataSource) ctx.lookup(JNDI_NAME);
	}

}
