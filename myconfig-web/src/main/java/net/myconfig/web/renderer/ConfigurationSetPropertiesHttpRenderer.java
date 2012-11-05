package net.myconfig.web.renderer;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.core.model.ConfigurationValue;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationSetPropertiesHttpRenderer extends AbstractHttpRenderer<ConfigurationSet> {
	
	@Override
	public void renderer(ConfigurationSet set, String variant, HttpServletResponse response) throws IOException {
		String properties = renderProperties (set);
		// Encoding
		byte[] bytes = properties.getBytes("US-ASCII");
		// Writes the response
		response.setContentType("text/plain");
		response.setContentLength(bytes.length);
		response.setCharacterEncoding("US-ASCII");
		// Output
		ServletOutputStream output = response.getOutputStream();
		output.write(bytes);
		output.flush();
	}
	
	protected String renderProperties(ConfigurationSet set) {
		StringWriter writer = new StringWriter();
		renderProperties(writer, set);
		return writer.toString();
	}

	protected void renderProperties(StringWriter writer, ConfigurationSet set) {
		writer.write (String.format ("# Configuration properties for '%s' (%s)\n", set.getId(), set.getName()));
		writer.write (String.format ("# Version: %s\n", set.getVersion()));
		writer.write (String.format ("# Environment: %s\n\n", set.getEnvironment()));
		for (ConfigurationValue confValue : set.getValues()) {
			writer.write(String.format("# %s\n%s = %s\n\n",
					StringEscapeUtils.escapeJava(confValue.getDescription()),
					confValue.getKey(),
					StringEscapeUtils.escapeJava(confValue.getValue())
					));
		}
	}

	@Override
	public String getContentType() {
		return "properties";
	}
	
	@Override
	public boolean appliesTo(Class<?> type) {
		return ConfigurationSet.class.isAssignableFrom(type);
	}

}
