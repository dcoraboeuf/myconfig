package com.netbeetle.jackson;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public final class ObjectMapperFactory {

	public static ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		DeserializationConfig config = mapper.getDeserializationConfig();
		AnnotationIntrospector pair = new AnnotationIntrospector.Pair(config.getAnnotationIntrospector(), new ConstructorPropertiesAnnotationIntrospector());
		config = config.withAnnotationIntrospector(pair);
		return mapper.setDeserializationConfig(config);
	}

	private ObjectMapperFactory() {
	}

}
