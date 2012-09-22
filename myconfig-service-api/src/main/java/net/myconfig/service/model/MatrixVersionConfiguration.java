package net.myconfig.service.model;

import java.util.Collection;
import java.util.Set;

import lombok.Data;

import com.google.common.collect.ImmutableSet;

@Data
public class MatrixVersionConfiguration {

	private final String name;
	private final Set<String> keys;

	public MatrixVersionConfiguration(String name, Collection<String> keys) {
		this.name = name;
		this.keys = ImmutableSet.copyOf(keys);
	}

}
