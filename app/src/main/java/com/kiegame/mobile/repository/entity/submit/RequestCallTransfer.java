package com.kiegame.mobile.repository.entity.submit;

public class RequestCallTransfer {

    /**
     * cpId :
     * transferToEmpId :
     */
    private String cpId;
    private String transferToEmpId;

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public void setTransferToEmpId(String transferToEmpId) {
        this.transferToEmpId = transferToEmpId;
    }

    public String getCpId() {
        return cpId;
    }

    public String getTransferToEmpId() {
        return transferToEmpId;
    }

    @Override
    public String toString() {
        return "RequestCallTransfer{" +
                "cpId='" + cpId + '\'' +
                ", transferToEmpId='" + transferToEmpId + '\'' +
                '}';
    }
}
