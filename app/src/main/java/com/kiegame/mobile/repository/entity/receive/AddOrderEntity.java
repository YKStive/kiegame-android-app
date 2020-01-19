package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 充值下单
 */
public class AddOrderEntity {

    /**
     * productOrderDiscountMoney : 0
     * productOrderMoney : 1400
     * productOrderId : CO3530768878943233
     * rechargeOrderDiscountMoney : 0
     * rechargeOrderMoney : 3000
     * rechargeOrderRewardMoney : 1000
     * rechargeOrderId : CO3530768884677633
     * paymentPayId : PM3530936982879232
     * payState : 2
     */

    private int productOrderDiscountMoney;
    private int productOrderMoney;
    private String productOrderId;
    private int rechargeOrderDiscountMoney;
    private int rechargeOrderMoney;
    private int rechargeOrderRewardMoney;
    private String rechargeOrderId;
    private String paymentPayId;
    private int payState;

    public int getProductOrderDiscountMoney() {
        return productOrderDiscountMoney;
    }

    public void setProductOrderDiscountMoney(int productOrderDiscountMoney) {
        this.productOrderDiscountMoney = productOrderDiscountMoney;
    }

    public int getProductOrderMoney() {
        return productOrderMoney;
    }

    public void setProductOrderMoney(int productOrderMoney) {
        this.productOrderMoney = productOrderMoney;
    }

    public String getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(String productOrderId) {
        this.productOrderId = productOrderId;
    }

    public int getRechargeOrderDiscountMoney() {
        return rechargeOrderDiscountMoney;
    }

    public void setRechargeOrderDiscountMoney(int rechargeOrderDiscountMoney) {
        this.rechargeOrderDiscountMoney = rechargeOrderDiscountMoney;
    }

    public int getRechargeOrderMoney() {
        return rechargeOrderMoney;
    }

    public void setRechargeOrderMoney(int rechargeOrderMoney) {
        this.rechargeOrderMoney = rechargeOrderMoney;
    }

    public int getRechargeOrderRewardMoney() {
        return rechargeOrderRewardMoney;
    }

    public void setRechargeOrderRewardMoney(int rechargeOrderRewardMoney) {
        this.rechargeOrderRewardMoney = rechargeOrderRewardMoney;
    }

    public String getRechargeOrderId() {
        return rechargeOrderId;
    }

    public void setRechargeOrderId(String rechargeOrderId) {
        this.rechargeOrderId = rechargeOrderId;
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
