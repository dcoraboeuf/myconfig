package net.myconfig.web.settings;

public abstract class AbstractSettingsCollaborator implements SettingsCollaborator {
	
	@Override
	public String getId() {
		return getClass().getName();
	}

}
