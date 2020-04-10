package com.kiegame.mobile.repository.entity.receive;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 订单
 */
public class BuyOrderEntity {


    /**
     * orderType : 2
     * orderId : CP3525262500133888
     * sourceChannel : 1
     * operateTime : 2019-12-21 16:58:10
     * payState : 2
     * operatorName : 李四
     * customerName : 何总
     * orderState : 5
     * customerType : 1
     * payType : 2
     * orderAmount : 1400
     * payAmount : 7000
     * serviceId : 80803499721711205376
     * customerId : 2
     * orderBaseId : CO3525262500133889
     * itemList : [{"productId":"PD80803512393241918464","productFlavorName":"加糖","orderId":"CP3525262500133888","productSpecName":"500ML","productImg":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576154160009&di=d7f8312ccd27b4f365e3cbe794b171cd&imgtype=0&src=http%3A%2F%2Fqcloud.dpfile.com%2Fpc%2FuRwyg7aFjgcnOzcP4Vv2j_ltN6f1JddmzFkOJ0uUeSKpmzL61j2g1pCktPiS-HyFTK-l1dfmC-sNXFHV2eRvcw.jpg","discountAmount":0,"sellPrice":200,"productName":"自制测试商品04","orderDetailId":"CPD3525262500133890","productUnitName":"包","sellAmount":400,"sellCount":2,"needPayAmount":400,"productImgBig":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576154160009&di=d7f8312ccd27b4f365e3cbe794b171cd&imgtype=0&src=http%3A%2F%2Fqcloud.dpfile.com%2Fpc%2FuRwyg7aFjgcnOzcP4Vv2j_ltN6f1JddmzFkOJ0uUeSKpmzL61j2g1pCktPiS-HyFTK-l1dfmC-sNXFHV2eRvcw.jpg"},{"productId":"PD80803504003496311808","productFlavorName":"加糖","orderId":"CP3525262500133888","productSpecName":"500ML","productImg":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576154160009&di=d7f8312ccd27b4f365e3cbe794b171cd&imgtype=0&src=http%3A%2F%2Fqcloud.dpfile.com%2Fpc%2FuRwyg7aFjgcnOzcP4Vv2j_ltN6f1JddmzFkOJ0uUeSKpmzL61j2g1pCktPiS-HyFTK-l1dfmC-sNXFHV2eRvcw.jpg","discountAmount":0,"sellPrice":1000,"productName":"测试商品07","orderDetailId":"CPD3525262501198848","productUnitName":"包","sellAmount":1000,"sellCount":1,"needPayAmount":1000,"productImgBig":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576154160009&di=d7f8312ccd27b4f365e3cbe794b171cd&imgtype=0&src=http%3A%2F%2Fqcloud.dpfile.com%2Fpc%2FuRwyg7aFjgcnOzcP4Vv2j_ltN6f1JddmzFkOJ0uUeSKpmzL61j2g1pCktPiS-HyFTK-l1dfmC-sNXFHV2eRvcw.jpg"}]
     * operatorId : 80803503964748839937
     */

    private int orderType;
    private String orderId;
    private int sourceChannel;
    private String operateTime;
    private int payState;
    private String operatorName;
    private String customerName;
    private Integer discountAmount;
    private int orderState;
    private int customerType;
    private int payType;
    private int orderAmount;
    private int payAmount;
    private String serviceId;
    private String customerId;
    private String orderBaseId;
    private String operatorId;
    private List<BuyShopEntity> itemList;
    private boolean isSelect;
    private String idCard;
    private String seatNumber;

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getSourceChannel() {
        return sourceChannel;
    }

    public void setSourceChannel(int sourceChannel) {
        this.sourceChannel = sourceChannel;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderBaseId() {
        return orderBaseId;
    }

    public void setOrderBaseId(String orderBaseId) {
        this.orderBaseId = orderBaseId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public List<BuyShopEntity> getItemList() {
        return itemList;
    }

    public void setItemList(List<BuyShopEntity> itemList) {
        this.itemList = itemList;
    }

    public int getTotalAmount() {
        return discountAmount != null ? this.orderAmount - this.discountAmount : this.orderAmount;
    }
}
