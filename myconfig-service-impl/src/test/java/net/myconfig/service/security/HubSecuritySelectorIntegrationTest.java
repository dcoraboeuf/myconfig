package net.myconfig.service.security;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.test.AbstractIntegrationTest;
import net.myconfig.test.DBUnitHelper;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HubSecuritySelectorIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private SecuritySelector selector;

	@Before
	public void before() throws SQLException {
		IDatabaseConnection databaseConnection = DBUnitHelper.getConnection();
		Connection c = databaseConnection.getConnection();
		try {
			PreparedStatement ps = c.prepareStatement("delete from configuration where name = 'security.mode'");
			try {
				ps.executeUpdate();
			} finally {
				ps.close();
			}
		} finally {
			c.commit();
		}
	}

	@Test
	public void getSecurityManagementId() {
		assertEquals("none", selector.getSecurityManagementId());
	}

	@Test
	public void getSecurityModes() {
		assertEquals(asList("builtin", "none"), selector.getSecurityModes());
	}

	@Test(expected = SecurityManagementNotFoundException.class)
	public void switchSecurityMode_0_unknown() {
		selector.switchSecurityMode("xcx");
	}

	@Test
	public void switchSecurityMode_1_builtin() throws DataSetException, SQLException {
		assertRecordNotExists("select * from configuration where name = 'security.mode'");
		selector.switchSecurityMode("builtin");
		assertRecordExists("select * from configuration where name = 'security.mode' and value = 'builtin'");
	}

}
