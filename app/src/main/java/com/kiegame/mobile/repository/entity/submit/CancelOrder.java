package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var_rain.
 * Created date: 2020/2/12.
 * Description: 取消订单
 */
public class CancelOrder {

    // *门店ID
    private String serviceId;
    // *退款渠道 固定 2
    private int refundChannel;
    // 备注
    private String memo;
    // *订单ID
    private String orderId;
    // *订单明细ID
    private String orderDetailId;
    // *是否反库存 固定 1
    private int isStock;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(int refundChannel) {
        this.refundChannel = refundChannel;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getIsStock() {
        return isStock;
    }

    public void setIsStock(int isStock) {
        this.isStock = isStock;
    }
}
