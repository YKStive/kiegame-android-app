package com.kiegame.mobile.repository.entity.submit;

import java.util.Objects;

/**
 * Created by: var_rain.
 * Created date: 2020/1/28.
 * Description: 购买商品
 */
public class BuyShop {

    // *产品ID
    private String productId;
    // *产品规格
    private String productSpecName;
    // *产品口味
    private String productFlavorName;
    // *产品数量
    private int productBuySum;
    // 产品优惠类型
    private Integer productDiscountType;
    // 产品优惠ID
    private String productDiscountId;
    // -费用/金额
    private int fee;
    // -商品图片
    private String shopImage;
    // -商品名称
    private String shopName;
    // -是否勾选购买
    private boolean isBuy;
    // -最大数量
    private int max;
    // -商品类型 1 固装 2 自制
    private int shopType;

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public BuyShop() {
        this.isBuy = true;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductSpecName() {
        return productSpecName;
    }

    public void setProductSpecName(String productSpecName) {
        this.productSpecName = productSpecName;
    }

    public String getProductFlavorName() {
        return productFlavorName;
    }

    public void setProductFlavorName(String productFlavorName) {
        this.productFlavorName = productFlavorName;
    }

    public int getProductBuySum() {
        return productBuySum;
    }

    public void setProductBuySum(int productBuySum) {
        this.productBuySum = productBuySum;
    }

    public Integer getProductDiscountType() {
        return productDiscountType;
    }

    public void setProductDiscountType(Integer productDiscountType) {
        this.productDiscountType = productDiscountType;
    }

    public String getProductDiscountId() {
        return productDiscountId;
    }

    public void setProductDiscountId(String productDiscountId) {
        this.productDiscountId = productDiscountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuyShop shop = (BuyShop) o;
        return fee == shop.fee &&
                Objects.equals(productId, shop.productId) &&
                Objects.equals(productSpecName, shop.productSpecName) &&
                Objects.equals(productFlavorName, shop.productFlavorName) &&
                Objects.equals(shopImage, shop.shopImage) &&
                Objects.equals(shopName, shop.shopName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productSpecName, productFlavorName, fee, shopImage, shopName);
    }
}
