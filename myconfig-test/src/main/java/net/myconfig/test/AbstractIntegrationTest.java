package net.myconfig.test;


import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.myconfig.core.MyConfigProfiles;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ActiveProfiles(profiles = { MyConfigProfiles.TEST })
public abstract class AbstractIntegrationTest extends AbstractJUnit4SpringContextTests {

	private final Logger logger = LoggerFactory.getLogger(AbstractIntegrationTest.class);

	private static final String TABLE_TEST = "test";

	protected ITable getTable(String name, String sql, Object... parameters) throws DataSetException, SQLException {
		IDatabaseConnection databaseConnection = DBUnitHelper.getConnection();
		return databaseConnection.createQueryTable(name, String.format(sql, parameters));
	}

	protected void execute(String sql, Object... params) throws SQLException {
		logger.debug("SQL: {} {}", sql, StringUtils.join(params));
		Connection c = DBUnitHelper.getConnection().getConnection();
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			try {
				int index = 1;
				for (Object param : params) {
					ps.setObject(index++, param);
				}
				ps.executeUpdate();
			} finally {
				ps.close();
			}
		} finally {
			c.commit();
		}
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
	
	protected void assertRecordValue (String value, String column, String sql, Object... parameters) throws DataSetException, SQLException {
		ITable table = getTable(TABLE_TEST, sql, parameters);
		assertEquals(1, table.getRowCount());
		assertEquals(value, table.getValue(0, column));
	}
	
	protected void assertRecordExists (String sql, Object... parameters) throws DataSetException, SQLException {
		ITable table = getTable(TABLE_TEST, sql, parameters);
		assertEquals(1, table.getRowCount());
	}
	
	protected void assertRecordNotExists (String sql, Object... parameters) throws DataSetException, SQLException {
		ITable table = getTable(TABLE_TEST, sql, parameters);
		assertEquals(0, table.getRowCount());
	}
	
	protected void assertRecordCount (int count, String sql, Object... parameters) throws DataSetException, SQLException {
		ITable table = getTable(TABLE_TEST, sql, parameters);
		assertEquals(count, table.getRowCount());
	}
	
}
