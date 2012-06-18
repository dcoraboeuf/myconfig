package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ApplicationConfiguration {

	private final int id;
	private final String name;
	private final List<VersionSummary> versionSummaryList;
	private final List<EnvironmentSummary> environmentSummaryList;
	private final List<KeySummary> keySummaryList;

	public ApplicationConfiguration(int id, String name, List<VersionSummary> versionSummaryList, List<EnvironmentSummary> environmentSummaryList, List<KeySummary> keySummaryList) {
		this.id = id;
		this.name = name;
		this.versionSummaryList = ImmutableList.copyOf(versionSummaryList);
		this.environmentSummaryList = ImmutableList.copyOf(environmentSummaryList);
		this.keySummaryList = ImmutableList.copyOf(keySummaryList);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public List<VersionSummary> getVersionSummaryList() {
		return versionSummaryList;
	}
	
	public List<EnvironmentSummary> getEnvironmentSummaryList() {
		return environmentSummaryList;
	}
	
	public List<KeySummary> getKeySummaryList() {
		return keySummaryList;
	}

}
