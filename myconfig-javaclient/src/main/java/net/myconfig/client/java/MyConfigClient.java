package net.myconfig.client.java;

import java.io.IOException;
import java.io.OutputStream;

import net.myconfig.core.MyConfigInterface;
import net.myconfig.core.model.ConfigurationDescription;

public interface MyConfigClient extends Client, MyConfigInterface {
	
	String key(String application, String version, String environment, String key);
	
	ConfigurationDescription configuration (String application, String version);
	
	void env(OutputStream out, String application, String version, String environment, String format) throws IOException;
	
	void env(OutputStream out, String application, String version, String environment, String format, String variant) throws IOException;

}
