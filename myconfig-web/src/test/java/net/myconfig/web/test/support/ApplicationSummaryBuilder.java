package net.myconfig.web.test.support;

import net.myconfig.core.model.ApplicationSummary;

public class ApplicationSummaryBuilder {

	public static ApplicationSummaryBuilder create() {
		return new ApplicationSummaryBuilder();
	}

	public static ApplicationSummaryBuilder create(String id, String name) {
		return create().setId(id).setName(name);
	}

	private String id;
	private String name;
	private int versionCount;
	private int keyCount;
	private int environmentCount;
	private int configCount;
	private int valueCount;
	private boolean delete = true;
	private boolean config = true;
	private boolean view = true;
	private boolean matrix = true;
	private boolean users = true;

	protected ApplicationSummaryBuilder() {
	}

	public ApplicationSummaryBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public ApplicationSummaryBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ApplicationSummaryBuilder setVersionCount(int versionCount) {
		this.versionCount = versionCount;
		return this;
	}

	public ApplicationSummaryBuilder setKeyCount(int keyCount) {
		this.keyCount = keyCount;
		return this;
	}

	public ApplicationSummaryBuilder setEnvironmentCount(int environmentCount) {
		this.environmentCount = environmentCount;
		return this;
	}

	public ApplicationSummaryBuilder setConfigCount(int configCount) {
		this.configCount = configCount;
		return this;
	}

	public ApplicationSummaryBuilder setValueCount(int valueCount) {
		this.valueCount = valueCount;
		return this;
	}

	public ApplicationSummary build() {
		return new ApplicationSummary(id, name, versionCount, keyCount, environmentCount, configCount, valueCount, delete, config, view, matrix, users);
	}

}
