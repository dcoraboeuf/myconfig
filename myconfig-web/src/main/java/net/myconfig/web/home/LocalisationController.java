package net.myconfig.web.home;

import static org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript;
import static org.apache.commons.lang3.StringUtils.replace;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LocalisationController {

    private final Strings strings;

    @Autowired
    public LocalisationController(Strings strings) {
        this.strings = strings;
    }

    @RequestMapping(value = "/localization")
    public void localisation(Locale locale, HttpServletResponse response) throws IOException {
        // Locale
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        // Gets the list of key/values
        Map<String, String> map = strings.getKeyValues(locale);
        // Output
        StringBuilder js = new StringBuilder();
        js.append("// ").append(new Date()).append("\n");
        js.append("var l = {\n");
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (i > 0) {
                js.append(",\n");
            }
            js.append(String.format("'%s': '%s'",
                    key,
                    escape(value)
            		));
            i++;
        }
        js.append("\n};\n");
        // Returns the response as JS
        byte[] bytes = js.toString().getBytes("UTF-8");
        response.setContentType("text/javascript");
        response.setContentLength(bytes.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        // TODO Cache management
    }

	protected String escape(String value) {
		return escapeEcmaScript(
					replace(value, "''", "'")
				);
	}

}
