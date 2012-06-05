package net.myconfig.web.rest;

import net.myconfig.service.api.MyConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(method = RequestMethod.GET, value = "/get")
public class GetController {

	private final MyConfigService myConfigService;

	@Autowired
	public GetController(MyConfigService myConfigService) {
		this.myConfigService = myConfigService;
	}

	// FIXME Full configuration for app x version x env
	// FIXME Configuration description for app x version

	@RequestMapping("/key/{application}/{version}/{environment}/{key}")
	public @ResponseBody
	String key(@PathVariable String application, @PathVariable String version, @PathVariable String environment, @PathVariable String key) {
		return myConfigService.getKey(application, version, environment, key);
	}

}
