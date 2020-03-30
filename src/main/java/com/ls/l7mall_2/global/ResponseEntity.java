package com.ls.l7mall_2.global;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * @author laijs
 * @date 2020-3-28-11:52
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseEntity<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    // 只响应状态码
    private ResponseEntity(int status) {
        this.status = status;
    }

    //响应状态码和提示信息
    private ResponseEntity(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    //响应状态码和数据
    private ResponseEntity(int status, T data) {
        this.status = status;
        this.data = data;
    }

    //响应状态码、提示信息和数据
    private ResponseEntity(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    // 响应成功时，有以下情况
    // * 只响应状态码
    public static <T> ResponseEntity<T> responesWhenSuccess(){
        return new ResponseEntity<T>(ResponseCode.SUCCESS.getCode());
    }
    // * 响应状态码和提示信息
    public static <T> ResponseEntity<T> responesWhenSuccess(String msg){
        return new ResponseEntity<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    // * 响应状态码和数据
    public static <T> ResponseEntity<T> responesWhenSuccess(T data){
        return new ResponseEntity<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    // * 响应状态码、提示信息和数据
    public static <T> ResponseEntity<T> responesWhenSuccess(String msg,T data){
        return new ResponseEntity<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    // 响应失败时，有以下情况
    // * 普通错误：响应状态码和状态描述
    public static <T> ResponseEntity<T> responesWhenError(){
        return new ResponseEntity<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDecs());
    }
    // * 响应状态码和提示信息
    public static <T> ResponseEntity<T> responesWhenError(String msg){
        return new ResponseEntity<T>(ResponseCode.ERROR.getCode(),msg);
    }
    // * 响应其他状态码和提示信息
    public static <T> ResponseEntity<T> responesWhenError(int code,String msg){
        return new ResponseEntity<T>(code,msg);
    }

    // 提供一个判断是否响应成功的方法,该方法不参与json序列化
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }
}
