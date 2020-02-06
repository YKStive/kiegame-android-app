package com.kiegame.mobile.repository.entity.submit;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 充值下订单
 */
public class AddOrder {

    // *门店ID
    private String serviceId;
    // 产品订单ID或充值ID
    private String productOrderList;
    // 充值订单ID或产品ID
    private String rechargeOrderList;
    // 座位号
    private String seatNumber;
    // *会员ID
    private String customerId;
    // *产品列表
    private List<BuyShop> productList;
    // *充值金额
    private int rechargeMoney;
    // *充值奖励金额 没有为0
    private int rechargeRewardMoney;
    // 充值优惠类型
    private Integer rechargeDiscountType;
    // 充值优惠ID
    private String rechargeDiscountId;
    // 充值优惠金额
    private Integer rechargeDiscountMoney;
    // *是否下订单 下单1 结算2
    private int isAddOrder;
    // *支付类型 现金2 卡扣3 客维4 扫码支付5
    private int buyPayType;
    // 支付密码
    private String buyPayPassword;
    // *是否位左面 固定2
    private int isSurfacePos;
    // *固定2
    private int buyPayChannel;
    // *当前登录员工ID
    private String commissionId;
    // *实付金额
    private String paidInAmount;
    // 备注
    private String memo;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(int rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public int getRechargeRewardMoney() {
        return rechargeRewardMoney;
    }

    public void setRechargeRewardMoney(int rechargeRewardMoney) {
        this.rechargeRewardMoney = rechargeRewardMoney;
    }

    public Integer getRechargeDiscountType() {
        return rechargeDiscountType;
    }

    public void setRechargeDiscountType(Integer rechargeDiscountType) {
        this.rechargeDiscountType = rechargeDiscountType;
    }

    public String getRechargeDiscountId() {
        return rechargeDiscountId;
    }

    public void setRechargeDiscountId(String rechargeDiscountId) {
        this.rechargeDiscountId = rechargeDiscountId;
    }

    public Integer getRechargeDiscountMoney() {
        return rechargeDiscountMoney;
    }

    public void setRechargeDiscountMoney(Integer rechargeDiscountMoney) {
        this.rechargeDiscountMoney = rechargeDiscountMoney;
    }

    public int getIsAddOrder() {
        return isAddOrder;
    }

    public void setIsAddOrder(int isAddOrder) {
        this.isAddOrder = isAddOrder;
    }

    public int getIsSurfacePos() {
        return isSurfacePos;
    }

    public void setIsSurfacePos(int isSurfacePos) {
        this.isSurfacePos = isSurfacePos;
    }

    public int getBuyPayChannel() {
        return buyPayChannel;
    }

    public void setBuyPayChannel(int buyPayChannel) {
        this.buyPayChannel = buyPayChannel;
    }

    public String getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(String commissionId) {
        this.commissionId = commissionId;
    }

    public String getPaidInAmount() {
        return paidInAmount;
    }

    public void setPaidInAmount(String paidInAmount) {
        this.paidInAmount = paidInAmount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getBuyPayType() {
        return buyPayType;
    }

    public void setBuyPayType(int buyPayType) {
        this.buyPayType = buyPayType;
    }

    public String getBuyPayPassword() {
        return buyPayPassword;
    }

    public void setBuyPayPassword(String buyPayPassword) {
        this.buyPayPassword = buyPayPassword;
    }

    public List<BuyShop> getProductList() {
        return productList;
    }

    public void setProductList(List<BuyShop> productList) {
        this.productList = productList;
    }

    public String getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(String productOrderList) {
        this.productOrderList = productOrderList;
    }

    public String getRechargeOrderList() {
        return rechargeOrderList;
    }

    public void setRechargeOrderList(String rechargeOrderList) {
        this.rechargeOrderList = rechargeOrderList;
    }
}
