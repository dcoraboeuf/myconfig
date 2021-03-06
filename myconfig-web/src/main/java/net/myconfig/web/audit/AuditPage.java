package net.myconfig.web.audit;

import java.util.Arrays;
import java.util.Collection;

import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.core.model.EventFilter;
import net.myconfig.core.model.EventRecord;
import net.myconfig.service.api.EventService;
import net.myconfig.web.gui.AbstractGUIPage;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/audit")
public class AuditPage extends AbstractGUIPage {
	
	private final EventService eventService;

	@Autowired
	public AuditPage(UIInterface ui, ErrorHandler errorHandler, EventService eventService) {
		super(ui, errorHandler);
		this.eventService = eventService;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String audit(Model model) {
		// Default filter
		filter (model, new EventFilter());
		// OK
		return "audit";
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String onFilter(Model model, EventFilter filter) {
		// Filtering
		filter (model, filter);
		// OK
		return "audit";
	}

	protected void filter(Model model, EventFilter eventFilter) {
		// Filter
		model.addAttribute("filter", eventFilter);
		// Records
		Collection<EventRecord> records = eventService.filter (eventFilter);
		model.addAttribute("records", records);
		// Static data
		model.addAttribute("categories", Arrays.asList(EventCategory.values()));
		model.addAttribute("actions", Arrays.asList(EventAction.values()));
	}

}
