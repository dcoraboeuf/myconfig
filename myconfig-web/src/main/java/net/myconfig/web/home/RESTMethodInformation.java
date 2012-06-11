package net.myconfig.web.home;


public class RESTMethodInformation {

	private final String name;
	private final String key;
	private final String path;
	private final String method;

	public RESTMethodInformation(String name, String methodKey, String path, String method) {
		this.name = name;
		this.key = methodKey;
		this.path = path;
		this.method = method;
	}
	
	public String getMethod() {
		return method;
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
