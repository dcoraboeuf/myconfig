package net.myconfig.web.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.web.renderer.HttpRenderer;
import net.myconfig.web.renderer.HttpRendererService;
import net.myconfig.web.renderer.RendererNotFoundException;
import net.myconfig.web.support.ErrorHandler;
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

	private final HttpRendererService httpRendererService;

	@Autowired
	public GetController(Strings strings, ErrorHandler errorHandler, MyConfigService myConfigService, HttpRendererService httpRendererService) {
		super (strings, errorHandler, myConfigService);
		this.httpRendererService = httpRendererService;
	}

	// FIXME Configuration description for app x version

	@RequestMapping("/key/{application}/{environment}/{version}/{key:.*}")
	public @ResponseBody
	String key(@PathVariable String application, @PathVariable String version, @PathVariable String environment, @PathVariable String key) {
		return getMyConfigService().getKey(application, version, environment, key);
	}

	@RequestMapping("/env/{application}/{environment}/{version}/{mode}")
	public void env_default (@PathVariable String application, @PathVariable String version, @PathVariable String environment,
			@PathVariable String mode,
			HttpServletResponse response) throws IOException {
		// No variant
		env (application, version, environment, mode, null, response);
	}

	@RequestMapping("/env/{application}/{environment}/{version}/{mode}/{variant}")
	public void env (@PathVariable String application, @PathVariable String version, @PathVariable String environment,
			@PathVariable String mode, @PathVariable String variant,
			HttpServletResponse response) throws IOException {
		// Gets the configuration
		ConfigurationSet set = getMyConfigService().getEnv (application, version, environment);
		// Gets the renderer
		HttpRenderer<ConfigurationSet> renderer = getConfigurationSetRenderer(mode);
		// Renders the configuration into the response
		renderer.renderer (set, variant, response);
	}

	private HttpRenderer<ConfigurationSet> getConfigurationSetRenderer(String mode) {
		try {
			return httpRendererService.getRenderer (ConfigurationSet.class, mode);
		} catch (RendererNotFoundException ex) {
			throw new ConfigurationModeNotFoundException (mode);
		}
	}

}
