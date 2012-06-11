package net.myconfig.web.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.web.rest.GetController;
import net.myconfig.web.test.AbstractConfigurationTest;

@ActiveProfiles(MyConfigProfiles.TEST)
public class HomeControllerTest extends AbstractConfigurationTest {
	
	@Autowired
	private HomeController homeController;
	
	@Autowired
	private GetController getController;
	
	@Test
	public void getRESTInformationVersion() {
		RESTInformation info = homeController.getRESTInformation(getController);
		assertNotNull (info);
		RESTMethodInformation method = getMethod(info, "version");
		assertEquals ("rest.AbstractRESTController.version", method.getKey());
		assertEquals ("/get/version", method.getPath());
	}
	
	@Test
	public void getRESTInformationKey() {
		RESTInformation info = homeController.getRESTInformation(getController);
		assertNotNull (info);
		RESTMethodInformation method = getMethod(info, "key");
		assertEquals ("rest.GetController.key", method.getKey());
		assertEquals ("/get/key/{key}/{application}/{version}/{environment}", method.getPath());
	}

	protected RESTMethodInformation getMethod(RESTInformation info, final String methodName) {
		ImmutableList<RESTMethodInformation> methods = info.getMethods();
		RESTMethodInformation method = Iterables.find(methods, new Predicate<RESTMethodInformation>() {
			@Override
			public boolean apply(RESTMethodInformation mi) {
				return methodName.equals(mi.getName());
			}
		});
		return method;
	}

}
