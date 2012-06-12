package net.myconfig.web.renderer;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

public abstract class AbstractJSONHttpRenderer<T> extends AbstractHttpRenderer<T> {

	protected void render(Object o, HttpServletResponse response) throws IOException {
		// JSON as UTF-8 bytes
		ObjectMapper mapper = new ObjectMapper();
		byte[] bytes = mapper.writeValueAsString(o).getBytes("UTF-8");
		// Writes the response
		response.setContentType("application/json");
		response.setContentLength(bytes.length);
		response.setCharacterEncoding("UTF-8");
		ServletOutputStream output = response.getOutputStream();
		output.write(bytes);
		output.flush();
	}
	
	@Override
	public String getContentType() {
		return "json";
	}

}
