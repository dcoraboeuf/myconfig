package net.myconfig.service.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.myconfig.core.model.Ack;

import org.junit.Test;

public class AckTest {
	
	@Test
	public void ok() {
		assertTrue (Ack.OK.isSuccess());
	}
	
	@Test
	public void nok() {
		assertFalse (Ack.NOK.isSuccess());
	}

}
