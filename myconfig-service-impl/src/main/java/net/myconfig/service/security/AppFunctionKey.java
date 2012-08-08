package net.myconfig.service.security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.myconfig.core.AppFunction;

public final class AppFunctionKey {

	private final int application;
	private final AppFunction fn;

	public AppFunctionKey(int application, AppFunction fn) {
		this.application = application;
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
		return String.format("%d/%s", application, fn);
	}

}
