package net.myconfig.core.type;

import net.myconfig.core.InputException;

public interface ValueType {
	
	String PLAIN = "plain";
	
	String getId();
	
	InputException validate (String value, String param);

}
