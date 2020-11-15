package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var.
 * Created date: 2020/4/14.
 * Description:用户信息数据提交
 */
public class UserInfo {

    // 机位号/身份证后4位/姓名
    private String param;
    // 门店ID
    private String serviceId;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
