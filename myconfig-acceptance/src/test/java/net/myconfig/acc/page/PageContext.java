package net.myconfig.acc.page;

import java.io.File;

import net.myconfig.acc.support.AccContext;

public interface PageContext {

	String getName();
	
	AccContext context();

	void copyFile(File file, String type, String targetFileName);

}
