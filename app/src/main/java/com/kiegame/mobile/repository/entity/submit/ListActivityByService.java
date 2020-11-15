package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var.
 * Created date: 2020/4/14.
 * Description: 查询门店活动
 */
public class ListActivityByService {

    // *门店ID
    private String serviceId;
    // 产品ID
    private String productId;

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
