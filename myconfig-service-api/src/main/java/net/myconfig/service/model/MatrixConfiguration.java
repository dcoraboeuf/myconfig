package net.myconfig.service.model;

import java.util.List;

import lombok.Data;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableList;

@Data
public class MatrixConfiguration {

	private final int id;
	private final String name;
	private final List<MatrixVersionConfiguration> versionConfigurationList;
	private final List<Key> keyList;

	public MatrixConfiguration(int id, String name, List<MatrixVersionConfiguration> versionConfigurationList, List<Key> keyList) {
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
