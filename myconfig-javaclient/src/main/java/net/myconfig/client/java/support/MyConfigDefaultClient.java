package net.myconfig.client.java.support;

import static java.lang.String.format;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Locale;

import net.myconfig.client.java.MyConfigClient;
import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ApplicationUsers;
import net.myconfig.core.model.ConfigurationDescription;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.EnvironmentUsers;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.UserSummaries;
import net.myconfig.core.model.VersionConfiguration;
import net.myconfig.core.type.ConfigurationValidationInput;
import net.myconfig.core.type.ConfigurationValidationOutput;
import net.myconfig.core.type.ValueType;
import net.myconfig.core.type.ValueTypeDescriptions;
import net.myconfig.core.utils.MapBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class MyConfigDefaultClient extends AbstractClient implements MyConfigClient {

	public MyConfigDefaultClient(String url) {
		super(url);
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
	public ApplicationSummary applicationCreate(String id, String name) {
		return put(String.format("/ui/application/%s/%s", id, name), ApplicationSummary.class);
	}

	@Override
	public Ack applicationDelete(String id) {
		return delete(format("/ui/application/%s", id), Ack.class);
	}

	/**
	 * GET /ui/application/{application}
	 */
	@Override
	public ApplicationConfiguration applicationConfiguration(String id) {
		return get(format("/ui/application/%s", id), ApplicationConfiguration.class);
	}

	/**
	 * PUT /ui/version/{application}/{name:.*}
	 */
	@Override
	public Ack versionCreate(String id, String name) {
		return put (String.format("/ui/version/%s/%s", id, name), Ack.class);
	}

	/**
	 * DELETE /ui/version/{application}/{name:.*}
	 */
	@Override
	public Ack versionDelete(String id, String name) {
		return delete (format("/ui/version/%s/%s", id, name), Ack.class);
	}

	/**
	 * PUT /ui/environment/{application}/{name:.*}
	 */
	@Override
	public Ack environmentCreate(String id, String name) {
		return put(String.format("/ui/environment/%s/%s", id, name), Ack.class);
	}

	/**
	 * DELETE /ui/environment/{application}/{name}
	 */
	@Override
	public Ack environmentDelete(String id, String name) {
		return delete (format("/ui/environment/%s/%s", id, name), Ack.class);
	}
	
	/**
	 * PUT /environment/{id}/{environment}/up
	 */
	@Override
	public Ack environmentUp(String id, String environment) {
		return put (format("/ui/environment/%s/%s/up", id, environment), Ack.class);
	}
	
	/**
	 * PUT /environment/{id}/{environment}/down
	 */
	@Override
	public Ack environmentDown(String id, String environment) {
		return put (format("/ui/environment/%s/%s/down", id, environment), Ack.class);
	}

	/**
	 * DELETE /ui/key/{id}/{name:.*}
	 */
	@Override
	public Ack keyDelete(String id, String name) {
		return delete (format("/ui/key/%s/%s", id, name), Ack.class);
	}

	/**
	 * <p>POST /ui/key/{application}/{name}/create
	 * <p>param: description
	 * <p>param: typeId
	 * <p>param: typeParam
	 */
	@Override
	public Ack keyCreate(String id, String name, String description, String typeId, String typeParam) {
		return post(format("/ui/key/%s/%s/create", id, name), Ack.class,
				MapBuilder.<String,String>create()
				.put("description", description)
				.put("typeId", typeId)
				.put("typeParam", typeParam).build());
	}

	/**
	 * <p>POST /ui/key/{application}/{name}/update
	 * <p>param: description
	 */
	@Override
	public Ack keyUpdate(String id, String name, String description) {
		return post(format("/ui/key/%s/%s/update", id, name), Ack.class, Collections.singletonMap("description", description));
	}

	/**
	 * GET /ui/application/{id}/key_version
	 */
	@Override
	public MatrixConfiguration keyVersionConfiguration(String id) {
		return get(format("/ui/application/%s/key_version", id), MatrixConfiguration.class);
	}

	/**
	 * POST /ui/version/{application}/{version}/add/{key:.*}
	 */
	@Override
	public Ack keyVersionAdd(String application, String version, String key) {
		return post(format("/ui/version/%s/%s/add/%s", application, version, key), Ack.class, null);
	}

	/**
	 * POST /ui/version/{application}/{version}/remove/{key:.*}
	 */
	@Override
	public Ack keyVersionRemove(String application, String version, String key) {
		return post(format("/ui/version/%s/%s/remove/%s", application, version, key), Ack.class, null);
	}

	/**
	 * GET /ui/configuration/version/{application}/{version:.*}
	 */
	@Override
	public VersionConfiguration versionConfiguration(String application, String version) {
		return get (format("/ui/configuration/version/%s/%s", application, version), VersionConfiguration.class);
	}

	/**
	 * GET /ui/configuration/environment/{application}/{environment:.*}
	 */
	@Override
	public EnvironmentConfiguration environmentConfiguration(String application, String environment) {
		return get (format("/ui/configuration/environment/%s/%s", application, environment), EnvironmentConfiguration.class);
	}

	/**
	 * GET /ui/configuration/key/{application}/{key:.*}
	 */
	@Override
	public KeyConfiguration keyConfiguration(String application, String key) {
		return get (format("/ui/configuration/key/%s/%s", application, key), KeyConfiguration.class);
	}

	/**
	 * <p>POST /ui/configuration/{application:\\d+}
	 * <p>Body: JSON updates
	 */
	@Override
	public Ack updateConfiguration(String application, ConfigurationUpdates updates) {
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
	 * <p>POST /ui/user/{mode}/{name:.*}
	 * <p>param: displayName
	 * <p>param: email
	 */
	@Override
	public Ack userCreate(String mode, String name, String displayName, String email) {
		return post (format("/ui/user/%s/%s", mode, name), Ack.class, MapBuilder.<String,String>create().put("displayName", displayName).put("email", email).build());
	}

	/**
	 * DELETE /ui/user/{name:.*}
	 */
	@Override
	public Ack userDelete(String name) {
		return delete (format("/ui/user/%s", name), Ack.class);
	}

	/**
	 * POST /ui/user/{name}/function/{fn}/add
	 */
	@Override
	public Ack userFunctionAdd(String name, UserFunction fn) {
		return post (format("/ui/user/%s/function/%s/add", name, fn), Ack.class, null);
	}

	/**
	 * POST /ui/user/{name}/function/{fn}/remove
	 */
	@Override
	public Ack userFunctionRemove(String name, UserFunction fn) {
		return post (format("/ui/user/%s/function/%s/remove", name, fn), Ack.class, null);
	}

	/**
	 * POST /ui/user/{user}/application/{application}/function/{fn}/add
	 */
	@Override
	public Ack appFunctionAdd(String user, String application, AppFunction fn) {
		return post (format("/ui/user/%s/application/%s/function/%s/add", user, application, fn), Ack.class, null);
	}

	/**
	 * POST /ui/user/{user}/application/{application}/function/{fn}/remove
	 */
	@Override
	public Ack appFunctionRemove(String user, String application, AppFunction fn) {
		return post (format("/ui/user/%s/application/%s/function/%s/remove", user, application, fn), Ack.class, null);
	}

	/**
	 * POST /ui/user/{user}/application/{application}/environment/{environment}/function/{fn}/add
	 */
	@Override
	public Ack envFunctionAdd(String user, String application, String environment, EnvFunction fn) {
		return post (format("/ui/user/%s/application/%s/environment/%s/function/%s/add", user, application, environment, fn), Ack.class, null);
	}

	/**
	 * POST /ui/user/{user}/application/{application}/environment/{environment}/function/{fn}/remove
	 */
	@Override
	public Ack envFunctionRemove(String user, String application, String environment, EnvFunction fn) {
		return post (format("/ui/user/%s/application/%s/environment/%s/function/%s/remove", user, application, environment, fn), Ack.class, null);
	}

	/**
	 * POST /security/mode/{mode}
	 */
	@Override
	public Ack setSecurityMode(String mode) {
		return post(format("/ui/security/mode/%s", mode), Ack.class, null);
	}
	
	/**
	 * <p>POST /ui/user/{name}/confirm/{token}
	 * <p>param: password
	 */
	@Override
	public Ack userConfirm(String name, String token, String password) {
		return post(format("/ui/user/%s/confirm/%s", name, token), Ack.class, Collections.singletonMap("password", password));
	}
	
	/**
	 * GET /ui/application/{application:\\d+}/users
	 */
	@Override
	public ApplicationUsers applicationUsers(String application) {
		return get(format("/ui/application/%s/users", application), ApplicationUsers.class);
	}
	
	/**
	 * GET /ui/application/{application:\\d+}/environment/{environment}/users
	 */
	@Override
	public EnvironmentUsers environmentUsers(String application, String environment) {
		return get(format("/ui/application/%s/environment/%s/users", application, environment), EnvironmentUsers.class);
	}
	
	/**
	 * POST /ui/type/{typeId}/validate/param
	 */
	@Override
	public String typeParameterValidate(Locale locale, String typeId, String parameter) {
		// TODO Locale
		return post(format("/ui/type/%s/validate/param", normalizeTypeId(typeId)), String.class, MapBuilder.<String, String>create().put("typeParam",parameter).build());
	}
	
	/**
	 * POST /ui/type/{typeId}/validate/value
	 */
	@Override
	public String typeValueValidate(Locale locale, String typeId, String parameter, String value) {
		// TODO Locale
		return post(format("/ui/type/%s/validate/value", normalizeTypeId(typeId)), String.class, MapBuilder.<String, String>create().put("typeParam",parameter).put("value",value).build());
	}
	
	/**
	 * GET /ui/types
	 */
	@Override
	public ValueTypeDescriptions types() {
		return get("/ui/types", ValueTypeDescriptions.class);
	}
	
	/**
	 * <p>POST /ui/configuration/{id}/validate
	 * <p>Body: JSON
	 */
	@Override
	public ConfigurationValidationOutput configurationValidate(Locale locale, String application, ConfigurationValidationInput input) {
		// TODO Locale
		return post(String.format("/ui/configuration/%s/validate", application), ConfigurationValidationOutput.class, input);
	}

	protected String normalizeTypeId(String typeId) {
		return StringUtils.isBlank(typeId) ? ValueType.PLAIN : typeId;
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
	 * GET /get/configuration/{application}/{version:.*}
	 */
	@Override
	public ConfigurationDescription configuration(String application, String version) {
		return get(String.format("/get/configuration/%s/%s", application, version), ConfigurationDescription.class);
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
			throw new ClientGeneralException(get, e);
		}
	}

}
