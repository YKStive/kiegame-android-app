package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.entity.receive.BuyOrderEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.PreferPlus;

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
        this.login = PreferPlus.get(Setting.USER_LOGIN_OBJECT, LoginEntity.class);
    }

    /**
     * 查询订单
     */
    public LiveData<List<BuyOrderEntity>> queryOrders(Integer payState, String startTime, String endTime, Integer payType, String seatNum, String customName) {
        Network.api().listBuyOrder(login.getServiceId(), payState, startTime, endTime, 2, payType, seatNum, customName, login.getEmpId())
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
