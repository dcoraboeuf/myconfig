package net.myconfig.web.support;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

	@RequestMapping("/error/{error:\\d+}")
	public String error(@PathVariable int error, Model model) {
		model.addAttribute("errorCode", error);
		return "error_code";
	}

}
