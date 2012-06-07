package net.myconfig.web.renderer;


public interface HttpRendererService {

	<T> HttpRenderer<T> getRenderer(Class<T> type, String contentType);

}
