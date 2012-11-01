package net.myconfig.service.impl;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.service.audit.Audit;
import net.myconfig.service.audit.AuditedInterface;
import net.myconfig.service.audit.CollectionItems;
import net.myconfig.service.exception.ApplicationNotFoundException;

public class AuditedImpl implements AuditedInterface {

	@Override
	public void noAudit() {
	}

	@Override
	@Audit(category = EventCategory.CONFIGURATION, action = EventAction.UPDATE, identifier = "#id")
	public void identifierOnly(int id) {
	}

	@Override
	@Audit(category = EventCategory.CONFIGURATION, action = EventAction.UPDATE, identifier = "#id", message = "'[' + #id + '] ' + #message")
	public void identifierAndMessage(int id, String message) {
	}

	@Override
	@Audit(category = EventCategory.CONFIGURATION, action = EventAction.UPDATE, identifier = "#id", result = "#result.success")
	public Ack identifierAndResult(int id) {
		return Ack.validate(id % 2 == 0);
	}

	@Override
	@Audit(category = EventCategory.CONFIGURATION, action = EventAction.UPDATE, application = "#application", environment = "#environment", version = "#version", key = "#key")
	public void allKeys(int application, String environment, String version, String key) {
	}
	
	@Override
	@Audit(category = EventCategory.CONFIGURATION, action = EventAction.UPDATE, application = "#id")
	public void withException(int id) {
		throw new ApplicationNotFoundException(id);
	}
	
	@Override
	@Audit(category = EventCategory.CONFIGURATION, action = EventAction.UPDATE, collection = "#items.versions", application = "#application", version = "#item.name", result = "#result.success")
	public Ack withCollection(int application, CollectionItems items) {
		return Ack.OK;
	}
	
	@Override
	@Audit(category = EventCategory.CONFIGURATION, action = EventAction.UPDATE, application = "#id")
	public void expressionMismatch(int application) {
	}

}
