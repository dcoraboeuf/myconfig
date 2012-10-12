package net.myconfig.client.java.support;

import java.io.IOException;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
		return get("/ui/version", String.class);
	}

	@Override
	public ApplicationSummaries applications() {
		return get("/ui/applications", ApplicationSummaries.class);
	}

	@Override
	public ApplicationSummary applicationCreate(String name) {
		return put(String.format("/ui/application/%s", name), ApplicationSummary.class);
	}

	@Override
	public Ack applicationDelete(int id) {
		return delete(String.format("/ui/application/%d", id), Ack.class);
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

	protected <T> T get(String path, Class<T> returnType) {
		return request(new HttpGet(getUrl(path)), returnType);
	}

	protected <T> T put(String path, Class<T> returnType) {
		return request(new HttpPut(getUrl(path)), returnType);
	}

	protected <T> T delete(String path, Class<T> returnType) {
		return request(new HttpDelete(getUrl(path)), returnType);
	}

	protected String getUrl(String path) {
		return url + path;
	}

	protected <T> T request(HttpUriRequest request, Class<T> returnType) {
		// Gets the HTTP client
		HttpClient client = new DefaultHttpClient();
		// Executes the call
		try {
			HttpResponse response = client.execute(request);
			// Parses the response
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// OK response
				HttpEntity entity = response.getEntity();
				// Gets the content as a JSON string
				String content = EntityUtils.toString(entity, "UTF-8");
				// Parses the response
				if (String.class.isAssignableFrom(returnType)) {
					@SuppressWarnings("unchecked")
					T value = (T) content;
					return value;
				} else {
					ObjectMapper mapper = new ObjectMapper();
					return mapper.readValue(content, returnType);
				}
			} else {
				throw new RuntimeException(String.format("Error while executing %s: [%d] %s",
					request,
					response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase()));
			}
		} catch (IOException e) {
			throw new RuntimeException("Error while executing " + request, e);
		}
	}

}
