package net.myconfig.service.impl;

import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.service.audit.Audit;
import net.myconfig.service.audit.AuditedInterface;

public class AuditedImpl implements AuditedInterface {

	@Override
	public void noAudit() {
	}

	@Override
	@Audit(category = EventCategory.CONFIGURATION, action = EventAction.UPDATE, identifier = "#id")
	public void identifierOnly(int id) {
	}

}
