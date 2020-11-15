package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 呼叫服务列表
 */
public class ServiceCallEntity {

    /**
     * cancelOperationId :
     * finishTime :
     * transferTime :
     * cpId :
     * idCard :
     * customId :
     * customerName :
     * seatNumber :
     * timeout : true
     * specialTreatOperatorId :
     * realOperationId :
     * startTime :
     * state : 0 转接  1完成
     * serviceId :
     * timeLeft : 0
     * specialTreatTime :
     */
    private String cancelOperationId;
    private String finishTime;
    private String transferTime;
    private String cpId;
    private String idCard;
    private String customId;
    private String customerName;
    private String seatNumber;
    private boolean timeout;
    private String specialTreatOperatorId;
    private String realOperationId;
    private String startTime;
    private int state=1;
    private String serviceId;
    private int timeLeft = 120;
    private String specialTreatTime;

    public ServiceCallEntity(int i) {

    }

    public void setCancelOperationId(String cancelOperationId) {
        this.cancelOperationId = cancelOperationId;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public void setSpecialTreatOperatorId(String specialTreatOperatorId) {
        this.specialTreatOperatorId = specialTreatOperatorId;
    }

    public void setRealOperationId(String realOperationId) {
        this.realOperationId = realOperationId;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setSpecialTreatTime(String specialTreatTime) {
        this.specialTreatTime = specialTreatTime;
    }

    public String getCancelOperationId() {
        return cancelOperationId;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public String getCpId() {
        return cpId;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getCustomId() {
        return customId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public String getSpecialTreatOperatorId() {
        return specialTreatOperatorId;
    }

    public String getRealOperationId() {
        return realOperationId;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getState() {
        return state;
    }

    public String getServiceId() {
        return serviceId;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public String getSpecialTreatTime() {
        return specialTreatTime;
    }
}
