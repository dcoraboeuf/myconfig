package net.myconfig.web.test;

import org.apache.commons.lang3.StringUtils;

import net.myconfig.service.model.ApplicationSummary;

import com.google.common.base.Predicate;

public class ApplicationSummaryNamePredicate implements Predicate<ApplicationSummary> {

	private final String name;

	public ApplicationSummaryNamePredicate(String name) {
		this.name = name;
	}

	@Override
	public boolean apply(ApplicationSummary o) {
		return StringUtils.equals(name, o.getName());
	}

}
