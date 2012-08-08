package net.myconfig.service.api.security;

import java.util.List;

public interface SecuritySelector extends SecurityOperations {

	String getSecurityMode();

	List<String> getSecurityModes();

}
