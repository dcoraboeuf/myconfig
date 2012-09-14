package net.myconfig.web.support.fm;

import java.util.List;

import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FnSettingsAppName implements TemplateMethodModel {

	private final ConfigurationService configurationService;

	@Autowired
	public FnSettingsAppName(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.size() == 0, "No argument is needed");
		// OK
		return configurationService.getParameter(ConfigurationKey.APP_NAME);
	}

}
