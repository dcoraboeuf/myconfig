package net.myconfig.web.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.web.renderer.HttpRenderer;
import net.myconfig.web.renderer.HttpRendererService;
import net.myconfig.web.renderer.RendererNotFoundException;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(method = RequestMethod.GET, value = "/get")
public class GetController extends AbstractRESTController {

	private final MyConfigService myConfigService;
	private final HttpRendererService httpRendererService;

	@Autowired
	public GetController(Strings strings, MyConfigService myConfigService, HttpRendererService httpRendererService) {
		super (strings);
		this.myConfigService = myConfigService;
		this.httpRendererService = httpRendererService;
	}
	
	@RequestMapping("/version")
	public @ResponseBody
	String version () {
		return myConfigService.getVersion();
	}

	// FIXME Configuration description for app x version

	@RequestMapping("/key/{key}/{application}/{version}/{environment}")
	public @ResponseBody
	String key(@PathVariable String application, @PathVariable String version, @PathVariable String environment, @PathVariable String key) {
		return myConfigService.getKey(application, version, environment, key);
	}

	@RequestMapping("/env/{application}/{version}/{environment}/{mode}")
	public void env (@PathVariable String application, @PathVariable String version, @PathVariable String environment,
			@PathVariable String mode,
			HttpServletResponse response) throws IOException {
		// Gets the configuration
		ConfigurationSet set = myConfigService.getEnv (application, version, environment);
		// Gets the renderer
		HttpRenderer<ConfigurationSet> renderer = getConfigurationSetRenderer(mode);
		// Renders the configuration into the response
		renderer.renderer (set, response);
	}

	private HttpRenderer<ConfigurationSet> getConfigurationSetRenderer(String mode) {
		try {
			return httpRendererService.getRenderer (ConfigurationSet.class, mode);
		} catch (RendererNotFoundException ex) {
			throw new ConfigurationModeNotFoundException (mode);
		}
	}

}
