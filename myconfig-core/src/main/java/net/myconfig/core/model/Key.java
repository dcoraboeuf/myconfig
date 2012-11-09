package net.myconfig.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Key {

	private final String name;
	private final String description;
	private final String typeId;
	private final String typeParam;

}
