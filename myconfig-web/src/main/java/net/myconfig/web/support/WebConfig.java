package net.myconfig.web.support;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.netbeetle.jackson.ObjectMapperFactory;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
	
	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// JSON
		MappingJacksonHttpMessageConverter mapper = new MappingJacksonHttpMessageConverter();
		mapper.setObjectMapper(ObjectMapperFactory.createObjectMapper());
		converters.add(mapper);
		// Default converters
		addDefaultHttpMessageConverters(converters);
	}

}
