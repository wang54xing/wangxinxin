package com.sys.utils;

import java.io.Serializable;

import com.sys.constants.Constant;

public class SalaryRespObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 处理成功
	 */
	public  static final SalaryRespObj SUCCESS=new SalaryRespObj(Constant.SUCCESS_CODE);
	/**
	 * 处理失败
	 */
	public  static final SalaryRespObj FAILD=new SalaryRespObj(Constant.FAILD_CODE);
	
	public String code;
	public int total;
	public Object message;
	
	public SalaryRespObj(String code) {
		super();
		this.code = code;
	}
	
	public SalaryRespObj(String code, Object message) {
		super();
		this.code = code;
		this.message = message;
		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SalaryRespObj [code=" + code + ", total=" + total
				+ ", message=" + message + "]";
	}

	
}
