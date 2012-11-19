package net.myconfig.service.api;

import lombok.Data;

@Data
public class ApplicationSettings {

	private final String name;
	private final String replyToAddress;
	private final String replyToName;

}
