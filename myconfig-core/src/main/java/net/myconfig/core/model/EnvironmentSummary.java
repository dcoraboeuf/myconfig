package net.myconfig.core.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EnvironmentSummary extends Environment {

	private final int configCount;
	private final int valueCount;

	@JsonCreator
	public EnvironmentSummary(
			@JsonProperty("name") String name,
			@JsonProperty("configCount") int configCount,
			@JsonProperty("valueCount") int valueCount) {
		super(name);
		this.configCount = configCount;
		this.valueCount = valueCount;
	}

}
