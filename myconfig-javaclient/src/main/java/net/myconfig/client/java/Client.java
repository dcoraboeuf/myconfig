package net.myconfig.client.java;

public interface Client<C extends Client<C>> {
	
	void logout();
	
	C withLogin (String name, String password);

}
