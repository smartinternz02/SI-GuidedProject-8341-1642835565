package com.viruchith.springexpensetracker.jwt;

public class IncorrectUsernameOrPasswordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2208189476758226865L;

	public IncorrectUsernameOrPasswordException() {
		super("Incorrect Username or Password !");
		// TODO Auto-generated constructor stub
	}

	public IncorrectUsernameOrPasswordException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
