package net.myconfig.service.api;

public interface MyConfigService {

	String getKey(String application, String version, String environment, String key);

}
