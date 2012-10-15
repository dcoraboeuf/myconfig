package net.myconfig.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public final class MapBuilder<K, V> {

	public static <K, V> MapBuilder<K, V> create() {
		return new MapBuilder<K, V>();
	}

	private final Map<K, V> map;

	private MapBuilder() {
		map = new LinkedHashMap<K, V>();
	}

	public Map<K, V> build() {
		return ImmutableMap.copyOf(map);
	}

	public MapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}

}
