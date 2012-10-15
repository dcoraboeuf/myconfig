package net.myconfig.core.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class VersionSummary extends Version {

	private final int keyCount;
	private final int configCount;
	private final int valueCount;

	@JsonCreator
	public VersionSummary(
			@JsonProperty("name") String name,
			@JsonProperty("keyCount") int keyCount,
			@JsonProperty("configCount") int configCount,
			@JsonProperty("valueCount") int valueCount) {
		super(name);
		this.keyCount = keyCount;
		this.configCount = configCount;
		this.valueCount = valueCount;
	}

}
