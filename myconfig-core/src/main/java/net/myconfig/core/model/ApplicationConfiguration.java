package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class ApplicationConfiguration {

	private final String id;
	private final String name;
	private final List<VersionSummary> versionSummaryList;
	private final List<EnvironmentSummary> environmentSummaryList;
	private final List<KeySummary> keySummaryList;

	@JsonCreator
	public ApplicationConfiguration(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("versionSummaryList") List<VersionSummary> versionSummaryList,
			@JsonProperty("environmentSummaryList") List<EnvironmentSummary> environmentSummaryList,
			@JsonProperty("keySummaryList") List<KeySummary> keySummaryList) {
		this.id = id;
		this.name = name;
		this.versionSummaryList = ImmutableList.copyOf(versionSummaryList);
		this.environmentSummaryList = ImmutableList.copyOf(environmentSummaryList);
		this.keySummaryList = ImmutableList.copyOf(keySummaryList);
	}

}
