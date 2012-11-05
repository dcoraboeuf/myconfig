package net.myconfig.web.gui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public interface GUITestHelper {

	String generateId(String prefix);

	String generateName(String prefix);

	ModelAndView run(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView run(String method, String path, String paramName, Object paramValue) throws Exception;

	void assertErrorMessage(ModelAndView mav, String errorKey, String format, Object... parameters);

}
