package net.myconfig.client.java.support;

import java.util.List;

import net.myconfig.client.java.MyConfigClient;
import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.UserSummary;
import net.myconfig.core.model.VersionConfiguration;

public class MyConfigDefaultClient implements MyConfigClient {

	private final String url;

	public MyConfigDefaultClient(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public List<ApplicationSummary> applications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationSummary applicationCreate(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack applicationDelete(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationConfiguration applicationConfiguration(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack versionCreate(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack versionDelete(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack environmentCreate(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack environmentDelete(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyDelete(int id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyCreate(int id, String name, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyUpdate(int id, String name, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MatrixConfiguration keyVersionConfiguration(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyVersionAdd(int application, String version, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack keyVersionRemove(int application, String version, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionConfiguration versionConfiguration(int application, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnvironmentConfiguration environmentConfiguration(int application, String environment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KeyConfiguration keyConfiguration(int application, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack updateConfiguration(int application, ConfigurationUpdates updates) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserSummary> users() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack userCreate(String name, String displayName, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack userDelete(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack userFunctionAdd(String name, UserFunction fn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack userFunctionRemove(String name, UserFunction fn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack appFunctionAdd(String user, int application, AppFunction fn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ack appFunctionRemove(String user, int application, AppFunction fn) {
		// TODO Auto-generated method stub
		return null;
	}

}
