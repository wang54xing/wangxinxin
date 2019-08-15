package com.sys.exceptions;

/**
 * 查询结果过多异常，一般是由于参数没有做检查引起
 * @author fourer
 *
 */
public class ResultTooManyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5583988686637591074L;

	
	public ResultTooManyException() {
		super();
	}

	public ResultTooManyException(String arg0) {
		super(arg0);
	}
	
}
