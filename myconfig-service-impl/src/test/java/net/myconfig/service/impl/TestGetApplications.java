package net.myconfig.service.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.service.api.MyConfigService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit tests for {@link MyConfigService#getApplications()}.
 * 
 */
public class TestGetApplications extends AbstractSecurityTest {

	@Autowired
	private MyConfigService service;
	
	@Test
	public void admin() throws SQLException {
		asAdmin();
		List<ApplicationSummary> list = service.getApplications().getSummaries();
		assertNotNull(list);
		assertEquals (asList(
				new ApplicationSummary("A", "app1", 0, 0, 0, 0, 0, true, true, true, true, true),
				new ApplicationSummary("BB", "app2", 0, 0, 0, 0, 0, true, true, true, true, true)
				), list);
	}
	
	@Test
	public void app_list_no_grant() throws SQLException {
		asUser().grant(UserFunction.app_list);
		List<ApplicationSummary> list = service.getApplications().getSummaries();
		assertNotNull(list);
		assertEquals (asList(
				new ApplicationSummary("A", "app1", 0, 0, 0, 0, 0, false, false, false, false, false),
				new ApplicationSummary("BB", "app2", 0, 0, 0, 0, 0, false, false, false, false, false)
				), list);
	}
	
	@Test
	public void app_list_some_grant() throws SQLException {
		asUser().grant(UserFunction.app_list).grant("BB", AppFunction.app_view);
		List<ApplicationSummary> list = service.getApplications().getSummaries();
		assertNotNull(list);
		assertEquals (asList(
				new ApplicationSummary("A", "app1", 0, 0, 0, 0, 0, false, false, false, false, false),
				new ApplicationSummary("BB", "app2", 0, 0, 0, 0, 0, false, false, true, false, false)
				), list);
	}
	
	@Test
	public void app_view_no_other_grant() throws SQLException {
		asUser().grant("A", AppFunction.app_view);
		List<ApplicationSummary> list = service.getApplications().getSummaries();
		assertNotNull(list);
		assertEquals (asList(
				new ApplicationSummary("A", "app1", 0, 0, 0, 0, 0, false, false, true, false, false)
				), list);
	}
	
	@Test
	public void app_view_and_config() throws SQLException {
		asUser().grant("A", AppFunction.app_view).grant("A", AppFunction.app_config);
		List<ApplicationSummary> list = service.getApplications().getSummaries();
		assertNotNull(list);
		assertEquals (asList(
				new ApplicationSummary("A", "app1", 0, 0, 0, 0, 0, false, true, true, false, false)
				), list);
	}
	
	@Test
	public void app_view_and_config_and_delete() throws SQLException {
		asUser().grant("A", AppFunction.app_view).grant("A", AppFunction.app_config).grant("A", AppFunction.app_delete);
		List<ApplicationSummary> list = service.getApplications().getSummaries();
		assertNotNull(list);
		assertEquals (asList(
				new ApplicationSummary("A", "app1", 0, 0, 0, 0, 0, true, true, true, false, false)
				), list);
	}

}
