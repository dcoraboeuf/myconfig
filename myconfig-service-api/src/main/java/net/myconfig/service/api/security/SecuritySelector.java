package net.myconfig.service.api.security;

import java.util.List;

public interface SecuritySelector extends SecurityOperations {
	
	/**
	 * @deprecated 
	 */
	// FIXME Security mode - duplicate code
	@Deprecated
	String getSecurityManagementId();

	/**
	 * @deprecated 
	 */
	// FIXME Security mode - duplicate code
	@Deprecated
	List<String> getSecurityModes();

}
