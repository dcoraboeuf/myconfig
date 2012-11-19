package net.myconfig.service.api;

import java.util.Collection;

import net.myconfig.core.model.Event;
import net.myconfig.core.model.EventFilter;
import net.myconfig.core.model.EventRecord;

public interface EventService {
	
	void saveEvent (Event event);

	Collection<EventRecord> filter(EventFilter eventFilter);

	int clean(int retentionDays);

	void clearAll();

}
