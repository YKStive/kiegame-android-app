package com.kiegame.mobile.repository.entity.submit;


/**
 * Description: 请求呼叫服务列表
 * @author yx
 */
public class CallServiceRequest {
    private String serviceId;
    private String localDate;
    private int page=1;
    private int size = 50;

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

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

    @Override
    public String toString() {
        return "CallServiceRequest{" +
                "serviceId='" + serviceId + '\'' +
                ", localDate='" + localDate + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
