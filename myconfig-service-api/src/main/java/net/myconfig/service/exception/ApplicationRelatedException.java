package net.myconfig.service.exception;

public abstract class ApplicationRelatedException extends InputException {

	private final int id;

	public ApplicationRelatedException(int id, Object... params) {
		super(params);
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
