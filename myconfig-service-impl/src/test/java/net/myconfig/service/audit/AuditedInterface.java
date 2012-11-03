package net.myconfig.service.audit;

import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;

public interface AuditedInterface {
	
	void noAudit();
	
	void identifierOnly(int id);
	
	void identifierAndMessage(int id, String message);
	
	Ack identifierAndResult (int id);
	
	void allKeys(String application, String environment, String version, String key);
	
	Ack withCollection (String application, CollectionItems items);
	
	void withException(String id);
	
	void expressionMismatch(String application);
	
	Ack userAndFunctionOnly (String user, UserFunction function);

}
