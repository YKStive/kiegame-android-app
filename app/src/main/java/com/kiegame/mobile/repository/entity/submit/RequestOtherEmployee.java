package com.kiegame.mobile.repository.entity.submit;

public class RequestOtherEmployee {

    /**
     * excludeEmpId :
     * serviceId :
     */
    private String excludeEmpId;
    private String serviceId;

    public void setExcludeEmpId(String excludeEmpId) {
        this.excludeEmpId = excludeEmpId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getExcludeEmpId() {
        return excludeEmpId;
    }

    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String toString() {
        return "RequestOtherEmployee{" +
                "excludeEmpId='" + excludeEmpId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }
}
