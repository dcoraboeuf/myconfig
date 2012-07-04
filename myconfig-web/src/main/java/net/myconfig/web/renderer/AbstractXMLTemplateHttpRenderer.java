package net.myconfig.web.renderer;

import freemarker.template.Configuration;

public abstract class AbstractXMLTemplateHttpRenderer<T> extends AbstractTemplateHttpRenderer<T> {
	
	public AbstractXMLTemplateHttpRenderer(Configuration configuration) {
		super(configuration, ENCODING, "text/xml");
	}

	@Override
	public String getContentType() {
		return "xml";
	}

}
