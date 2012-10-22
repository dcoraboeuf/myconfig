package net.myconfig.web.security;

import net.myconfig.core.model.Ack;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.web.rest.AbstractRESTController;
import net.myconfig.web.support.ErrorHandler;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ui/security")
public class SecurityUIController extends AbstractRESTController implements SecurityUIInterface {

	private final SecurityService securityService;
	
	@Autowired
	public SecurityUIController(Strings strings, ErrorHandler errorHandler, MyConfigService myConfigService, SecurityService securityService) {
		super(strings, errorHandler, myConfigService);
		this.securityService = securityService;
	}

	@Override
	@RequestMapping(value = "/mode/{mode}", method = RequestMethod.POST)
	public @ResponseBody Ack setSecurityMode(@PathVariable String mode) {
		securityService.setSecurityMode(mode);
		return Ack.OK;
	}

}
