package net.myconfig.service.api;


public interface UIService {

	public enum Link {
		NEW_USER, RESET_USER, USER_CHANGE_PASSWORD, FORGOTTEN_PASSWORD;
	}

	String getLink(Link link, String... components);

}
