package net.myconfig.web.support.fm;

import java.util.List;
import java.util.Locale;

import net.myconfig.web.language.CurrentLocale;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

// FIXME Re-using code in FnLoc
public class FnLocLink implements TemplateMethodModel {
	
	private final Strings strings;
	private final CurrentLocale currentLocale;
	
	@Autowired
	public FnLocLink(Strings strings, CurrentLocale currentLocale) {
		this.strings = strings;
		this.currentLocale = currentLocale;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.size() == 2, "List of arguments must be equal to 2 (key & link)");
		// Gets the key
		String key = (String) list.get(0);
		// Gets the link
		String link = (String) list.get(1);
		// Replacement
		String replacement = String.format ("%s$1</a>", link);
		// Gets the locale from the context
		Locale locale = currentLocale.getCurrentLocale();
		// Gets the complete message
		String message = strings.get(locale, key);
		// Replacement using REGEXP
		String completeMessage = message.replaceAll("%(.+)%", replacement);
		// OK
		return completeMessage;
	}

}
