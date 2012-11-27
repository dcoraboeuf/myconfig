package net.myconfig.core;

import java.util.Locale;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ApplicationUsers;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.EnvironmentUsers;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.UserSummaries;
import net.myconfig.core.model.VersionConfiguration;
import net.myconfig.core.type.ConfigurationValidationInput;
import net.myconfig.core.type.ConfigurationValidationOutput;
import net.myconfig.core.type.ValueTypeDescriptions;

public interface MyConfigInterface {
	
	String version();

	ApplicationSummaries applications();

	ApplicationSummary applicationCreate(String id, String name);

	Ack applicationDelete(String id);

	ApplicationConfiguration applicationConfiguration(String id);

	Ack versionCreate(String id, String name);

	Ack versionDelete(String id, String name);

	Ack environmentCreate(String id, String name);

	Ack environmentDelete(String id, String name);

	Ack environmentUp(String id, String environment);

	Ack environmentDown(String id, String environment);

	Ack keyDelete(String id, String name);

	Ack keyCreate(String id, String name, String description, String typeId, String typeParam);

	Ack keyUpdate(String id, String name, String description);

	MatrixConfiguration keyVersionConfiguration(String id);

	Ack keyVersionAdd(String application, String version, String key);

	Ack keyVersionRemove(String application, String version, String key);

	VersionConfiguration versionConfiguration(String application, String version);

	EnvironmentConfiguration environmentConfiguration(String application, String environment);

	KeyConfiguration keyConfiguration(String application, String key);

	Ack updateConfiguration(String application, ConfigurationUpdates updates);

	UserSummaries users();

	Ack userCreate(String mode, String name, String displayName, String email);

	Ack userDelete(String name);

	Ack userFunctionAdd(String name, UserFunction fn);

	Ack userFunctionRemove(String name, UserFunction fn);

	Ack appFunctionAdd(String user, String application, AppFunction fn);

	Ack appFunctionRemove(String user, String application, AppFunction fn);

	Ack setSecurityMode(String mode);

	Ack userConfirm(String name, String token, String password);
	
	ApplicationUsers applicationUsers(String application);

	EnvironmentUsers environmentUsers(String application, String environment);

	Ack envFunctionAdd(String user, String application, String environment, EnvFunction fn);

	Ack envFunctionRemove(String user, String application, String environment, EnvFunction fn);
	
	ValueTypeDescriptions types ();
	
	String typeParameterValidate (Locale locale, String typeId, String parameter);
	
	String typeValueValidate (Locale locale, String typeId, String parameter, String value);
	
	ConfigurationValidationOutput configurationValidate (Locale locale, String application, ConfigurationValidationInput input);

}
