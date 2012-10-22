package net.myconfig.acc.support;

import net.myconfig.client.java.MyConfigClient;
import net.sf.jstring.Strings;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractUseCase {
	
	private AccContext context;
	
	@Before
	public void initContext() {
		context = AccUtils.createContext();
	}
	
	@After
	public void destroyContext() {
		context.close();
	}
	
	protected AccContext context() {
		return context;
	}

	protected MyConfigClient client() {
		return context.getClient();
	}
	
	protected Strings strings() {
		return context.getStrings();
	}

}
