package com.kiegame.mobile.repository.entity.receive;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 商品
 */
public class ShopEntity implements MultiItemEntity, Serializable {

    /**
     * barCount : 2
     * productId : PD80803512393241918464
     * productFlavorName : 超级酸
     * productSpecName : 500M
     * productImg : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576154160009&di=d7f8312ccd27b4f365e3cbe794b171cd&imgtype=0&src=http%3A%2F%2Fqcloud.dpfile.com%2Fpc%2FuRwyg7aFjgcnOzcP4Vv2j_ltN6f1JddmzFkOJ0uUeSKpmzL61j2g1pCktPiS-HyFTK-l1dfmC-sNXFHV2eRvcw.jpg
     * costPrice : 200
     * productStock : 2
     * sellPrice : 200
     * sort : 1
     * serviceName : 奥克斯广场店铺3
     * productName : 自制测试商品04
     * productTypeId : TY80803492536815680512
     * commissionLimit : 0
     * productDesc : 测试
     * masterCount : 21
     * productUnitName : 包
     * productTypeName : 香烟
     * totalStock : 23
     * productVariety : 2
     * productImgBig : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576154160009&di=d7f8312ccd27b4f365e3cbe794b171cd&imgtype=0&src=http%3A%2F%2Fqcloud.dpfile.com%2Fpc%2FuRwyg7aFjgcnOzcP4Vv2j_ltN6f1JddmzFkOJ0uUeSKpmzL61j2g1pCktPiS-HyFTK-l1dfmC-sNXFHV2eRvcw.jpg
     * serviceId : 80803499721711205376
     * serviceProductId : SP3530570632625152
     * barcode : 3512392317025280
     * commissionAmount : 0
     */

    private int barCount;
    private String productId;
    private String productFlavorName;
    private String productSpecName;
    private String productImg;
    private int costPrice;
    private int productStock;
    private int sellPrice;
    private int sort;
    private String serviceName;
    private String productName;
    private String productTypeId;
    private int commissionLimit;
    private String productDesc;
    private int masterCount;
    private String productUnitName;
    private String productTypeName;
    private int totalStock;
    private int productVariety;
    private String productImgBig;
    private String serviceId;
    private String serviceProductId;
    private String barcode;
    private int commissionAmount;

    public final static int CONTENT = 0;
    public final static int TITLE = 1;

    private int itemType;
    private int buySize;

    public ShopEntity() {

    }

    public ShopEntity(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getBarCount() {
        return barCount;
    }

    public void setBarCount(int barCount) {
        this.barCount = barCount;
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

    public int getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(int costPrice) {
        this.costPrice = costPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getCommissionLimit() {
        return commissionLimit;
    }

    public void setCommissionLimit(int commissionLimit) {
        this.commissionLimit = commissionLimit;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public int getMasterCount() {
        return masterCount;
    }

    public void setMasterCount(int masterCount) {
        this.masterCount = masterCount;
    }

    public String getProductUnitName() {
        return productUnitName;
    }

    public void setProductUnitName(String productUnitName) {
        this.productUnitName = productUnitName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }

    public int getProductVariety() {
        return productVariety;
    }

    public void setProductVariety(int productVariety) {
        this.productVariety = productVariety;
    }

    public String getProductImgBig() {
        return productImgBig;
    }

    public void setProductImgBig(String productImgBig) {
        this.productImgBig = productImgBig;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceProductId() {
        return serviceProductId;
    }

    public void setServiceProductId(String serviceProductId) {
        this.serviceProductId = serviceProductId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(int commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public int getBuySize() {
        return buySize;
    }

    public void setBuySize(int buySize) {
        this.buySize = buySize;
    }
}
