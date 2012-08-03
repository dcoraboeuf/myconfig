package net.myconfig.web.settings;

import java.util.Map;

public abstract class AbstractSettingsCollaborator implements SettingsCollaborator {
	
	@Override
	public String getId() {
		return getClass().getName();
	}
	
	protected String getParameter (Map<String, String[]> parameters, String name, boolean mandatory, String defaultValue) {
		String[] values = parameters.get(name);
		if (values == null || values.length == 0) {
			if (mandatory) {
				throw new SettingParameterMissingException(name);
			} else {
				return defaultValue;
			}
		} else if (values.length > 1) {
			throw new SettingParameterMultipleException(name);
		} else {
			return values[0];
		}
	}

}
