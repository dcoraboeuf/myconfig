package net.myconfig.test;

import javax.sql.DataSource;

import net.myconfig.core.MyConfigProfiles;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ MyConfigProfiles.TEST, MyConfigProfiles.IT })
public class TestDataSourceConfig {

	@Bean
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl("jdbc:h2:file:target/work/db/test;AUTOCOMMIT=OFF;MVCC=true");
		ds.setUsername("sa");
		ds.setPassword("");
		ds.setDefaultAutoCommit(false);
		ds.setInitialSize(5);
		ds.setMaxActive(10);
		return ds;
	}

}
