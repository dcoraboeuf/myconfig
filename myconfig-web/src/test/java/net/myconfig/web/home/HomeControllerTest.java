package net.myconfig.web.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.myconfig.core.MyConfigProfiles;
import net.myconfig.web.rest.GetController;
import net.myconfig.web.rest.UIController;
import net.myconfig.web.test.AbstractConfigurationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

@ActiveProfiles(MyConfigProfiles.TEST)
public class HomeControllerTest extends AbstractConfigurationTest {
	
	@Autowired
	private HomeController homeController;
	
	@Autowired
	private GetController getController;
	
	@Autowired
	private UIController uiController;
	
	@Test
	public void getRESTInformationVersion() {
		RESTInformation info = homeController.getRESTInformation(getController);
		assertNotNull (info);
		RESTMethodInformation method = getMethod(info, "version");
		assertEquals ("rest.AbstractRESTController.version", method.getKey());
		assertEquals ("/get/version", method.getPath());
		assertEquals ("GET", method.getMethod());
	}
	
	@Test
	public void getRESTInformationKey() {
		RESTInformation info = homeController.getRESTInformation(getController);
		assertNotNull (info);
		RESTMethodInformation method = getMethod(info, "key");
		assertEquals ("rest.GetController.key", method.getKey());
		assertEquals ("/get/key/{key}/{application}/{version}/{environment}", method.getPath());
		assertEquals ("GET", method.getMethod());
	}
	
	@Test
	public void getRESTInformationUIApplicationCreate() {
		RESTInformation info = homeController.getRESTInformation(uiController);
		assertNotNull (info);
		RESTMethodInformation method = getMethod(info, "applicationCreate");
		assertEquals ("rest.UIController.applicationCreate", method.getKey());
		assertEquals ("/ui/application/{name}", method.getPath());
		assertEquals ("PUT", method.getMethod());
	}
	
	@Test
	public void getRESTInformationUIApplicationDelete() {
		RESTInformation info = homeController.getRESTInformation(uiController);
		assertNotNull (info);
		RESTMethodInformation method = getMethod(info, "applicationDelete");
		assertEquals ("rest.UIController.applicationDelete", method.getKey());
		assertEquals ("/ui/application/{id}", method.getPath());
		assertEquals ("DELETE", method.getMethod());
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
