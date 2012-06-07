package net.myconfig.web.renderer;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

@Component
public class DefaultHttpRendererService implements HttpRendererService {
	
	private final List<HttpRenderer<?>> renderers;
	
	@Autowired
	public DefaultHttpRendererService(Collection<HttpRenderer<?>> renderers) {
		this.renderers = ImmutableList.copyOf(renderers);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> HttpRenderer<T> getRenderer(Class<T> type, String contentType) {
		for (HttpRenderer<?> renderer : renderers) {
			if (contentType.equals(renderer.getContentType()) && renderer.appliesTo (type)) {
				return (HttpRenderer<T>) renderer;
			}
		}
		throw new RendererNotFoundException (type, contentType);
	}

}
