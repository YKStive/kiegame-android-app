package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var.
 * Created date: 2020/4/14.
 * Description: 查询商品
 */
public class QueryShops {

    // 门店ID
    private String serviceId;
    // 分类ID
    private String productTypeId;
    // 名称
    private String productName;
    // 标签ID
    private String productTagId;
    // *是否移动端 (固定 1)
    private Integer isMobile;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTagId() {
        return productTagId;
    }

    public void setProductTagId(String productTagId) {
        this.productTagId = productTagId;
    }

    public Integer getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(Integer isMobile) {
        this.isMobile = isMobile;
    }
}
