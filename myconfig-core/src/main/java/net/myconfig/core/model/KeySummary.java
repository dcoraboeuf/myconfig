package net.myconfig.core.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class KeySummary extends Key {

	private final int versionCount;
	private final int configCount;
	private final int valueCount;

	@JsonCreator
	public KeySummary(
			@JsonProperty("name") String name,
			@JsonProperty("description") String description,
			@JsonProperty("versionCount") int versionCount,
			@JsonProperty("configCount") int configCount,
			@JsonProperty("valueCount") int valueCount) {
		super(name, description);
		this.versionCount = versionCount;
		this.configCount = configCount;
		this.valueCount = valueCount;
	}

}
