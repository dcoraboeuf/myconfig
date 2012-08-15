package net.myconfig.service.model;

public final class Ack {

	public static final Ack OK = new Ack(true);
	public static final Ack NOK = new Ack(false);

	public static Ack validate(boolean test) {
		return test ? OK : NOK;
	}

	public static Ack one(int count) {
		return validate(count == 1);
	}

	private final boolean success;

	private Ack(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public Ack and(Ack ack) {
		return validate(isSuccess() && ack.isSuccess());
	}

	public Ack or(Ack ack) {
		return validate(isSuccess() || ack.isSuccess());
	}

}
