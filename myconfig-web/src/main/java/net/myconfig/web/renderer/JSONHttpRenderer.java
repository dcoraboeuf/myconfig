package net.myconfig.web.renderer;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JSONHttpRenderer extends AbstractHttpRenderer<Object> {

	@Override
	public void renderer(Object o, HttpServletResponse response)
			throws IOException {
		// JSON as UTF-8 bytes
		ObjectMapper mapper = new ObjectMapper();
		byte[] bytes = mapper.writeValueAsString(o).getBytes("UTF-8");
		// Writes the response
		response.setContentType("application/json");
		response.setContentLength(bytes.length);
		response.setCharacterEncoding("UTf-8");
		ServletOutputStream output = response.getOutputStream();
		output.write(bytes);
		output.flush();
	}
	
	@Override
	public boolean appliesTo(Class<?> type) {
		return true;
	}
	
	@Override
	public String getContentType() {
		return "json";
	}

}
