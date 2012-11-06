package net.myconfig.core.type;

public interface ValueType<T> {
	
	String getId();
	
	ValueIO<T> getStorageIO();
	
	ValueIO<T> getPresentationIO();

}
