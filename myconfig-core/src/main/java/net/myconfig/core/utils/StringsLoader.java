package net.myconfig.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sf.jstring.Strings;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringsLoader {
	
	private final Logger logger = LoggerFactory.getLogger(StringsLoader.class);

	public Strings load() throws IOException {
		logger.info("[strings] Setting default locale to English");
		Locale.setDefault(Locale.ENGLISH);
		logger.info("[strings] Loading all strings...");
		Set<String> paths = new HashSet<String>();
		Strings strings = new Strings();
		// Adds all properties in META-INF/resources
		Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/resources/strings");
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			logger.info("[strings] Trying to load URL {}", url);
			try (InputStream in = url.openStream()) {
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
			}
		}
		// OK
		return strings;
	}

}
