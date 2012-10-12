package net.myconfig.client.java.support;

import java.io.IOException;
import java.io.InputStream;

import net.myconfig.client.java.MyConfigClient;
import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.UserSummaries;
import net.myconfig.core.model.VersionConfiguration;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

public class MyConfigDefaultClient implements MyConfigClient {

	private final String url;

	public MyConfigDefaultClient(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	@Override
	public String version() {
		return get ("/version", String.class);
	}

	@Override
	public ApplicationSummaries applications() {
		return get ("/ui/applications", ApplicationSummaries.class);
	}

	@Override
	public ApplicationSummary applicationCreate(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack applicationDelete(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationConfiguration applicationConfiguration(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack versionCreate(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack versionDelete(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack environmentCreate(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack environmentDelete(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyDelete(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyCreate(int id, String name, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyUpdate(int id, String name, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MatrixConfiguration keyVersionConfiguration(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyVersionAdd(int application, String version, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyVersionRemove(int application, String version, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionConfiguration versionConfiguration(int application, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnvironmentConfiguration environmentConfiguration(int application, String environment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KeyConfiguration keyConfiguration(int application, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack updateConfiguration(int application, ConfigurationUpdates updates) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserSummaries users() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack userCreate(String name, String displayName, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack userDelete(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack userFunctionAdd(String name, UserFunction fn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack userFunctionRemove(String name, UserFunction fn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack appFunctionAdd(String user, int application, AppFunction fn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack appFunctionRemove(String user, int application, AppFunction fn) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected <T> T get (String path, Class<T> returnType) {
		// Gets the HTTP client
		HttpClient client = new DefaultHttpClient();
		// Method
		HttpGet get = new HttpGet(getUrl(path));
		// Executes the call
		try {
			HttpResponse response = client.execute(get);
			// Parses the response
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// OK response
				InputStream o = response.getEntity().getContent();
				try {
					// Parses the response
					ObjectMapper mapper = new ObjectMapper();
					return mapper.readValue(o, returnType);
				} finally {
					o.close();
				}
			} else {
				// FIXME Error
				return null;
			}
		} catch (IOException e) {
			// FIXME Error
			return null;
		}
	}

	protected String getUrl(String path) {
		return url + path;
	}

}
