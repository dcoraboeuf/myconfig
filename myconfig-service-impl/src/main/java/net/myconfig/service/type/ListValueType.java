package net.myconfig.service.type;

import static org.apache.commons.lang3.StringUtils.trim;
import net.sf.jstring.Localizable;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ListValueType extends AbstractValueType {

	public ListValueType() {
		super("list");
	}

	@Override
	protected boolean doValidate(String value, String param) {
		if (StringUtils.isBlank(value) && StringUtils.isBlank(param)) {
			return true;
		} else {
			String item = trim(ObjectUtils.toString(value, ""));
			String[] items = StringUtils.split(param, ",");
			if (items != null) {
				for (String candidate : items) {
					candidate = trim(candidate);
					if (StringUtils.equals(candidate, item)) {
						return true;
					}
				}
			}
			return false;
		}
	}

	@Override
	public boolean acceptParameter() {
		return true;
	}

	@Override
	public Localizable validateParameter(String param) {
		return null;
	}

}
