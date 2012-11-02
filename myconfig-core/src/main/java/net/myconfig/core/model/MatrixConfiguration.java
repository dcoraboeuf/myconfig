package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class MatrixConfiguration {

	private final String id;
	private final String name;
	private final List<MatrixVersionConfiguration> versionConfigurationList;
	private final List<Key> keyList;

	@JsonCreator
	public MatrixConfiguration(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("versionConfigurationList") List<MatrixVersionConfiguration> versionConfigurationList,
			@JsonProperty("keyList") List<Key> keyList) {
		this.id = id;
		this.name = name;
		this.versionConfigurationList = ImmutableList.copyOf(versionConfigurationList);
		this.keyList = ImmutableList.copyOf(keyList);
	}

	public boolean isEnabled(String version, String key) {
		for (MatrixVersionConfiguration matrixVersionConfiguration : versionConfigurationList) {
			if (StringUtils.equals(version, matrixVersionConfiguration.getName())) {
				if (matrixVersionConfiguration.getKeys().contains(key)) {
					return true;
				}
			}
		}
		return false;
	}

}
