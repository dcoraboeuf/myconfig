package net.myconfig.web.renderer;

import net.myconfig.service.exception.CoreException;

public class RendererNotFoundException extends CoreException {

	public RendererNotFoundException(Class<?> type, String key) {
		super (type, key);
	}

}
