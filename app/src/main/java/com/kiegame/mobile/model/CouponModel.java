package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ActivityEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/2/19.
 * Description: 优惠券
 */
public class CouponModel extends ViewModel {

    private MutableLiveData<List<ActivityEntity>> serviceActivity;
    private MutableLiveData<List<ActivityEntity>> customerCoupons;

    private LoginEntity login;

    public CouponModel() {
        this.serviceActivity = new MutableLiveData<>();
        this.customerCoupons = new MutableLiveData<>();

        this.login = Cache.ins().getLoginInfo();
    }

    /**
     * 门店活动查询
     */
    public LiveData<List<ActivityEntity>> queryServiceActivity(String productId) {
        Network.api().listActivityByService(login.getServiceId(), productId)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<ActivityEntity>>(false) {
                    @Override
                    public void onSuccess(List<ActivityEntity> data, int total, int length) {
                        serviceActivity.setValue(data);
                    }
                });
        return this.serviceActivity;
    }

    /**
     * 会员卡券查询
     */
    public LiveData<List<ActivityEntity>> queryCustomerCoupons(String customerId, String productId) {
        Network.api().listActivityCardResultByTime(customerId, login.getServiceId(), productId)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<ActivityEntity>>(false) {
                    @Override
                    public void onSuccess(List<ActivityEntity> data, int total, int length) {
                        customerCoupons.setValue(data);
                    }
                });
        return this.customerCoupons;
    }
}
