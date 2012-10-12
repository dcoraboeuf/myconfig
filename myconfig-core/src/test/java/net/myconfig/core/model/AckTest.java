package net.myconfig.core.model;

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
	
	@Test
	public void validate_true() {
		assertTrue(Ack.validate(true).isSuccess());
	}
	
	@Test
	public void validate_false() {
		assertFalse(Ack.validate(false).isSuccess());
	}
	
	@Test
	public void one_0() {
		assertFalse(Ack.one(0).isSuccess());
	}
	
	@Test
	public void one_1() {
		assertTrue(Ack.one(1).isSuccess());
	}
	
	@Test
	public void one_more() {
		assertFalse(Ack.one(2).isSuccess());
	}

}
