package net.myconfig.service.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import net.sf.jstring.Strings;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class StringsLoader {
	
	private final Logger logger = LoggerFactory.getLogger(StringsLoader.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public Strings load() throws IOException {
		logger.debug("[strings] Loading all strings...");
		Strings strings = new Strings();
		// Adds all properties in META-INF/resources
		Resource[] resources = applicationContext.getResources("classpath*:META-INF/resources/strings");
		for (Resource resource : resources) {
			URI uri = resource.getURI();
			logger.debug("[strings] Trying to load uri {}", uri);
			// JDK7 Reads the content
			InputStream in = resource.getInputStream();
			try {
				List<String> lines = IOUtils.readLines(in);
				for (String line : lines) {
					if (StringUtils.isNotBlank(line)) {
						line = StringUtils.trim(line);
						String path = String.format("META-INF.resources.%s", line);
						logger.debug("[strings] Adding path {}", path);
						strings.add(path);
					}
				}
			} finally {
				in.close();
			}
		}
		// OK
		return strings;
	}

}
