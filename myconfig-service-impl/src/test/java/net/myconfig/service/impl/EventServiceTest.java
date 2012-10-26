package net.myconfig.service.impl;

import java.sql.SQLException;

import net.myconfig.core.model.Event;
import net.myconfig.service.api.EventService;
import net.myconfig.test.AbstractIntegrationTest;

import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EventServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private EventService eventService;
	
	@Test
	public void save() throws DataSetException, SQLException {
		assertRecordNotExists("select id from events where category = '%s'", "save");
		Event e = new Event("save", "id", "old", "new", "message");
		eventService.saveEvent(e);
		assertRecordCount(1, "select id from events where category = '%s'", "save");
	}

//	@Test
//	public void value_length() {
//		assertEquals(EventRecord.LENGTH_OLDVALUE, EventRecord.LENGTH_NEWVALUE);
//	}
//
//	@Test
//	public void prepare_in_boundaries() {
//		EventRecord e = new EventRecord(1, "sec", "user", new DateTime(), "cat", "id", "old", "new", "message");
//		EventRecord ep = e.prepareForRecording();
//		assertEquals(e, ep);
//	}
//
//	@Test
//	public void prepare_truncated() {
//		EventRecord e = new EventRecord(1, repeat("security",5), repeat("user",25), new DateTime(), repeat("category", 20), repeat("identifier",9), repeat("oldvalue",30), repeat("newvalue",30), repeat("messages", 80));
//		EventRecord ep = e.prepareForRecording();
//		EventRecord ea = new EventRecord(1, "securityse", repeat("user",20), e.getCreation(), repeat("category", 10), repeat("identifier",8), repeat("oldvalue",25), repeat("newvalue",25), repeat("messages", 75));
//		assertEquals(ea, ep);
//	}

}
