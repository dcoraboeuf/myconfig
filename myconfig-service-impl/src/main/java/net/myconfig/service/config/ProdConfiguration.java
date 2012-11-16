package net.myconfig.service.config;

import javax.mail.Session;

import net.myconfig.core.MyConfigProfiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Profile(MyConfigProfiles.PROD)
public class ProdConfiguration {
	
	@Autowired
	private Session session;
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setSession(session);
		return mailSender;
	}

}
