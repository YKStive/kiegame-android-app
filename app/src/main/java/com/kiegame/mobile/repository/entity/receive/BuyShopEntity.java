package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description:
 */
public class BuyShopEntity {

    /**
     * productId : PD80803512393241918464
     * productFlavorName : 加糖
     * orderId : CP3525262500133888
     * productSpecName : 500ML
     * productImg : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576154160009&di=d7f8312ccd27b4f365e3cbe794b171cd&imgtype=0&src=http%3A%2F%2Fqcloud.dpfile.com%2Fpc%2FuRwyg7aFjgcnOzcP4Vv2j_ltN6f1JddmzFkOJ0uUeSKpmzL61j2g1pCktPiS-HyFTK-l1dfmC-sNXFHV2eRvcw.jpg
     * discountAmount : 0
     * sellPrice : 200
     * productName : 自制测试商品04
     * orderDetailId : CPD3525262500133890
     * productUnitName : 包
     * sellAmount : 400
     * sellCount : 2
     * needPayAmount : 400
     * productImgBig : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576154160009&di=d7f8312ccd27b4f365e3cbe794b171cd&imgtype=0&src=http%3A%2F%2Fqcloud.dpfile.com%2Fpc%2FuRwyg7aFjgcnOzcP4Vv2j_ltN6f1JddmzFkOJ0uUeSKpmzL61j2g1pCktPiS-HyFTK-l1dfmC-sNXFHV2eRvcw.jpg
     */

    private String productId;
    private String productFlavorName;
    private String orderId;
    private String productSpecName;
    private String productImg;
    private Integer discountAmount;
    private String sellPrice;
    private String productName;
    private String orderDetailId;
    private String productUnitName;
    private int sellAmount;
    private String sellCount;
    private int needPayAmount;
    private String productImgBig;
    private String productDesc;

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductFlavorName() {
        return productFlavorName;
    }

    public void setProductFlavorName(String productFlavorName) {
        this.productFlavorName = productFlavorName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductSpecName() {
        return productSpecName;
    }

    public void setProductSpecName(String productSpecName) {
        this.productSpecName = productSpecName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getProductUnitName() {
        return productUnitName;
    }

    public void setProductUnitName(String productUnitName) {
        this.productUnitName = productUnitName;
    }

    public int getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(int sellAmount) {
        this.sellAmount = sellAmount;
    }

    public String getSellCount() {
        return sellCount;
    }

    public void setSellCount(String sellCount) {
        this.sellCount = sellCount;
    }

    public int getNeedPayAmount() {
        return needPayAmount;
    }

    public void setNeedPayAmount(int needPayAmount) {
        this.needPayAmount = needPayAmount;
    }

    public String getProductImgBig() {
        return productImgBig;
    }

    public void setProductImgBig(String productImgBig) {
        this.productImgBig = productImgBig;
    }
}
