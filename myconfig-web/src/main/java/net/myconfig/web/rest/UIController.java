package net.myconfig.web.rest;

import java.util.List;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.EnvironmentConfiguration;
import net.myconfig.service.model.MatrixConfiguration;
import net.myconfig.service.model.VersionConfiguration;
import net.myconfig.service.model.ConfigurationUpdates;
import net.myconfig.web.support.ErrorHandler;
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

	@Autowired
	public UIController(Strings strings, ErrorHandler errorHandler, MyConfigService myConfigService) {
		super(strings, errorHandler, myConfigService);
	}

	@Override
	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	public @ResponseBody List<ApplicationSummary> applications() {
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
	@RequestMapping(value = "/key/{application}/{name}", method = RequestMethod.PUT)
	public @ResponseBody Ack keyCreate(@PathVariable int application, @PathVariable String name, @RequestParam String description) {
		return getMyConfigService().createKey (application, name, description);
	}
	
	@Override
	@RequestMapping(value = "/key/{id}/{name}", method = RequestMethod.DELETE)
	public @ResponseBody Ack keyDelete(@PathVariable int id, @PathVariable String name) {
		return getMyConfigService().deleteKey (id, name);
	}
	
	@Override
	@RequestMapping(value = "/application/{id}/key_version", method = RequestMethod.DELETE)
	public MatrixConfiguration keyVersionConfiguration(@PathVariable int id) {
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

}
