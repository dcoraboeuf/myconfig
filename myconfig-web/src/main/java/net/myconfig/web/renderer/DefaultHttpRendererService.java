package net.myconfig.web.renderer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultHttpRendererService implements HttpRendererService {
	
	private final Map<String, HttpRenderer<?>> renderers;
	
	@Autowired
	public DefaultHttpRendererService(Map<String, HttpRenderer<?>> renderers) {
		this.renderers = renderers;
	}

	@Override
	public <T> HttpRenderer<T> getRenderer(Class<T> type, String key) {
		@SuppressWarnings("unchecked")
		HttpRenderer<T> renderer = (HttpRenderer<T>) renderers.get(key);
		if (renderer != null) {
			return renderer;
		} else {
			throw new RendererNotFoundException (type, key);
		}
	}

}
