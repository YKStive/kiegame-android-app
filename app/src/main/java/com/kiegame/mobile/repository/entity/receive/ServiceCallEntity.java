package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 呼叫服务列表
 */
public class ServiceCallEntity {

    //1：转接；2：已完成
    private int state;
    // TODO: 2020/11/10 需要自己补充字段


    public ServiceCallEntity() {
    }

    public ServiceCallEntity(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
