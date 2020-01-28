package com.kiegame.mobile.repository.entity.submit;

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
    private int productDiscountType;
    // 产品优惠ID
    private int productDiscountId;

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

    public int getProductDiscountType() {
        return productDiscountType;
    }

    public void setProductDiscountType(int productDiscountType) {
        this.productDiscountType = productDiscountType;
    }

    public int getProductDiscountId() {
        return productDiscountId;
    }

    public void setProductDiscountId(int productDiscountId) {
        this.productDiscountId = productDiscountId;
    }
}
