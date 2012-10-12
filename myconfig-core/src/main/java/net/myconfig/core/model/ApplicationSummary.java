package net.myconfig.core.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;

@Data
public class ApplicationSummary {

	private final int id;
	private final String name;

	private final int versionCount;
	private final int keyCount;
	private final int environmentCount;
	private final int configCount;
	private final int valueCount;

	private final boolean delete;
	private final boolean config;
	private final boolean view;
	private final boolean matrix;

	@JsonCreator
	public ApplicationSummary(
			@JsonProperty("id") int id,
			@JsonProperty("name") String name,
			@JsonProperty("versionCount") int versionCount,
			@JsonProperty("keyCount") int keyCount,
			@JsonProperty("environmentCount") int environmentCount, 
			@JsonProperty("configCount") int configCount, 
			@JsonProperty("valueCount") int valueCount, 
			@JsonProperty("delete") boolean delete, 
			@JsonProperty("config") boolean config, 
			@JsonProperty("view") boolean view, 
			@JsonProperty("matrix") boolean matrix) {
		this.id = id;
		this.name = name;
		this.versionCount = versionCount;
		this.keyCount = keyCount;
		this.environmentCount = environmentCount;
		this.configCount = configCount;
		this.valueCount = valueCount;
		this.delete = delete;
		this.config = config;
		this.view = view;
		this.matrix = matrix;
	}

	public ApplicationSummary acl(boolean canDelete, boolean canConfig, boolean canView, boolean canMatrix) {
		return new ApplicationSummary(id, name, versionCount, keyCount, environmentCount, configCount, valueCount, canDelete, canConfig, canView, canMatrix);
	}

}
