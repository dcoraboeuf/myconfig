package net.myconfig.client.java.support;

import net.myconfig.client.java.MyConfigClient;

public class MyConfigClientFactory {

	public static MyConfigClientFactory create(String url) {
		return new MyConfigClientFactory(url);
	}

	private final String url;

	private MyConfigClientFactory(String url) {
		this.url = url;
	}
	
	public MyConfigClient build() {
		return new MyConfigDefaultClient (url);
	}

}
