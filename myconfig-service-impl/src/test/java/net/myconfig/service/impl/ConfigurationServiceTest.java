package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.test.AbstractIntegrationTest;

import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigurationServiceTest extends AbstractIntegrationTest {

	@Autowired
	private ConfigurationService service;

	@Test
	public void getParameter_ok() {
		String value = service.getParameter(ConfigurationKey.APP_NAME);
		assertEquals("myapp", value);
	}

	@Test
	public void getParameter_not_found() {
		String value = service.getParameter(ConfigurationKey.APP_REPLYTO_NAME);
		assertEquals(ConfigurationKey.APP_REPLYTO_NAME.getDefault(), value);
	}
	
	@Test
	public void setParameter_exist() throws DataSetException, SQLException {
		assertRecordExists("select * from configuration where name = 'app.name' and value = 'myapp'");
		service.setParameter(ConfigurationKey.APP_NAME, "myapp2");
		assertRecordExists("select * from configuration where name = 'app.name' and value = 'myapp2'");
	}
	
	@Test
	public void setParameter_not_exist() throws DataSetException, SQLException {
		assertRecordNotExists("select * from configuration where name = 'app.replyto.name'");
		service.setParameter(ConfigurationKey.APP_REPLYTO_NAME, "value2");
		assertRecordExists("select * from configuration where name = 'app.replyto.name' and value = 'value2'");
	}
	
	@Test
	public void caching_read() throws SQLException {
		// Sets the initial value
		service.setParameter(ConfigurationKey.APP_REPLYTO_ADDRESS, "address1");
		// Gets the value
		assertEquals("address1", service.getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS));
		// Updates the value directly in the database
		execute("update configuration set value = ? where name = ?", "address2", ConfigurationKey.APP_REPLYTO_ADDRESS.getKey());
		// The initial value in still in cache
		assertEquals("address1", service.getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS));
	}
	
	@Test
	public void caching_read_and_update() throws SQLException {
		// Sets the initial value
		service.setParameter(ConfigurationKey.APP_REPLYTO_ADDRESS, "address1");
		// Gets the value
		assertEquals("address1", service.getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS));
		// Updates the value
		service.setParameter(ConfigurationKey.APP_REPLYTO_ADDRESS, "address2");
		// The value has been updated
		assertEquals("address2", service.getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS));
	}

}
