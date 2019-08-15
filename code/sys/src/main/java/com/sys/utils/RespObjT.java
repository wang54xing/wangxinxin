package com.sys.utils;

import java.io.Serializable;

import com.sys.constants.Constant;

/**
 * 泛型类型的RespObj
 * @author fourer
 *
 * @param <T>
 */
public class RespObjT<T> implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 60683785108752137L;

    /**
     * 处理成功
     */
    public static final RespObj SUCCESS = new RespObj(Constant.SUCCESS_CODE);
    /**
     * 处理失败
     */
    public static final RespObj FAILD = new RespObj(Constant.FAILD_CODE);


    public String code;
    public T message;


    public RespObjT(String code) {
        super();
        this.code = code;
    }


    public RespObjT(String code, T message) {
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

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }


  

}
