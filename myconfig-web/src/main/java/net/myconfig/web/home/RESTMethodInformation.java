package net.myconfig.web.home;

public class RESTMethodInformation {

	private final String key;
	private final String path;

	public RESTMethodInformation(String methodKey, String path) {
		this.key = methodKey;
		this.path = path;
	}

	public String getKey() {
		return key;
	}

	public String getPath() {
		return path;
	}

}
