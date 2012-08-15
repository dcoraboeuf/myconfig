package net.myconfig.service.api;


public interface UIService {

	public enum Link {
		NEW_USER
	}

	String getLink(Link link, String... components);

}
