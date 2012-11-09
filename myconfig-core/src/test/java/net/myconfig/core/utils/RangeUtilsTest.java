package net.myconfig.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.common.collect.Range;

public class RangeUtilsTest {
	
	@Test
	public void all() {
		parse(null, "(-∞‥+∞)");
		parse(" ", "(-∞‥+∞)");
		parse("..", "(-∞‥+∞)");
	}
	
	@Test
	public void at_most() {
		parse("10", "(-∞‥10]");
		parse("..10", "(-∞‥10]");
		parse("..10]", "(-∞‥10]");
		parse("..10)", "(-∞‥10]");
	}
	
	@Test
	public void less_than() {
		parse("..10[", "(-∞‥10)");
		parse("..10(", "(-∞‥10)");
	}
	
	@Test
	public void at_least() {
		parse("10..", "[10‥+∞)");
		parse("[10..", "[10‥+∞)");
		parse("(10..", "[10‥+∞)");
	}
	
	@Test
	public void greater_than() {
		parse(")10..", "(10‥+∞)");
		parse("]10..", "(10‥+∞)");
	}
	
	@Test
	public void open_closed() {
		parse(")10..20", "(10‥20]");
		parse("]10..20", "(10‥20]");
		parse(")10..20)", "(10‥20]");
		parse("]10..20)", "(10‥20]");
		parse(")10..20]", "(10‥20]");
		parse("]10..20]", "(10‥20]");
	}
	
	@Test
	public void closed_open() {
		parse("10..20(", "[10‥20)");
		parse("10..20[", "[10‥20)");
		parse("(10..20(", "[10‥20)");
		parse("(10..20[", "[10‥20)");
		parse("[10..20(", "[10‥20)");
		parse("[10..20[", "[10‥20)");
	}
	
	@Test
	public void closed() {
		parse("10..20", "[10‥20]");
		parse("10..20)", "[10‥20]");
		parse("10..20]", "[10‥20]");
		parse("10..20", "[10‥20]");
		parse("(10..20)", "[10‥20]");
		parse("[10..20]", "[10‥20]");
	}
	
	@Test
	public void open() {
		parse(")10..20(", "(10‥20)");
		parse(")10..20[", "(10‥20)");
		parse("]10..20(", "(10‥20)");
		parse("]10..20[", "(10‥20)");
	}

	protected void parse(String value, String ex) {
		Range<Integer> range = RangeUtils.parse(value, RangeUtils.INTEGER);
		assertNotNull(range);
		assertEquals(ex, range.toString());
	}

}
