package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var.
 * Created date: 2020/4/14.
 * Description: 查询订单
 */
public class ListBuyOrder {

    // *门店ID
    private String serviceId;
    // 支付状态 1待支付 2支付成功 3支付中 4支付失败
    private Integer payTypeState;
    // 开始时间 (yyyy-MM-dd HH:mm:ss)
    private String startTime;
    // 结束时间
    private String endTime;
    // *固定 2
    private Integer payChannel;
    // 支付类型
    private Integer payType;
    // 机位号
    private String seatNumber;
    // 会员名
    private String customerName;
    // *当前登录员工ID
    private String empId;
    // 分页
    private int page;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getPayTypeState() {
        return payTypeState;
    }

    public void setPayTypeState(Integer payTypeState) {
        this.payTypeState = payTypeState;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
