package net.myconfig.client.java.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.myconfig.client.java.MyConfigClient;

public class MyConfigClientUtils {

	public static String envAsString(MyConfigClient client, String application, String version, String environment, String format, String variant) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			client.env(out, application, version, environment, format, variant);
		} finally {
			out.close();
		}
		return out.toString("UTF-8");
	}

	public static String envAsString(MyConfigClient client, String application, String version, String environment, String format) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			client.env(out, application, version, environment, format);
		} finally {
			out.close();
		}
		return out.toString("UTF-8");
	}

}
