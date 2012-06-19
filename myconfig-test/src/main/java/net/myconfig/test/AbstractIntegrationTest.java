package net.myconfig.test;


import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(CleanInsertTestExecutionListener.class)
@Transactional
@ContextConfiguration({ "classpath*:META-INF/spring/*.xml" })
@ActiveProfiles(profiles = { "test" })
public abstract class AbstractIntegrationTest extends AbstractJUnit4SpringContextTests {

	protected ITable getTable(String name, String sql, Object... parameters) throws DataSetException, SQLException {
		IDatabaseConnection databaseConnection = DBUnitHelper.getConnection();
		return databaseConnection.createQueryTable(name, String.format(sql, parameters));
	}

	protected <T> List<T> getInitialSituation(int expectedCount,
			String column, String id, String sql, Object... sqlParameters) throws DataSetException,
			SQLException {
		ITable table = getTable(id, sql, sqlParameters);
		int tableRowCount = table.getRowCount();
		assertEquals(expectedCount, tableRowCount);
		List<T> results = new ArrayList<T>();
		for (int i = 0 ; i < tableRowCount ; i++) {
			@SuppressWarnings("unchecked")
			T value = (T) table.getValue(i, column);
			results.add(value);
		}
		return results;
	}
	
	protected void assertRecordExists (String sql, Object... parameters) throws DataSetException, SQLException {
		ITable table = getTable("test", sql, parameters);
		assertEquals(1, table.getRowCount());
	}
	
	protected void assertRecordNotExists (String sql, Object... parameters) throws DataSetException, SQLException {
		ITable table = getTable("test", sql, parameters);
		assertEquals(0, table.getRowCount());
	}
	
}
