package net.myconfig.service.type;

import net.myconfig.core.type.ValueIO;

import org.apache.commons.lang3.ObjectUtils;

public class PlainValueIO implements ValueIO<Object> {
	
	public static final PlainValueIO INSTANCE = new PlainValueIO();
	
	@Override
	public Object toValue(String s, String param) {
		return s;
	}

	@Override
	public String toString(Object o, String param) {
		return ObjectUtils.toString(o, null);
	}
	
}