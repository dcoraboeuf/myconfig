package net.myconfig.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.netbeetle.jackson.ObjectMapperFactory;

public class VersionTest {

	@Test(expected = JsonMappingException.class)
	public void json_with_no_factory() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		json_test(mapper);
	}

	@Test
	public void json_with_factory() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
		json_test(mapper);
	}

	protected void json_test(ObjectMapper mapper) throws IOException, JsonGenerationException, JsonMappingException, JsonParseException {
		Version version = new Version("1.0");
		String json = mapper.writeValueAsString(version);
		assertEquals("{\"name\":\"1.0\"}", json);
		Version o = mapper.readValue(json, Version.class);
		assertNotNull(o);
		assertEquals("1.0", o.getName());
	}

}
