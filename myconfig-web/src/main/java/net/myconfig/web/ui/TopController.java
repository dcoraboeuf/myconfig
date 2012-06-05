package net.myconfig.web.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TopController {

	@RequestMapping("/")
	public String home() {
		// FIXME Loads the home content
		// OK
		return "home";
	}

}
