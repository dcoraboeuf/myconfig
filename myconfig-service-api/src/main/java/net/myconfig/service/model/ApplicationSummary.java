package net.myconfig.service.model;

import lombok.Data;

@Data
public class ApplicationSummary {

	private final int id;
	private final String name;

	private final int versionCount;
	private final int keyCount;
	private final int environmentCount;
	private final int configCount;
	private final int valueCount;

	private final boolean delete;
	private final boolean config;
	private final boolean view;

	public ApplicationSummary acl(boolean canDelete, boolean canConfig, boolean canView) {
		return new ApplicationSummary(id, name, versionCount, keyCount, environmentCount, configCount, valueCount, canDelete, canConfig, canView);
	}

}
