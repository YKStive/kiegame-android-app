package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/2/13.
 * Description: 在线支付结果返回
 */
public class PayResultEntity {

    // 支付单号
    private String paymentPayId;
    // 支付状态 2 支付成功 3 支付中 4 支付失败
    private int payState;
    // 订单单号
    private String orderBaseId;
    // 退款状态 3 退款中 4 退款成功 5 退款失败
    private int returnState;

    public String getOrderBaseId() {
        return orderBaseId;
    }

    public void setOrderBaseId(String orderBaseId) {
        this.orderBaseId = orderBaseId;
    }

    public int getReturnState() {
        return returnState;
    }

    public void setReturnState(int returnState) {
        this.returnState = returnState;
    }

    public String getPaymentPayId() {
        return paymentPayId;
    }

    public void setPaymentPayId(String paymentPayId) {
        this.paymentPayId = paymentPayId;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }
}
