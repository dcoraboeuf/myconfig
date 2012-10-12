package net.myconfig.acc.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ITRESTUI extends AbstractClientUseCase {

	@Test
	public void version() {
		String actualVersion = client.version();
		assertEquals(version, actualVersion);
	}

}
