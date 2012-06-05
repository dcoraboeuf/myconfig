package net.myconfig.service.db;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	
	// FIXME Configuration using myconfig !
	
	@Bean
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl(String.format("jdbc:h2:file:%s/myconfig-dev/db/data;AUTOCOMMIT=OFF;MVCC=true", System.getProperty("user.home")));
		ds.setUsername("sa");
		ds.setPassword("");
		ds.setDefaultAutoCommit(false);
		ds.setInitialSize(5);
		ds.setMaxActive(10);
		return ds;
	}

}
