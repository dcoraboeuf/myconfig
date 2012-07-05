package net.myconfig.service.model;

public class ConditionalValue {

	private final boolean enabled;
	private final String value;

	public ConditionalValue(boolean enabled, String value) {
		this.enabled = enabled;
		this.value = value;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getValue() {
		return value;
	}

}
