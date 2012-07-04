package net.myconfig.web.renderer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class AbstractTemplateHttpRenderer<T> extends AbstractHttpRenderer<T> {

	public static final String ENCODING = "UTF-8";

	private final Configuration configuration;
	private final String templateEncoding;
	private final String responseContentType;

	public AbstractTemplateHttpRenderer(Configuration configuration, String templateEncoding, String responseContentType) {
		this.configuration = configuration;
		this.templateEncoding = templateEncoding;
		this.responseContentType = responseContentType;
	}

	@Override
	public void renderer(T o, String variant, HttpServletResponse response) throws IOException {
		// Template name
		String templateName = getTemplateName (o, variant);
		// Template
		Template template = configuration.getTemplate(templateName, templateEncoding);
		if (template == null) {
			throw new TemplateNotFoundException (templateName);
		}
		// Model
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", o);
		// Content type
		response.setCharacterEncoding(ENCODING);
		response.setContentType(responseContentType);
		// Rendering
		OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), ENCODING);
		try {
			template.process(model, writer);
		} catch (TemplateException e) {
			throw new IOException("Cannot render template", e);
		} finally {
			writer.flush();
		}
	}

	protected abstract String getTemplateName(T o, String variant);

}
