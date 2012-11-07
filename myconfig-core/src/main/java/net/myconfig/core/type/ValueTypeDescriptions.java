package net.myconfig.core.type;

import java.util.Collection;
import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class ValueTypeDescriptions {

	private final List<ValueTypeDescription> descriptions;

	@JsonCreator
	public ValueTypeDescriptions(@JsonProperty("descriptions") Collection<ValueTypeDescription> descriptions) {
		this.descriptions = ImmutableList.copyOf(descriptions);
	}

}
