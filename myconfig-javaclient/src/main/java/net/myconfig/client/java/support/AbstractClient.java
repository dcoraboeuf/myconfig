package net.myconfig.client.java.support;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.myconfig.client.java.Client;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
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

public abstract class AbstractClient<C extends Client<C>> implements Client<C> {

	private final String url;
	private final Credentials credentials;

	public AbstractClient(String url) {
		this (url, null);
	}

	protected AbstractClient(String url, Credentials credentials) {
		this.url = url;
		this.credentials = credentials;
	}

	public String getUrl() {
		return url;
	}
	
	@Override
	public void logout() {
		// Gets the HTTP client
		HttpClient client = createHttpClient();
		// Executes the call
		try {
			client.execute(new HttpGet(getUrl("/logout")));
		} catch (IOException e) {
			throw new ClientGeneralException("Logout", e);
		}
	}

	protected DefaultHttpClient createHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		// Credentials
		if (credentials != null) {
			client.getCredentialsProvider().setCredentials(
					new AuthScope(null, -1),
                    new UsernamePasswordCredentials(credentials.getName(), credentials.getPassword()));
		}
		// OK
		return client;
	}
	
	@Override
	public C withLogin(String name, String password) {
		// Credentials
		Credentials credentials = new Credentials(name, password);
		// OK
		return withCredentials(credentials);
	}

	protected abstract C withCredentials(Credentials credentials);

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
				throw new ClientGeneralException(post, e);
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
				throw new ClientGeneralException(post, e);
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
		HttpClient client = createHttpClient();	
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
				if (returnType == null) {
					return null;
				} else if (String.class.isAssignableFrom(returnType)) {
					@SuppressWarnings("unchecked")
					T value = (T) content;
					return value;
				} else {
					ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
					return mapper.readValue(content, returnType);
				}
			} else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
				throw new ClientCannotLoginException(request);
			} else if (statusCode == HttpStatus.SC_FORBIDDEN) {
				throw new ClientForbiddenException(request);
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
			throw new ClientGeneralException(request, e);
		}
	}

}
