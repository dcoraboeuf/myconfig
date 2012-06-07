package net.myconfig.service.db;

import java.util.Collections;

import javax.sql.DataSource;

import net.sf.dbinit.DBInit;
import net.sf.dbinit.DBInitAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired(required = false)
	@Qualifier("dbPostAction")
	private DBInitAction postAction;
	
	@Bean
	public DBInit dbInit() {
		DBInit db = new DBInit();
		db.setVersion(0);
		db.setJdbcDataSource(dataSource);
		db.setVersionTable("DBVERSION");
		db.setVersionColumnName("VALUE");
		db.setVersionColumnTimestamp("UPDATED");
		db.setResourceInitialization("/META-INF/db/init.sql");
		db.setResourceUpdate("/META-INF/db/update.{0}.sql");
		if (postAction != null) {
			db.setPostActions(Collections.singletonList(postAction));
		}
		return db;
	}

}
