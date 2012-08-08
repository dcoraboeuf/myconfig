package net.myconfig.service.security;

import net.myconfig.core.EnvFunction;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class EnvFunctionKey {

	private final int application;
	private final String environment;
	private final EnvFunction fn;

	public EnvFunctionKey(int application, String environment, EnvFunction fn) {
		this.application = application;
		this.environment = environment;
		this.fn = fn;
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return String.format("%d/%s/%s", application, environment, fn);
	}

}
