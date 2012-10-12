package net.myconfig.client.java;

import net.myconfig.core.MyConfigInterface;

public interface MyConfigClient extends MyConfigInterface {
	
	String key(String application, String version, String environment, String key);

}
