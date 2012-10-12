package net.myconfig.web.renderer;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;

import freemarker.template.Configuration;
import net.myconfig.core.model.ConfigurationSet;

@Component
public class ConfigurationSetXMLHttpRenderer extends AbstractXMLTemplateHttpRenderer<ConfigurationSet> {

	public static final String VARIANT_ATTRIBUTES_ONLY = "attributesOnly";
	public static final String VARIANT_TAGS_ONLY = "tagsOnly";
	public static final String VARIANT_MIXED = "mixed";

	public static final Set<String> ALL_VARIANTS = ImmutableSet.copyOf(Arrays.asList(VARIANT_ATTRIBUTES_ONLY, VARIANT_TAGS_ONLY, VARIANT_MIXED));

	@Autowired
	public ConfigurationSetXMLHttpRenderer(@Value("#{freemarkerConfig.configuration}") Configuration configuration) {
		super(configuration);
	}

	@Override
	public boolean appliesTo(Class<?> type) {
		return ConfigurationSet.class.isAssignableFrom(type);
	}

	@Override
	protected String getTemplateName(ConfigurationSet o, String variant) {
		if (StringUtils.isBlank(variant)) {
			return String.format("env-%s.xml", VARIANT_ATTRIBUTES_ONLY);
		} else if (ALL_VARIANTS.contains(variant)) {
			return String.format("env-%s.xml", variant);
		} else {
			throw new ConfigurationSetXMLUnknownVariantException(variant);
		}
	}

}
