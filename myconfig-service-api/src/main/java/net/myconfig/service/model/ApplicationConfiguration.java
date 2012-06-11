package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ApplicationConfiguration {

	private final int id;
	private final String name;
	private final List<VersionSummary> versionSummaryList;

	public ApplicationConfiguration(int id, String name, List<VersionSummary> versionSummaryList) {
		this.id = id;
		this.name = name;
		this.versionSummaryList = ImmutableList.copyOf(versionSummaryList);
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

}
