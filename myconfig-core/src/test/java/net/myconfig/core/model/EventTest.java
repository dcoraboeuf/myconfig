package net.myconfig.core.model;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

public class EventTest {

	@Test
	public void value_length() {
		assertEquals(Event.LENGTH_OLDVALUE, Event.LENGTH_NEWVALUE);
	}

	@Test
	public void prepare_in_boundaries() {
		Event e = new Event(1, "sec", "user", new DateTime(), "cat", "id", "old", "new", "message");
		Event ep = e.prepareForRecording();
		assertEquals(e, ep);
	}

	@Test
	public void prepare_truncated() {
		Event e = new Event(1, repeat("security",5), repeat("user",25), new DateTime(), repeat("category", 20), repeat("identifier",9), repeat("oldvalue",30), repeat("newvalue",30), repeat("messages", 80));
		Event ep = e.prepareForRecording();
		Event ea = new Event(1, "securityse", repeat("user",20), e.getCreation(), repeat("category", 10), repeat("identifier",8), repeat("oldvalue",25), repeat("newvalue",25), repeat("messages", 75));
		assertEquals(ea, ep);
	}

}
