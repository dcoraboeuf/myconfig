package net.myconfig.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class CleanInsertTestExecutionListener implements TestExecutionListener {

	private static final Logger LOG = LoggerFactory.getLogger(CleanInsertTestExecutionListener.class);

	@Override
	public void beforeTestClass(TestContext testContext) {
	}

	@Override
	public void prepareTestInstance(TestContext testContext) {
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws IOException, SQLException, DatabaseUnitException {
		// locations of the data sets
		List<String> dataSetResourcePaths = new ArrayList<String>();

		// first, the annotation on the test class
		Class<? extends Object> testClass = testContext.getTestInstance().getClass();
		DataSetLocation dsLocation = testClass.getAnnotation(DataSetLocation.class);
		if (dsLocation != null) {
			dataSetResourcePaths.add(dsLocation.value());
		} else {
			// no annotation, let's try with the name of the test
			String tempDsRes = testClass.getName();
			tempDsRes = StringUtils.replace(tempDsRes, ".", "/");
			tempDsRes = "/" + tempDsRes + "-dataset.xml";
			if (getClass().getResourceAsStream(tempDsRes) != null) {
				LOG.info("detected default dataset: {}", tempDsRes);
				dataSetResourcePaths.add(tempDsRes);
			} else {
				LOG.info("no default dataset");
			}
		}

		// Tries on the method
		dsLocation = testContext.getTestMethod().getAnnotation(DataSetLocation.class);
		if (dsLocation != null) {
			dataSetResourcePaths.add(dsLocation.value());
		}

		IDatabaseConnection dbConn = new DatabaseDataSourceConnection(testContext.getApplicationContext().getBean(DataSource.class));
		if (!dataSetResourcePaths.isEmpty()) {
			for(String dataSetResourcePath : dataSetResourcePaths) {
				InputStream in = testClass.getResourceAsStream(dataSetResourcePath);
				if (in == null) {
					throw new IOException("Cannot find dataset at " + dataSetResourcePath);
				} else {
					try {
						LOG.info("Injecting dataset {} for {}", dataSetResourcePath, testContext.getClass().getName());
						IDataSet dataSet = new FlatXmlDataSetBuilder().build(in);
						DatabaseOperation.CLEAN_INSERT.execute(dbConn, dataSet);
					} finally {
						in.close();
					}
				}
			}
			dbConn.getConnection().commit();
		} else {
			LOG.info("{} does not have any data set, no data injection", testContext.getClass().getName());
		}
		DBUnitHelper.setConnection(dbConn);
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws SQLException {
		IDatabaseConnection c = DBUnitHelper.getConnection();
		if (c != null) {
			c.close();
			DBUnitHelper.setConnection(null);
		}
	}

	@Override
	public void afterTestClass(TestContext testContext) {
	}

}
