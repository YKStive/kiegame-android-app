package com.kiegame.mobile.repository.entity.submit;


/**
 * Description: 请求呼叫服务列表
 * @author yx
 */
public class CallServiceRequest {
    private String serviceId;
    private int page ;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
