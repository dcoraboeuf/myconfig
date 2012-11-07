package net.myconfig.web.rest;

import java.util.Locale;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ApplicationUsers;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.EnvironmentUsers;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.UserSummaries;
import net.myconfig.core.model.VersionConfiguration;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.web.support.ErrorHandler;
import net.sf.jstring.Localizable;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ui")
public class UIController extends AbstractRESTController implements UIInterface {

	private final SecurityService securityService;

	@Autowired
	public UIController(Strings strings, ErrorHandler errorHandler, MyConfigService myConfigService, SecurityService securityService) {
		super(strings, errorHandler, myConfigService);
		this.securityService = securityService;
	}
	
	/**
	 * Forces the login of the user for pure-UI calls.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public @ResponseBody Ack login() {
		return Ack.OK;
	}

	@Override
	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	public @ResponseBody ApplicationSummaries applications() {
		return getMyConfigService().getApplications();
	}
	
	@Override
	@RequestMapping(value = "/application/{id}/{name:.*}", method = RequestMethod.PUT)
	public @ResponseBody ApplicationSummary applicationCreate(@PathVariable String id, @PathVariable String name) {
		return getMyConfigService().createApplication (id, name);
	}
	
	@Override
	@RequestMapping(value = "/application/{id:.*}", method = RequestMethod.DELETE)
	public @ResponseBody Ack applicationDelete(@PathVariable String id) {
		return getMyConfigService().deleteApplication (id);
	}
	
	@Override
	@RequestMapping(value = "/application/{id:.*}", method = RequestMethod.GET)
	public @ResponseBody ApplicationConfiguration applicationConfiguration(@PathVariable String id) {
		return getMyConfigService().getApplicationConfiguration (id);
	}
	
	@Override
	@RequestMapping(value = "/configuration/version/{id}/{version:.*}", method = RequestMethod.GET)
	public @ResponseBody VersionConfiguration versionConfiguration(@PathVariable String id, @PathVariable  String version) {
		return getMyConfigService().getVersionConfiguration (id, version);
	}
	
	@Override
	@RequestMapping(value = "/configuration/environment/{id}/{environment:.*}", method = RequestMethod.GET)
	public @ResponseBody EnvironmentConfiguration environmentConfiguration(@PathVariable String id, @PathVariable  String environment) {
		return getMyConfigService().getEnvironmentConfiguration (id, environment);
	}
	
	@Override
	@RequestMapping(value = "/configuration/key/{id}/{key:.*}", method = RequestMethod.GET)
	public @ResponseBody KeyConfiguration keyConfiguration(@PathVariable String id, @PathVariable  String key) {
		return getMyConfigService().getKeyConfiguration (id, key);
	}
	
	@Override
	@RequestMapping(value = "/configuration/{id:.*}", method = RequestMethod.POST)
	public @ResponseBody Ack updateConfiguration(@PathVariable String id, @RequestBody ConfigurationUpdates updates) {
		return getMyConfigService().updateConfiguration (id, updates);
	}
	
	@Override
	@RequestMapping(value = "/version/{id}/{name:.*}", method = RequestMethod.PUT)
	public @ResponseBody Ack versionCreate(@PathVariable String id, @PathVariable String name) {
		return getMyConfigService().createVersion (id, name);
	}
	
	@Override
	@RequestMapping(value = "/version/{id}/{name:.*}", method = RequestMethod.DELETE)
	public @ResponseBody Ack versionDelete(@PathVariable String id, @PathVariable String name) {
		return getMyConfigService().deleteVersion (id, name);
	}
	
	@Override
	@RequestMapping(value = "/environment/{id}/{name:.*}", method = RequestMethod.PUT)
	public @ResponseBody Ack environmentCreate(@PathVariable String id, @PathVariable String name) {
		return getMyConfigService().createEnvironment (id, name);
	}
	
	@Override
	@RequestMapping(value = "/environment/{id}/{name:.*}", method = RequestMethod.DELETE)
	public @ResponseBody Ack environmentDelete(@PathVariable String id, @PathVariable String name) {
		return getMyConfigService().deleteEnvironment (id, name);
	}
	
	@Override
	@RequestMapping(value = "/key/{id}/{name}/create", method = RequestMethod.POST)
	public @ResponseBody Ack keyCreate(@PathVariable String id, @PathVariable String name, @RequestParam String description, @RequestParam(required = false) String typeId, @RequestParam(required = false) String typeParam) {
		return getMyConfigService().createKey (id, name, description, typeId, typeParam);
	}
	
	@Override
	@RequestMapping(value = "/key/{id}/{name}/update", method = RequestMethod.POST)
	public @ResponseBody Ack keyUpdate(@PathVariable String id, @PathVariable String name, @RequestParam String description) {
		return getMyConfigService().updateKey (id, name, description);
	}
	
	@Override
	@RequestMapping(value = "/key/{id}/{name:.*}", method = RequestMethod.DELETE)
	public @ResponseBody Ack keyDelete(@PathVariable String id, @PathVariable String name) {
		return getMyConfigService().deleteKey (id, name);
	}
	
	@Override
	@RequestMapping(value = "/application/{id}/key_version", method = RequestMethod.GET)
	public @ResponseBody MatrixConfiguration keyVersionConfiguration(@PathVariable String id) {
		return getMyConfigService().keyVersionConfiguration(id);
	}

	@Override
	@RequestMapping(value = "/version/{id}/{version}/add/{key:.*}", method = RequestMethod.POST)
	public @ResponseBody Ack keyVersionAdd (@PathVariable String id, @PathVariable String version, @PathVariable String key) {
		return getMyConfigService().addKeyVersion (id, version, key);
	}

	@Override
	@RequestMapping(value = "/version/{id}/{version}/remove/{key:.*}", method = RequestMethod.POST)
	public @ResponseBody Ack keyVersionRemove (@PathVariable String id, @PathVariable String version, @PathVariable String key) {
		return getMyConfigService().removeKeyVersion (id, version, key);
	}
	
	@Override
	@RequestMapping(value = "/security/users", method = RequestMethod.GET)
	public @ResponseBody UserSummaries users() {
		return securityService.getUserList();
	}
	
	@Override
	@RequestMapping(value = "/user/{name:.*}", method = RequestMethod.POST)
	public @ResponseBody Ack userCreate(@PathVariable String name, @RequestParam String displayName, @RequestParam String email) {
		return securityService.userCreate(name, displayName, email);
	}
	
	@Override
	@RequestMapping(value = "/user/{name:.*}", method = RequestMethod.DELETE)
	public @ResponseBody Ack userDelete(@PathVariable String name) {
		return securityService.userDelete(name);
	}

	@Override
	@RequestMapping(value = "/user/{name}/function/{fn}/add", method = RequestMethod.POST)
	public @ResponseBody Ack userFunctionAdd (@PathVariable String name, @PathVariable UserFunction fn) {
		return securityService.userFunctionAdd (name, fn);
	}

	@Override
	@RequestMapping(value = "/user/{name}/function/{fn}/remove", method = RequestMethod.POST)
	public @ResponseBody Ack userFunctionRemove (@PathVariable String name, @PathVariable UserFunction fn) {
		return securityService.userFunctionRemove (name, fn);
	}
	
	@Override
	@RequestMapping(value = "/user/{user}/application/{application}/function/{fn}/add", method = RequestMethod.POST)
	public @ResponseBody Ack appFunctionAdd(@PathVariable String user, @PathVariable String application, @PathVariable AppFunction fn) {
		return securityService.appFunctionAdd (application, user, fn);
	}
	
	@Override
	@RequestMapping(value = "/user/{user}/application/{application}/function/{fn}/remove", method = RequestMethod.POST)
	public @ResponseBody Ack appFunctionRemove(@PathVariable String user, @PathVariable String application, @PathVariable AppFunction fn) {
		return securityService.appFunctionRemove (application, user, fn);
	}

	@Override
	@RequestMapping(value = "/security/mode/{mode}", method = RequestMethod.POST)
	public @ResponseBody Ack setSecurityMode(@PathVariable String mode) {
		securityService.setSecurityMode(mode);
		return Ack.OK;
	}
	
	@Override
	@RequestMapping(value = "/user/{name}/confirm/{token}", method = RequestMethod.POST)
	public @ResponseBody Ack userConfirm(@PathVariable String name, @PathVariable String token, @RequestParam String password) {
		securityService.userConfirm(name, token, password);
		return Ack.OK;
	}
	
	@Override
	@RequestMapping(value = "/application/{application}/users", method = RequestMethod.GET)
	public @ResponseBody ApplicationUsers applicationUsers(@PathVariable String application) {
		return getMyConfigService().getApplicationUsers(application);
	}
	
	@Override
	@RequestMapping(value = "/application/{application}/environment/{environment}/users", method = RequestMethod.GET)
	public @ResponseBody EnvironmentUsers environmentUsers(@PathVariable String application, @PathVariable String environment) {
		return getMyConfigService().getEnvironmentUsers(application, environment);
	}
	
	@Override
	@RequestMapping(value = "/user/{user}/application/{application}/environment/{environment}/function/{fn}/add", method = RequestMethod.POST)
	public @ResponseBody Ack envFunctionAdd(@PathVariable String user, @PathVariable String application, @PathVariable String environment, @PathVariable EnvFunction fn) {
		return securityService.envFunctionAdd (application, user, environment, fn);
	}
	
	@Override
	@RequestMapping(value = "/user/{user}/application/{application}/environment/{environment}/function/{fn}/remove", method = RequestMethod.POST)
	public @ResponseBody Ack envFunctionRemove(@PathVariable String user, @PathVariable String application, @PathVariable String environment, @PathVariable EnvFunction fn) {
		return securityService.envFunctionRemove (application, user, environment, fn);
	}
	
	@Override
	@RequestMapping(value = "/type/{typeId}/validate/param", method = RequestMethod.POST)
	public @ResponseBody String typeParameterValidate(Locale locale, @PathVariable String typeId, @RequestParam String typeParam) {
		Localizable message = getMyConfigService().validateTypeParameter (typeId, typeParam);
		if (message != null) {
			return message.getLocalizedMessage(strings, locale);
		} else {
			return "";
		}
	}
	
	@Override
	@RequestMapping(value = "/type/{typeId}/validate/value", method = RequestMethod.POST)
	public @ResponseBody String typeValueValidate(Locale locale, @PathVariable String typeId, @RequestParam String typeParam, @RequestParam String value) {
		Localizable message = getMyConfigService().validateTypeValue (typeId, typeParam, value);
		if (message != null) {
			return message.getLocalizedMessage(strings, locale);
		} else {
			return "";
		}
	}

}
