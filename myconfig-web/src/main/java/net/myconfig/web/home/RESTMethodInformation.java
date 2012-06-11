package net.myconfig.web.home;

public class RESTMethodInformation {

	private final String name;
	private final String key;
	private final String path;

	public RESTMethodInformation(String name, String methodKey, String path) {
		this.name = name;
		this.key = methodKey;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public String getPath() {
		return path;
	}

}
