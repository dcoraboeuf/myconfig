package net.myconfig.acc.page;

import java.io.File;
import java.io.IOException;

import net.myconfig.acc.support.AccContext;

import org.apache.commons.io.FileUtils;

public class DefaultPageContext implements PageContext {

	private static final String ROOT_PATH = "target/acceptance";

	private final String name;
	private final AccContext context;

	public DefaultPageContext(String name, AccContext context) {
		this.name = name;
		this.context = context;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public AccContext context() {
		return context;
	}

	@Override
	public void copyFile(File file, String type, String targetFileName) {
		try {
			File dir = getDirectory(type);
			FileUtils.copyFile(file, new File(dir, targetFileName));
		} catch (IOException ex) {
			// Just a warning
			ex.printStackTrace();
		}
	}

	protected File getDirectory(String type) throws IOException {
		File root = new File(ROOT_PATH);
		File dir = new File(root, type);
		FileUtils.forceMkdir(dir);
		return dir;
	}

}
