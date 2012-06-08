package net.myconfig.web.home;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.myconfig.web.rest.GetController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	private final GetController getController;
	
	
	@Autowired
	public HomeController(GetController getController) {
		this.getController = getController;
	}

	@RequestMapping("/")
	public String home(Model model) {
		// Loads information about the REST GET API
		RESTInformation getInformation = getRESTInformation (getController);
		model.addAttribute("getInformation", getInformation);
		// TODO Loads information about the REST UI API
		// OK
		return "home";
	}

	protected RESTInformation getRESTInformation(Object controller) {
		// Gets all public methods
		List<RESTMethodInformation> restMethods = new ArrayList<RESTMethodInformation>();
		Method[] methods = controller.getClass().getMethods();
		for (Method method : methods) {
			RESTMethodInformation restMethod = getRESTMethodInformation (method);
			if (restMethod != null) {
				restMethods.add(restMethod);
			}
		}
		// OK
		return new RESTInformation(restMethods);
	}

	protected RESTMethodInformation getRESTMethodInformation(Method method) {
		RequestMapping mapping = method.getAnnotation(RequestMapping.class);
		if (mapping != null) {
			String path = mapping.value()[0];
			String methodName = method.getName();
			String controllerName = method.getDeclaringClass().getSimpleName();
			String methodKey = String.format("rest.%s.%s", controllerName, methodName);
			return new RESTMethodInformation(methodKey, path);
		} else {
			return null;
		}
	}

}
