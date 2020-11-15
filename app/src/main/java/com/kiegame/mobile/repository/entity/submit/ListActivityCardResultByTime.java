package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var.
 * Created date: 2020/4/14.
 * Description: 查询用户优惠券
 */
public class ListActivityCardResultByTime {

    // *会员ID
    private String customerId;
    // 门店ID
    private String serviceId;
    // 产品ID
    private String productId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
