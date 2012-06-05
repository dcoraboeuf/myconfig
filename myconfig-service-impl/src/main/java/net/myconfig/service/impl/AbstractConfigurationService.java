package net.myconfig.service.impl;

import net.myconfig.service.api.ConfigurationService;

public abstract class AbstractConfigurationService implements ConfigurationService {

	private final String dbDriver;
	private final String dbUrl;
	private final String dbUser;
	private final String dbPassword;
	private final int dbPoolInitial;
	private final int dbPoolMax;

	public AbstractConfigurationService(String dbDriver, String dbUrl, String dbUser, String dbPassword, int dbPoolInitial, int dbPoolMax) {
		this.dbDriver = dbDriver;
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.dbPoolInitial = dbPoolInitial;
		this.dbPoolMax = dbPoolMax;
	}

	@Override
	public String getDBDriver() {
		return dbDriver;
	}

	@Override
	public String getDBURL() {
		return dbUrl;
	}

	@Override
	public String getDBUser() {
		return dbUser;
	}

	@Override
	public String getDBPassword() {
		return dbPassword;
	}

	@Override
	public int getDBPoolInitial() {
		return dbPoolInitial;
	}

	@Override
	public int getDBPoolMax() {
		return dbPoolMax;
	}

}
