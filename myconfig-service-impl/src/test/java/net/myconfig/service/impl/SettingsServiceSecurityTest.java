package net.myconfig.service.impl;

import java.sql.SQLException;

import net.myconfig.service.api.SettingsService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

public class SettingsServiceSecurityTest extends AbstractSecurityTest {
	
	@Autowired
	private SettingsService settingsService;
	
	@Test(expected = AccessDeniedException.class)
	public void setApplicationSettings_anonymous() {
		anonymous();
		settingsService.setApplicationSettings("AnyName", "AnyReplyAddress", "AnyReplyName");
	}
	
	@Test(expected = AccessDeniedException.class)
	public void setApplicationSettings_user() throws SQLException {
		asUser();
		settingsService.setApplicationSettings("AnyName", "AnyReplyAddress", "AnyReplyName");
	}
	
	@Test
	public void setApplicationSettings_admin() throws SQLException {
		asAdmin();
		settingsService.setApplicationSettings("AnyName", "AnyReplyAddress", "AnyReplyName");
	}
	
	@Test(expected = AccessDeniedException.class)
	public void setAuditRetentionDays_anonymous() {
		anonymous();
		settingsService.setAuditRetentionDays(10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void setAuditRetentionDays_user() throws SQLException {
		asUser();
		settingsService.setAuditRetentionDays(10);
	}
	
	@Test
	public void setAuditRetentionDays_admin() throws SQLException {
		asAdmin();
		settingsService.setAuditRetentionDays(10);
	}

}
