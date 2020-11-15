package com.kiegame.mobile.repository.entity.submit;

import androidx.annotation.NonNull;

/**
 * Created by: var.
 * Created date: 2020/4/14.
 * Description: 统一提交对象
 */
public class Submit {

    private String paramAes;

    public String getParamAes() {
        return paramAes;
    }

    public void setParamAes(String paramAes) {
        this.paramAes = paramAes;
    }

    @NonNull
    @Override
    public String toString() {
        return "{\"paramAes\":\"" + paramAes + "\"}";
    }
}
