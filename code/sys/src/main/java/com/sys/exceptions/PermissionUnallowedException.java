package com.sys.exceptions;

/**
 * 权限不允许
 * @author fourer
 *
 */
public class PermissionUnallowedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6993646484345971689L;

	public PermissionUnallowedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PermissionUnallowedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PermissionUnallowedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
