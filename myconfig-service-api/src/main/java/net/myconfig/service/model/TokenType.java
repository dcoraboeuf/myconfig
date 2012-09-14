package net.myconfig.service.model;

public enum TokenType {
	/**
	 * The user has just been created and he must enter his new password.
	 */
	NEW_USER,
	/**
	 * An administrator has selected a user to be reset. The user will be given
	 * access to a form that allows him to enter a new password.
	 */
	RESET_USER,
	/**
	 * A connected user has asked to change his password. He will be given
	 * access to a form that allows him to change his password as long as he can
	 * provide the old one.
	 */
	USER_CHANGE_PASSWORD,
	/**
	 * The user cannot connect because he has forgotten his password. he will
	 * have to enter his email. He will then receive a mail giving him access to
	 * a form that allows him to change his password.
	 */
	FORGOTTEN_PASSWORD;
}