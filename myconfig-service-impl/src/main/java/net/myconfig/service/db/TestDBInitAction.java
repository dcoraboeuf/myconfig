package net.myconfig.service.db;

import net.myconfig.core.MyConfigProfiles;
import net.sf.dbinit.DBInitScriptAction;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Qualifier("dbPostAction")
@Profile(MyConfigProfiles.IT)
public class TestDBInitAction extends DBInitScriptAction {
	
	public TestDBInitAction() {
		super (System.getProperty("it.db"));
	}

}
