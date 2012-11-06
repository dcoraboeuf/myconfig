package net.myconfig.core.type;

public interface ValueIO<T> {
	
	String toString (T o, String param);
	
	T toValue (String s, String param);

}
