package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class EnvironmentUsers {

	private final String id;
	private final String application;
	private final String environment;
	private final List<EnvironmentUserRights> users;

	@JsonCreator
	public EnvironmentUsers(
			@JsonProperty("id") String id,
			@JsonProperty("application") String application,
			@JsonProperty("environment") String environment,
			@JsonProperty("users") List<EnvironmentUserRights> users) {
		this.id = id;
		this.application = application;
		this.environment = environment;
		this.users = ImmutableList.copyOf(users);
	}

}
