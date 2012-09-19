package net.myconfig.service.support;

import net.myconfig.service.api.security.User;

public class UserBuilder {

	public static User administrator() {
		return create("admin").admin().build();
	}

	public static User user() {
		return user("user");
	}

	public static User user(String name) {
		return create(name).build();
	}

	public static UserBuilder create(String name) {
		return new UserBuilder(name);
	}
	
	private final String name;
	private String displayName = "John Doe";
	private String email = "john.doe@mail.com";
	private boolean admin = false;
	private boolean verified = true;
	private boolean disabled = true;
	
	private UserBuilder(String name) {
		this.name = name;
	}

	public UserBuilder admin() {
		this.admin = true;
		return this;
	}

	public User build() {
		return new User(name, displayName, email, admin, verified, disabled);
	}

}
