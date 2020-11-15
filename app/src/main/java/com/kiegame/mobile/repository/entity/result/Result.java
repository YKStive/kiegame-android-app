package com.kiegame.mobile.repository.entity.result;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: 返回数据格式
 */
public class Result<T> {

    // 状态码
    private int code;
    // 操作信息
    private String message;
    // 数据
    private T data;
    // 数据总数
    private int total;
    // 长度
    private int length;
    // 是否成功
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
