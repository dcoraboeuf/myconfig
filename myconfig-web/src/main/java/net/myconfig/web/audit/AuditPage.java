package net.myconfig.web.audit;

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

	@Autowired
	public AuditPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String audit(Model model) {
		// OK
		return "audit";
	}

}
