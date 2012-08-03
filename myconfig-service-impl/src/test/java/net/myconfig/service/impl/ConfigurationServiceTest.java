package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.myconfig.service.api.ConfigurationService;
import net.myconfig.test.AbstractIntegrationTest;

public class ConfigurationServiceTest extends AbstractIntegrationTest {

	@Autowired
	private ConfigurationService service;

	@Test
	public void getParameter_ok() {
		String value = service.getParameter("key1", null);
		assertEquals("value1", value);
	}

	@Test
	public void getParameter_not_found() {
		String value = service.getParameter("keyx", "valuex");
		assertEquals("valuex", value);
	}
	
	@Test
	public void setParameter_exist() throws DataSetException, SQLException {
		assertRecordExists("select * from configuration where name = 'key1' and value = 'value1'");
		service.setParameter("key1", "value2");
		assertRecordExists("select * from configuration where name = 'key1' and value = 'value2'");
	}
	
	@Test
	public void setParameter_not_exist() throws DataSetException, SQLException {
		assertRecordNotExists("select * from configuration where name = 'key2' and value = 'value2'");
		service.setParameter("key2", "value2");
		assertRecordExists("select * from configuration where name = 'key2' and value = 'value2'");
	}

}
