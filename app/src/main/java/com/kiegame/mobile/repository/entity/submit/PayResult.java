package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var.
 * Created date: 2020/4/14.
 * Description: 支付返回
 */
public class PayResult {

    // 支付返回的单号,在线支付时需要
    private String paymentPayId;
    // 退款时需要
    private String orderBaseId;

    public String getPaymentPayId() {
        return paymentPayId;
    }

    public void setPaymentPayId(String paymentPayId) {
        this.paymentPayId = paymentPayId;
    }

    public String getOrderBaseId() {
        return orderBaseId;
    }

    public void setOrderBaseId(String orderBaseId) {
        this.orderBaseId = orderBaseId;
    }
}
