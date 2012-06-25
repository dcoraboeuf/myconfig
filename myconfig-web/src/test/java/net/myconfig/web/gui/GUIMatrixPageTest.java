package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import net.myconfig.service.model.Key;
import net.myconfig.service.model.KeyVersionConfiguration;
import net.myconfig.service.model.VersionConfiguration;
import net.myconfig.web.rest.UIInterface;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

public class GUIMatrixPageTest {
	
	private GUIMatrixPage page;
	private UIInterface ui;
	
	@Before
	public void before() {
		ui = mock(UIInterface.class);
		page = new GUIMatrixPage(ui, null);
	}
	
	@Test
	public void pagePath() {
		assertEquals ("matrix/12", page.pagePath(12));
	}
	
	@Test
	public void page() {
		KeyVersionConfiguration configuration = new KeyVersionConfiguration(
				10, "myapp",
				Collections.<VersionConfiguration>emptyList(),
				Collections.<Key>emptyList());
		when(ui.keyVersionConfiguration(10)).thenReturn(
				configuration);
		ExtendedModelMap model = new ExtendedModelMap();
		String view = page.page(10, model);
		assertEquals ("matrix", view);
		assertSame (configuration, model.get("configuration"));
	}

}
