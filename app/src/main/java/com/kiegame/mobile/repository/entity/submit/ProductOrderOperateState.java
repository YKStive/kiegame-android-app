package com.kiegame.mobile.repository.entity.submit;

/**
 * 对订单产品的操作是否成功（完成，接单，抢单，出品）
 */
public class ProductOrderOperateState {
    public ProductOrderOperateState(int type, boolean isSuccess) {
        this.type = type;
        this.isSuccess = isSuccess;
    }

    public int type;
    public boolean isSuccess;
}
