package net.myconfig.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public final class Helper {

	public static String getResourceAsString(String path) throws IOException {
		InputStream in = Helper.class.getResourceAsStream(path);
		if (in == null) {
			throw new IOException("Cannot find resource at " + path);
		} else {
			try {
				return IOUtils.toString(in, "UTF-8");
			} finally {
				in.close();
			}
		}
	}

	private Helper() {
	}

}
