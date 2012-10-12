package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class ApplicationSummaries {

	private final List<ApplicationSummary> summaries;

	@JsonCreator
	public ApplicationSummaries(@JsonProperty("summaries") List<ApplicationSummary> summaries) {
		this.summaries = ImmutableList.copyOf(summaries);
	}

}
