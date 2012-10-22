package net.myconfig.client.java.support;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.myconfig.client.java.Client;
import net.myconfig.core.model.Ack;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.netbeetle.jackson.ObjectMapperFactory;

public abstract class AbstractClient implements Client {
	
	protected static interface ResponseHandler<T> {

		T handleResponse(HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ParseException, IOException;
		
	}
	
	protected static class NOPResponseHandler implements ResponseHandler<Void> {
		
		@Override
		public Void handleResponse(HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ParseException, IOException {
			return null;
		}
		
	}
		
	protected static abstract class BaseResponseHandler<T> implements ResponseHandler<T> {

		@Override
		public T handleResponse(HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ParseException, IOException {
			// Parses the response
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return handleEntity (entity);
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
		}

		protected abstract T handleEntity(HttpEntity entity) throws ParseException, IOException;
		
	}

	private final String url;
	private final DefaultHttpClient client;

	public AbstractClient(String url) {
		this.url = url;
		this.client = new DefaultHttpClient();
	}

	public String getUrl() {
		return url;
	}
	
	@Override
	public void logout() {
		// Executes the call
		request (new HttpGet(getUrl("/logout")), new NOPResponseHandler());
	}
	
	@Override
	public void login(String name, String password) {
		// Forces the logout
		logout();
		// Configures the client for the credentials
		client.getCredentialsProvider().setCredentials(
                new AuthScope(null, -1),
                new UsernamePasswordCredentials(name, password));
		// Gets the server to send a challenge back
		get("/ui/login", Ack.class);
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

	protected <T> T request(HttpRequestBase request, final Class<T> returnType) {
		return request (request, new BaseResponseHandler<T>() {
			@Override
			protected T handleEntity(HttpEntity entity) throws ParseException, IOException {
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
			}
		});
	}

	protected <T> T request(HttpRequestBase request, ResponseHandler<T> responseHandler) {
		// Executes the call
		try {
			HttpResponse response = client.execute(request);
			// Entity response
			HttpEntity entity = response.getEntity();
			try {
				return responseHandler.handleResponse (request, response, entity);
			} finally {
				EntityUtils.consume(entity);
			}
		} catch (IOException e) {
			throw new ClientGeneralException(request, e);
		} finally {
			request.releaseConnection();
		}
	}

}
