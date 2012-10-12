package net.myconfig.core.model;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonCreator;

import lombok.Data;

@Data
public final class Ack {

	public static final Ack OK = new Ack(true);
	public static final Ack NOK = new Ack(false);

	public static Ack validate(boolean test) {
		return test ? OK : NOK;
	}

	public static Ack one(int count) {
		return validate(count == 1);
	}
	
	@JsonCreator
	public static Ack fromJson (JsonNode node) {
		return Ack.validate(node.get("success").getBooleanValue());
	}

	private final boolean success;

	public Ack and(Ack ack) {
		return validate(isSuccess() && ack.isSuccess());
	}

	public Ack or(Ack ack) {
		return validate(isSuccess() || ack.isSuccess());
	}

}
