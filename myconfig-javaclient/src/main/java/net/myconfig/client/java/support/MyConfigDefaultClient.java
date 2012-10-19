package net.myconfig.client.java.support;

import static java.lang.String.format;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import net.myconfig.core.utils.MapBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.netbeetle.jackson.ObjectMapperFactory;

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
		return delete(format("/ui/application/%d", id), Ack.class);
	}

	/**
	 * GET /ui/application/{application}
	 */
	@Override
	public ApplicationConfiguration applicationConfiguration(int id) {
		return get(format("/ui/application/%d", id), ApplicationConfiguration.class);
	}

	/**
	 * PUT /ui/version/{application}/{name:.*}
	 */
	@Override
	public Ack versionCreate(int id, String name) {
		return put (String.format("/ui/version/%d/%s", id, name), Ack.class);
	}

	/**
	 * DELETE /ui/version/{application}/{name:.*}
	 */
	@Override
	public Ack versionDelete(int id, String name) {
		return delete (format("/ui/version/%d/%s", id, name), Ack.class);
	}

	/**
	 * PUT /ui/environment/{application}/{name:.*}
	 */
	@Override
	public Ack environmentCreate(int id, String name) {
		return put(String.format("/ui/environment/%d/%s", id, name), Ack.class);
	}

	/**
	 * DELETE /ui/environment/{application}/{name}
	 */
	@Override
	public Ack environmentDelete(int id, String name) {
		return delete (format("/ui/environment/%d/%s", id, name), Ack.class);
	}

	/**
	 * DELETE /ui/key/{id}/{name:.*}
	 */
	@Override
	public Ack keyDelete(int id, String name) {
		return delete (format("/ui/key/%d/%s", id, name), Ack.class);
	}

	/**
	 * <p>POST /ui/key/{application}/{name}/create
	 * <p>param: description
	 */
	@Override
	public Ack keyCreate(int id, String name, String description) {
		return post(format("/ui/key/%d/%s/create", id, name), Ack.class, Collections.singletonMap("description", description));
	}

	/**
	 * <p>POST /ui/key/{application}/{name}/update
	 * <p>param: description
	 */
	@Override
	public Ack keyUpdate(int id, String name, String description) {
		return post(format("/ui/key/%d/%s/update", id, name), Ack.class, Collections.singletonMap("description", description));
	}

	/**
	 * GET /ui/application/{id}/key_version
	 */
	@Override
	public MatrixConfiguration keyVersionConfiguration(int id) {
		return get(format("/ui/application/%d/key_version", id), MatrixConfiguration.class);
	}

	/**
	 * POST /ui/version/{application}/{version}/add/{key:.*}
	 */
	@Override
	public Ack keyVersionAdd(int application, String version, String key) {
		return post(format("/ui/version/%d/%s/add/%s", application, version, key), Ack.class, null);
	}

	/**
	 * POST /ui/version/{application}/{version}/remove/{key:.*}
	 */
	@Override
	public Ack keyVersionRemove(int application, String version, String key) {
		return post(format("/ui/version/%d/%s/remove/%s", application, version, key), Ack.class, null);
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

	/**
	 * <p>POST /ui/configuration/{application:\\d+}
	 * <p>Body: JSON updates
	 */
	@Override
	public Ack updateConfiguration(int application, ConfigurationUpdates updates) {
		return post (String.format("/ui/configuration/%s", application), Ack.class, updates);
	}

	/**
	 * GET /ui/security/users
	 */
	@Override
	public UserSummaries users() {
		return get ("/ui/security/users", UserSummaries.class);
	}

	/**
	 * <p>POST /ui/user/{name:.*}
	 * <p>param: displayName
	 * <p>param: email
	 */
	@Override
	public Ack userCreate(String name, String displayName, String email) {
		return post (format("/ui/user/%s", name), Ack.class, MapBuilder.<String,String>create().put("displayName", displayName).put("email", email).build());
	}

	/**
	 * DELETE /ui/user/{name:.*}
	 */
	@Override
	public Ack userDelete(String name) {
		return delete (format("/ui/user/%s", name), Ack.class);
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
	
	@Override
	public String key(String application, String version, String environment, String key) {
		return get(String.format(
				"/get/key/%s/%s/%s/%s",
				application,
				environment,
				version,
				key
			), String.class);
	}
	
	/**
	 * GET /get/env/{application}/{environment}/{version}/{mode}
	 */
	@Override
	public void env(OutputStream out, String application, String version, String environment, String format) throws IOException {
		env(out, String.format("/get/env/%s/%s/%s/%s", application, environment, version, format));
	}
	
	/**
	 * GET /get/env/{application}/{environment}/{version}/{mode}/{variant}
	 */
	@Override
	public void env(OutputStream out, String application, String version, String environment, String format, String variant) throws IOException {
		env(out, String.format("/get/env/%s/%s/%s/%s/%s", application, environment, version, format, variant));
	}

	protected void env(OutputStream out, String path) {
		// Gets the HTTP client
		HttpClient client = new DefaultHttpClient();
		// Get request
		HttpGet get = new HttpGet(getUrl(path));
		// Executes the call
		try {
			HttpResponse response = client.execute(get);
			// Parses the response
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				// OK response
				HttpEntity entity = response.getEntity();
				// Copy
				IOUtils.copy(entity.getContent(), out);
				// OK
				EntityUtils.consume(entity);
			} else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				String content = EntityUtils.toString(response.getEntity(), "UTF-8");
				if (StringUtils.isNotBlank(content)) {
					throw new ClientMessageException(content);
				} else {
					// Generic error
					throw new ClientServerException(
							get,
							statusCode,
							response.getStatusLine().getReasonPhrase());
				}
			} else {
				// Generic error
				throw new ClientServerException(
						get,
						statusCode,
						response.getStatusLine().getReasonPhrase());
			}
		} catch (IOException e) {
			// TODO Management of client exceptions
			throw new RuntimeException("Error while executing " + get, e);
		}
	}

	protected <T> T get(String path, Class<T> returnType) {
		return request(new HttpGet(getUrl(path)), returnType);
	}

	protected <T> T put(String path, Class<T> returnType) {
		return request(new HttpPut(getUrl(path)), returnType);
	}

	protected <T> T post(String path, Class<T> returnType, Map<String, String> parameters) {
		HttpPost post = new HttpPost(getUrl(path));
		if (parameters != null) {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			for (Map.Entry<String, String> param: parameters.entrySet()) {
				nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
			try {
				post.setEntity(new UrlEncodedFormEntity(nvps));
			} catch (UnsupportedEncodingException e) {
				// TODO Management of client exceptions
				throw new RuntimeException (e);
			}
		}
		return request(post, returnType);
	}

	protected <T> T post(String path, Class<T> returnType, Object body) {
		HttpPost post = new HttpPost(getUrl(path));
		if (body != null) {
			try {
				String json = ObjectMapperFactory.createObjectMapper().writeValueAsString(body);
				post.setEntity(new StringEntity(json, ContentType.create("application/json", "UTF-8")));
			} catch (IOException e) {
				// TODO Management of client exceptions
				throw new RuntimeException (e);
			}
		}
		return request(post, returnType);
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
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
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
					ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
					return mapper.readValue(content, returnType);
				}
			} else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				String content = EntityUtils.toString(response.getEntity(), "UTF-8");
				if (StringUtils.isNotBlank(content)) {
					throw new ClientMessageException(content);
				} else {
					// Generic error
					throw new ClientServerException(
							request,
							statusCode,
							response.getStatusLine().getReasonPhrase());
				}
			} else {
				// Generic error
				throw new ClientServerException(
						request,
						statusCode,
						response.getStatusLine().getReasonPhrase());
			}
		} catch (IOException e) {
			// TODO Management of client exceptions
			throw new RuntimeException("Error while executing " + request, e);
		}
	}

}
