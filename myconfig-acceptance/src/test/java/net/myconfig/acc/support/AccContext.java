package net.myconfig.acc.support;

import net.myconfig.client.java.MyConfigClient;
import lombok.Data;

@Data
public class AccContext {
	
	private final String url;
	private final String version;
	private final MyConfigClient client;

}
