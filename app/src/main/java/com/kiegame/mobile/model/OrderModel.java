package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.BuyOrderEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 待支付订单
 */
public class OrderModel extends ViewModel {

    private MutableLiveData<List<BuyOrderEntity>> orders;
    private LoginEntity login;

    public OrderModel() {
        this.orders = new MutableLiveData<>();
        this.login = Cache.ins().getLoginInfo();
    }

    /**
     * 查询订单
     */
    public LiveData<List<BuyOrderEntity>> queryOrders(Integer payState, String startTime, String endTime, Integer payType, String seatNum, String customName) {
//        Network.api().listBuyOrder(login.getServiceId(), payState, startTime, endTime, 2, payType, seatNum, customName, login.getEmpId())
        Network.api().listBuyOrder(login.getServiceId(), payState, startTime, endTime, 2, payType, seatNum, customName, null)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<BuyOrderEntity>>(false) {
                    @Override
                    public void onSuccess(List<BuyOrderEntity> data, int total, int length) {
                        orders.setValue(data);
                    }
                });
        return this.orders;
    }
}
