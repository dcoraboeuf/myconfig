package net.myconfig.core.type;

import net.sf.jstring.Localizable;

public interface ValueType {
	
	String PLAIN = "plain";
	
	String getId();
	
	boolean acceptParameter();
	
	Localizable validateParameter (String param);
	
	Localizable validate (String value, String param);

}
