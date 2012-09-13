package net.myconfig.service.template;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import net.myconfig.service.api.template.TemplateModel;
import net.myconfig.service.api.template.TemplateService;
import net.myconfig.test.AbstractIntegrationTest;
import net.myconfig.test.Helper;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TemplateServiceTest extends AbstractIntegrationTest {

	@Autowired
	private TemplateService service;

	@Test(expected = TemplateNotFoundException.class)
	public void template_not_found() {
		TemplateModel model = new TemplateModel();
		service.generate("unknown.txt", model);
	}

	@Test
	public void user_new() throws IOException {
		TemplateModel model = new TemplateModel().add("user", "testuser").add("userFullName", "John Smith").add("link", "http://mylink");
		String content = service.generate("user_new.txt", model);
		String expected = Helper.getResourceAsString("/templates/user_new.txt");
		assertEquals(expected, content);
	}

}
