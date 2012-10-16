package net.myconfig.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.netbeetle.jackson.ObjectMapperFactory;

public class VersionSummaryTest {

	@Test
	public void json() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
		VersionSummary summary = new VersionSummary("1.0", 4, 3, 2);
		String json = mapper.writeValueAsString(summary);
		assertEquals("{\"name\":\"1.0\",\"keyCount\":4,\"configCount\":3,\"valueCount\":2}", json);
		VersionSummary o = mapper.readValue(json, VersionSummary.class);
		assertNotNull(o);
		assertEquals("1.0", o.getName());
		assertEquals(4, o.getKeyCount());
		assertEquals(3, o.getConfigCount());
		assertEquals(2, o.getValueCount());
	}

}
