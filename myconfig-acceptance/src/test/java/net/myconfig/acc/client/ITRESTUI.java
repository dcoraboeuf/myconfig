package net.myconfig.acc.client;

import static org.junit.Assert.assertEquals;
import net.myconfig.acc.support.AccUtils;

import org.junit.Test;

public class ITRESTUI extends AbstractClientUseCase {

	@Test
	public void version() {
		String actualVersion = AccUtils.CONTEXT.getClient().version();
		assertEquals(AccUtils.CONTEXT.getVersion(), actualVersion);
	}

}
