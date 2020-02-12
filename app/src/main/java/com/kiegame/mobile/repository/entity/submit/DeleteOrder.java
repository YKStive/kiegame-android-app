package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var_rain.
 * Created date: 2020/2/12.
 * Description: 删除订单
 */
public class DeleteOrder {

    // *订单ID (orderBaseId)
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
