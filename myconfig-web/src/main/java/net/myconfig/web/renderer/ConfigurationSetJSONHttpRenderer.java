package net.myconfig.web.renderer;

import static com.google.common.collect.Maps.uniqueIndex;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.service.model.ConfigurationValue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

@Component
public class ConfigurationSetJSONHttpRenderer extends AbstractJSONHttpRenderer<ConfigurationSet> {

	public static final String VARIANT_COMPLETE = "complete";
	public static final String VARIANT_CONCISE = "concise";

	@Override
	public void renderer(ConfigurationSet set, String variant, HttpServletResponse response)
			throws IOException {
		// Gets the object to render
		Object o;
		if (StringUtils.isBlank(variant) || VARIANT_COMPLETE.equals(variant)) {
			o = set.getValues();
		} else if (VARIANT_CONCISE.equals(variant)) {
			// Map
			o = Maps.transformValues(
					uniqueIndex(set.getValues(), new Function<ConfigurationValue, String>() {
						@Override
						public String apply(ConfigurationValue configurationValue) {
							return configurationValue.getKey();
						}
					}),
					new Function<ConfigurationValue, String>() {
						@Override
						public String apply(ConfigurationValue configurationValue) {
							return configurationValue.getValue();
						}
					});
		} else {
			throw new ConfigurationSetJSONUnknownVariantException (variant);
		}
		// Renders the JSON
		render (o, response);
	}
	
	@Override
	public boolean appliesTo(Class<?> type) {
		return ConfigurationSet.class.isAssignableFrom(type);
	}

}
