package net.myconfig.web.support.fm;

import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

import net.myconfig.web.language.CurrentLocale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FnEllipsis implements TemplateMethodModel {
	
	public static final String ELLIPSIS = "...";

	public static String ellipsis(Locale locale, String value, int maxlength) {
		Validate.notNull(locale, "Locale must be specified");
		if (value == null) {
			return null;
		} else if (value.length() <= maxlength) {
			return value;
		} else {
			BreakIterator wordBreaker = BreakIterator.getWordInstance(locale);
			wordBreaker.setText(value);
			int end;
			int okEnd = 0;
			while ((end = wordBreaker.next()) != BreakIterator.DONE) {
				if (end <= maxlength) {
					okEnd = end;
				} else {
					break;
				}
			}
			if (okEnd > 0) {
				return StringUtils.substring(value, 0, okEnd - 1) + ELLIPSIS;
			} else {
				return StringUtils.substring(value, 0, maxlength - 1) + ELLIPSIS;
			}
		}
	}

	private final CurrentLocale currentLocale;

	@Autowired
	public FnEllipsis(CurrentLocale currentLocale) {
		this.currentLocale = currentLocale;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.size() == 2, "List of arguments must be equal to 2");
		// Data
		String value = (String) list.get(0);
		int maxlength = Integer.parseInt((String) list.get(1), 10);
		// Gets the locale from the context
		Locale locale = currentLocale.getCurrentLocale();
		// Computation
		return ellipsis(locale, value, maxlength);
	}

}
