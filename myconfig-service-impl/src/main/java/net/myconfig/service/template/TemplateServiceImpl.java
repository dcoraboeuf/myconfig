package net.myconfig.service.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import net.myconfig.service.api.template.TemplateModel;
import net.myconfig.service.api.template.TemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class TemplateServiceImpl implements TemplateService {

	private final Configuration configuration;

	@Autowired
	public TemplateServiceImpl(@Qualifier("templating") Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public String generate(String templateId, TemplateModel templateModel) {
		// Creates the model as a map
		Map<String, Object> root = templateModel.toMap();
		// Gets the template
		Template template;
		try {
			template = configuration.getTemplate(templateId);
		} catch (IOException ex) {
			throw new TemplateNotFoundException (templateId, ex);
		}
		// Merging w/ the model
		StringWriter writer = new StringWriter();
		try {
			template.process(root, writer);
		} catch (TemplateException ex) {
			throw new TemplateMergeException (templateId, ex);
		} catch (IOException ex) {
			// JDK7: same code for two exceptions
			throw new TemplateMergeException (templateId, ex);
		}
		// OK
		return writer.toString();
	}

}
