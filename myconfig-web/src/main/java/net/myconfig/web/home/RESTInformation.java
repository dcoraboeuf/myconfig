package net.myconfig.web.home;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class RESTInformation {

	private final ImmutableList<RESTMethodInformation> methods;

	public RESTInformation(List<RESTMethodInformation> restMethods) {
		this.methods = ImmutableList.copyOf(restMethods);
	}

	public ImmutableList<RESTMethodInformation> getMethods() {
		return methods;
	}

}
