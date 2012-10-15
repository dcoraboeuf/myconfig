package net.myconfig.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import net.myconfig.core.model.ConfigurationUpdate;
import net.myconfig.core.model.ConfigurationUpdates;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

public class VersionConfigurationUpdateTest {
	
	@Test
	public void parse_string () throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"updates\":[{\"environment\":\"DEV\",\"key\":\"a\",\"value\":\"dev a\"},{\"environment\":\"DEV\",\"key\":\"b\",\"value\":\"dev b\"}]}";

		ObjectMapper mapper = new ObjectMapper();
		ConfigurationUpdates updates = mapper.readValue(json, ConfigurationUpdates.class);
		assertNotNull (updates);
		List<ConfigurationUpdate> list = updates.getUpdates();
		assertNotNull (list);
		assertEquals (2, list.size());
		{
			assertEquals ("DEV", list.get(0).getEnvironment());
			assertEquals ("a", list.get(0).getKey());
			assertEquals ("dev a", list.get(0).getValue());
		}
		{
			assertEquals ("DEV", list.get(1).getEnvironment());
			assertEquals ("b", list.get(1).getKey());
			assertEquals ("dev b", list.get(1).getValue());
		}
		
	}
	
	@Test
	public void parse_tree () throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory f = JsonNodeFactory.instance;
		// JSON tree
		ObjectNode root = f.objectNode();
		ArrayNode array = root.putArray("updates");
		{
			ObjectNode o = array.addObject();
			o.put("environment", "ENV1");
			o.put("key", "KEY1");
			o.put("value", "VALUE1");
		}
		{
			ObjectNode o = array.addObject();
			o.put("environment", "ENV2");
			o.put("key", "KEY2");
			o.put("value", "VALUE2");
		}
		// As JSON string
		String json = mapper.writeValueAsString(root);
		// Tries to parse
		ConfigurationUpdates updates = mapper.readValue(json, ConfigurationUpdates.class);
		assertNotNull (updates);
		List<ConfigurationUpdate> list = updates.getUpdates();
		assertNotNull (list);
		assertEquals (2, list.size());
		{
			assertEquals ("ENV1", list.get(0).getEnvironment());
			assertEquals ("KEY1", list.get(0).getKey());
			assertEquals ("VALUE1", list.get(0).getValue());
		}
		{
			assertEquals ("ENV2", list.get(1).getEnvironment());
			assertEquals ("KEY2", list.get(1).getKey());
			assertEquals ("VALUE2", list.get(1).getValue());
		}
	}

}
