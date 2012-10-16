package com.netbeetle.jackson;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;


public final class ObjectMapperFactory {

	public static ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		DeserializationConfig config = mapper.getDeserializationConfig().withAnnotationIntrospector(new ConstructorPropertiesAnnotationIntrospector());
		mapper.setDeserializationConfig(config);
		return mapper;
	}

	private ObjectMapperFactory() {
	}

}
