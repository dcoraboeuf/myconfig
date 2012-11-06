package net.myconfig.service.type;

import net.myconfig.core.type.ValueIO;
import net.myconfig.core.type.ValueType;

public abstract class AbstractValueType<T> implements ValueType<T> {

	private final String id;
	private final ValueIO<T> storageIO;
	private final ValueIO<T> presentationIO;

	public AbstractValueType(String id, ValueIO<T> storageIO, ValueIO<T> presentationIO) {
		this.id = id;
		this.storageIO = storageIO;
		this.presentationIO = presentationIO;
	}

	@Override
	public ValueIO<T> getPresentationIO() {
		return presentationIO;
	}

	@Override
	public ValueIO<T> getStorageIO() {
		return storageIO;
	}

	@Override
	public String getId() {
		return id;
	}

}
