package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class UserSummaries {

	private final List<UserSummary> summaries;

	@JsonCreator
	public UserSummaries(@JsonProperty("summaries") List<UserSummary> summaries) {
		this.summaries = ImmutableList.copyOf(summaries);
	}

}
