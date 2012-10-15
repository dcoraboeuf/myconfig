package net.myconfig.client.java;

import java.io.IOException;
import java.io.OutputStream;

import net.myconfig.core.MyConfigInterface;

public interface MyConfigClient extends MyConfigInterface {
	
	String key(String application, String version, String environment, String key);
	
	void env(OutputStream out, String application, String version, String environment, String format) throws IOException;
	
	void env(OutputStream out, String application, String version, String environment, String format, String variant) throws IOException;

}
