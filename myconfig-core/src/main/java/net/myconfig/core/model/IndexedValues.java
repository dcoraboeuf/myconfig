package net.myconfig.core.model;

import java.util.Map;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

@Data
public class IndexedValues<T> {

	public static <V> Function<IndexedValues<V>, String> indexFn() {
		return new Function<IndexedValues<V>, String>() {
			@Override
			public String apply(IndexedValues<V> index) {
				return index.getName();
			}
		};
	}

	private final String name;
	private final Map<String, T> values;

	@JsonCreator
	public IndexedValues(
			@JsonProperty("name") String name,
			@JsonProperty("values") Map<String, T> values) {
		this.name = name;
		this.values = ImmutableMap.copyOf(values);
	}

}
