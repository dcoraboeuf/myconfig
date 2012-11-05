package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import net.myconfig.core.model.Key;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.MatrixVersionConfiguration;
import net.myconfig.web.rest.UIInterface;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

public class GUIMatrixPageTest {
	
	private static final String APP = "APP";
	private GUIMatrixPage page;
	private UIInterface ui;
	
	@Before
	public void before() {
		ui = mock(UIInterface.class);
		page = new GUIMatrixPage(ui, null);
	}
	
	@Test
	public void pagePath() {
		assertEquals ("matrix/APP", page.pagePath(APP));
	}
	
	@Test
	public void page() {
		MatrixConfiguration configuration = new MatrixConfiguration(
				APP, "myapp",
				Collections.<MatrixVersionConfiguration>emptyList(),
				Collections.<Key>emptyList());
		when(ui.keyVersionConfiguration(APP)).thenReturn(
				configuration);
		ExtendedModelMap model = new ExtendedModelMap();
		String view = page.page(APP, model);
		assertEquals ("matrix", view);
		assertSame (configuration, model.get("configuration"));
	}

}
