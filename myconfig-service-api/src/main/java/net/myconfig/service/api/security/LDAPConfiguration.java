package net.myconfig.service.api.security;

import lombok.Data;

@Data
public class LDAPConfiguration {
	
	private final boolean enabled;
	private final String server;
	private final String rootDN;
	private final String userSearchBase;
	private final String userSearchFilter;
	private final String managerDN;
	private final String managerPassword;

}
 