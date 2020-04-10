package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.PayResultEntity;
import com.kiegame.mobile.repository.entity.submit.AddOrder;
import com.kiegame.mobile.repository.entity.submit.CancelOrder;
import com.kiegame.mobile.repository.entity.submit.DeleteOrder;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 待支付订单
 */
public class OrderModel extends ViewModel {

    private MutableLiveData<Object> cancelOrder;
    private MutableLiveData<Object> deleteOrder;
    private MutableLiveData<List<AddOrderEntity>> updateOrder;
    private MutableLiveData<List<PayResultEntity>> payResult;
    private LoginEntity login;

    public MutableLiveData<String> failureMessage;

    public OrderModel() {
        this.cancelOrder = new MutableLiveData<>();
        this.deleteOrder = new MutableLiveData<>();
        this.updateOrder = new MutableLiveData<>();
        this.payResult = new MutableLiveData<>();
        this.login = Cache.ins().getLoginInfo();

        this.failureMessage = new MutableLiveData<>();
    }

    public LoginEntity getLogin() {
        return login;
    }

    /**
     * 支付订单
     */
    public LiveData<List<AddOrderEntity>> updateOrder(String productOrderList, String rechargeOrderList, int buyPayType, String buyPayPassword, String paidInAmount, String memo) {
        AddOrder order = new AddOrder();
        order.setServiceId(login.getServiceId());
        order.setProductOrderList(productOrderList);
        order.setRechargeOrderList(rechargeOrderList);
        order.setBuyPayType(buyPayType);
        order.setBuyPayPassword(buyPayPassword);
        order.setBuyPayChannel(2);
        order.setPaidInAmount(paidInAmount);
        order.setMemo(memo);
        Network.api().updateOrderPay(order)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<AddOrderEntity>>(true, true) {
                    @Override
                    public void onSuccess(List<AddOrderEntity> data, int total, int length) {
                        updateOrder.setValue(data);
                    }

                    @Override
                    public void onPayFailure(String msg) {
                        failureMessage.setValue(msg);
                    }
                });
        return this.updateOrder;
    }

    /**
     * 取消订单
     */
    public LiveData<Object> cancelOrder(String orderId, String orderDetilId, String memo) {
        CancelOrder order = new CancelOrder();
        order.setServiceId(login.getServiceId());
        order.setRefundChannel(2);
        order.setIsStock(1);
        order.setOrderId(orderId);
        order.setOrderDetailId(orderDetilId);
        order.setMemo(memo);
        Network.api().cancelOrder(order)
                .compose(Scheduler.apply())
                .subscribe(new Subs<Object>() {
                    @Override
                    public void onSuccess(Object data, int total, int length) {
                        cancelOrder.setValue(data);
                    }
                });
        return this.cancelOrder;
    }

    /**
     * 删除订单
     */
    public LiveData<Object> deleteOrder(String orderId) {
        DeleteOrder order = new DeleteOrder();
        order.setOrderId(orderId);
        Network.api().deleteOrder(order)
                .compose(Scheduler.apply())
                .subscribe(new Subs<Object>() {
                    @Override
                    public void onSuccess(Object data, int total, int length) {
                        deleteOrder.setValue(data);
                    }
                });
        return this.deleteOrder;
    }

    /**
     * 查询支付结果
     */
    public LiveData<List<PayResultEntity>> queryPayState(String payId, String baseId) {
        Network.api().payResult(payId, baseId)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<PayResultEntity>>() {
                    @Override
                    public void onSuccess(List<PayResultEntity> data, int total, int length) {
                        payResult.setValue(data);
                    }
                });
        return this.payResult;
    }
}
