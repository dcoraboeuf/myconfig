package net.myconfig.service.api.type;

import net.myconfig.core.CoreException;

public class ValueTypeNotFoundException extends CoreException {

	public ValueTypeNotFoundException(String id) {
		super(id);
	}

}
