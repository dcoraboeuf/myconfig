package net.myconfig.service.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MatrixConfigurationTest {
	
	@Test
	public void enabled_no_version () {
		MatrixConfiguration c = createTestConfiguration();
		assertFalse(c.isEnabled("1.x", "a"));
	}
	
	@Test
	public void enabled_no_key () {
		MatrixConfiguration c = createTestConfiguration();
		assertFalse(c.isEnabled("1.1", "c"));
	}
	
	@Test
	public void enabled_ok () {
		MatrixConfiguration c = createTestConfiguration();
		assertTrue(c.isEnabled("1.1", "a"));
	}

	protected MatrixConfiguration createTestConfiguration() {
		return new MatrixConfiguration(1, "myapp",
				Arrays.asList(
						new MatrixVersionConfiguration(
								"1.0",
								Arrays.asList("a", "b")),
						new MatrixVersionConfiguration(
								"1.1",
								Arrays.asList("a", "b")),
						new MatrixVersionConfiguration(
								"1.2",
								Arrays.asList("a", "b", "c"))
				),
				Arrays.asList(
						new Key("a", "aaa"),
						new Key("b", "bbb"),
						new Key("c", "ccc")
				)
		);
	}

}
