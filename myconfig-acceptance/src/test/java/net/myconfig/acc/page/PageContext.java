package net.myconfig.acc.page;

import java.io.File;

public interface PageContext {

	String getName();

	void copyFile(File file, String type, String targetFileName);

}
