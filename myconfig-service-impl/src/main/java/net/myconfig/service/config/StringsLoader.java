package net.myconfig.service.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		logger.info("[strings] Loading all strings...");
		Set<String> paths = new HashSet<String>();
		Strings strings = new Strings();
		// Adds all properties in META-INF/resources
		Resource[] resources = applicationContext.getResources("classpath*:META-INF/resources/strings");
		for (Resource resource : resources) {
			URI uri = resource.getURI();
			logger.info("[strings] Trying to load uri {}", uri);
			// JDK7 Reads the content
			InputStream in = resource.getInputStream();
			try {
				List<String> lines = IOUtils.readLines(in);
				for (String line : lines) {
					if (StringUtils.isNotBlank(line)) {
						line = StringUtils.trim(line);
						String path = String.format("META-INF.resources.%s", line);
						if (paths.contains(path)) {
							logger.error("[strings] {} path has already been added", path);
							throw new IllegalStateException("Duplicated strings bundle");
						} else {
							logger.info("[strings] Adding path {}", path);
							paths.add(path);
							strings.add(path);
						}
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
