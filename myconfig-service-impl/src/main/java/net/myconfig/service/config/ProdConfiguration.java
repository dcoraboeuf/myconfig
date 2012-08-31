package net.myconfig.service.config;

import javax.mail.Session;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.InitialisationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Profile(MyConfigProfiles.PROD)
public class ProdConfiguration extends CommonConfiguration {
	
	@Autowired
	private Session session;

	@Override
	@Bean
	public InitialisationService initialisationService() throws Exception {
		// No logging file
		return new DefaultInitialisationService(MyConfigProfiles.PROD);
	}
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setSession(session);
		return mailSender;
	}

}
