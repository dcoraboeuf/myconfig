package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class ApplicationUsers {

	private final String id;
	private final String name;
	private final List<ApplicationUserRights> users;

	@JsonCreator
	public ApplicationUsers(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("users") List<ApplicationUserRights> users) {
		this.id = id;
		this.name = name;
		this.users = ImmutableList.copyOf(users);
	}

}
