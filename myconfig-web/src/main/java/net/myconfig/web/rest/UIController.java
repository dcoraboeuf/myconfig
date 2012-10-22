package net.myconfig.web.rest;

import net.myconfig.core.AppFunction;
import net.myconfig.core.MyConfigRoles;
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
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.web.support.ErrorHandler;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
	@Secured({MyConfigRoles.USER, MyConfigRoles.ADMIN})
	public @ResponseBody Ack login() {
		return Ack.OK;
	}

	@Override
	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	public @ResponseBody ApplicationSummaries applications() {
		return getMyConfigService().getApplications();
	}
	
	@Override
	@RequestMapping(value = "/application/{name:.*}", method = RequestMethod.PUT)
	public @ResponseBody ApplicationSummary applicationCreate(@PathVariable String name) {
		return getMyConfigService().createApplication (name);
	}
	
	@Override
	@RequestMapping(value = "/application/{application}", method = RequestMethod.DELETE)
	public @ResponseBody Ack applicationDelete(@PathVariable int application) {
		return getMyConfigService().deleteApplication (application);
	}
	
	@Override
	@RequestMapping(value = "/application/{application}", method = RequestMethod.GET)
	public @ResponseBody ApplicationConfiguration applicationConfiguration(@PathVariable int application) {
		return getMyConfigService().getApplicationConfiguration (application);
	}
	
	@Override
	@RequestMapping(value = "/configuration/version/{application}/{version:.*}", method = RequestMethod.GET)
	public @ResponseBody VersionConfiguration versionConfiguration(@PathVariable int application, @PathVariable  String version) {
		return getMyConfigService().getVersionConfiguration (application, version);
	}
	
	@Override
	@RequestMapping(value = "/configuration/environment/{application}/{environment:.*}", method = RequestMethod.GET)
	public @ResponseBody EnvironmentConfiguration environmentConfiguration(@PathVariable int application, @PathVariable  String environment) {
		return getMyConfigService().getEnvironmentConfiguration (application, environment);
	}
	
	@Override
	@RequestMapping(value = "/configuration/key/{application}/{key:.*}", method = RequestMethod.GET)
	public @ResponseBody KeyConfiguration keyConfiguration(@PathVariable int application, @PathVariable  String key) {
		return getMyConfigService().getKeyConfiguration (application, key);
	}
	
	@Override
	@RequestMapping(value = "/configuration/{application:\\d+}", method = RequestMethod.POST)
	public @ResponseBody Ack updateConfiguration(@PathVariable int application, @RequestBody ConfigurationUpdates updates) {
		return getMyConfigService().updateConfiguration (application, updates);
	}
	
	@Override
	@RequestMapping(value = "/version/{application}/{name:.*}", method = RequestMethod.PUT)
	public @ResponseBody Ack versionCreate(@PathVariable int application, @PathVariable String name) {
		return getMyConfigService().createVersion (application, name);
	}
	
	@Override
	@RequestMapping(value = "/version/{application}/{name:.*}", method = RequestMethod.DELETE)
	public @ResponseBody Ack versionDelete(@PathVariable int application, @PathVariable String name) {
		return getMyConfigService().deleteVersion (application, name);
	}
	
	@Override
	@RequestMapping(value = "/environment/{application}/{name:.*}", method = RequestMethod.PUT)
	public @ResponseBody Ack environmentCreate(@PathVariable int application, @PathVariable String name) {
		return getMyConfigService().createEnvironment (application, name);
	}
	
	@Override
	@RequestMapping(value = "/environment/{application}/{name}", method = RequestMethod.DELETE)
	public @ResponseBody Ack environmentDelete(@PathVariable int application, @PathVariable String name) {
		return getMyConfigService().deleteEnvironment (application, name);
	}
	
	@Override
	@RequestMapping(value = "/key/{application}/{name}/create", method = RequestMethod.POST)
	public @ResponseBody Ack keyCreate(@PathVariable int application, @PathVariable String name, @RequestParam String description) {
		return getMyConfigService().createKey (application, name, description);
	}
	
	@Override
	@RequestMapping(value = "/key/{application}/{name}/update", method = RequestMethod.POST)
	public @ResponseBody Ack keyUpdate(@PathVariable int application, @PathVariable String name, @RequestParam String description) {
		return getMyConfigService().updateKey (application, name, description);
	}
	
	@Override
	@RequestMapping(value = "/key/{id}/{name:.*}", method = RequestMethod.DELETE)
	public @ResponseBody Ack keyDelete(@PathVariable int id, @PathVariable String name) {
		return getMyConfigService().deleteKey (id, name);
	}
	
	@Override
	@RequestMapping(value = "/application/{id}/key_version", method = RequestMethod.GET)
	public @ResponseBody MatrixConfiguration keyVersionConfiguration(@PathVariable int id) {
		return getMyConfigService().keyVersionConfiguration(id);
	}

	@Override
	@RequestMapping(value = "/version/{application}/{version}/add/{key:.*}", method = RequestMethod.POST)
	public @ResponseBody Ack keyVersionAdd (@PathVariable int application, @PathVariable String version, @PathVariable String key) {
		return getMyConfigService().addKeyVersion (application, version, key);
	}

	@Override
	@RequestMapping(value = "/version/{application}/{version}/remove/{key:.*}", method = RequestMethod.POST)
	public @ResponseBody Ack keyVersionRemove (@PathVariable int application, @PathVariable String version, @PathVariable String key) {
		return getMyConfigService().removeKeyVersion (application, version, key);
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
	public @ResponseBody Ack appFunctionAdd(@PathVariable String user, @PathVariable int application, @PathVariable AppFunction fn) {
		return securityService.appFunctionAdd (application, user, fn);
	}
	
	@Override
	@RequestMapping(value = "/user/{user}/application/{application}/function/{fn}/remove", method = RequestMethod.POST)
	public @ResponseBody Ack appFunctionRemove(@PathVariable String user, @PathVariable int application, @PathVariable AppFunction fn) {
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

}
