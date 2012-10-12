package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
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

}
