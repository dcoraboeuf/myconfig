package net.myconfig.service.model;

public class Ack {

	public static final Ack OK = new Ack(true);
	public static final Ack NOK = new Ack(false); 

	public static Ack validate(boolean test) {
		return test ? OK : NOK;
	}

	private final boolean success;

	private Ack(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

}
