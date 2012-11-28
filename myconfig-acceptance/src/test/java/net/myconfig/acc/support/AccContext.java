package net.myconfig.acc.support;

import lombok.Data;
import net.myconfig.client.java.MyConfigClient;
import net.sf.jstring.Strings;

@Data
public class AccContext {
	
	private final String url;
	private final String version;
	private final MyConfigClient client;
	private final Strings strings;
	
	public void close() {
	}

}
