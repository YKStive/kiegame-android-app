package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.entity.receive.BannerEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.PreferPlus;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/14.
 * Description: 网费
 */
public class NetFeeModel extends ViewModel {

    public MutableLiveData<String> userSearch;
    public MutableLiveData<String> userName;
    public MutableLiveData<String> amount;
    public MutableLiveData<String> award;
    public MutableLiveData<String> gabon;
    public MutableLiveData<String> bonus;
    public MutableLiveData<String> recharge;
    public MutableLiveData<Boolean> showDeleteBtn;
    public MutableLiveData<Boolean> paymentOnline;
    public MutableLiveData<Boolean> paymentOffline;

    public LoginEntity login;

    private MutableLiveData<List<BannerEntity>> banner;
    private MutableLiveData<List<UserInfoEntity>> userInfos;

    public NetFeeModel() {
        this.login = PreferPlus.get(Setting.USER_LOGIN_OBJECT, LoginEntity.class);

        this.userSearch = new MutableLiveData<>();
        this.userName = new MutableLiveData<>();
        this.amount = new MutableLiveData<>();
        this.award = new MutableLiveData<>();
        this.gabon = new MutableLiveData<>();
        this.bonus = new MutableLiveData<>();
        this.recharge = new MutableLiveData<>();
        this.showDeleteBtn = new MutableLiveData<>();
        this.paymentOnline = new MutableLiveData<>();
        this.paymentOffline = new MutableLiveData<>();

        this.banner = new MutableLiveData<>();
        this.userInfos = new MutableLiveData<>();

        this.initData();
    }

    /**
     * 初始化用户数据
     */
    private void initData() {
        this.userName.setValue("没有选择会员");
        this.showDeleteBtn.setValue(false);
        this.amount.setValue("0");
        this.award.setValue("0");
        this.gabon.setValue("0");
        this.bonus.setValue("0");
        this.recharge.setValue("0.00");
        this.paymentOnline.setValue(true);
    }

    /**
     * 查询banner图
     */
    public LiveData<List<BannerEntity>> queryBanner() {
        Network.api().queryBannerList(2)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<BannerEntity>>(false) {
                    @Override
                    public void onSuccess(List<BannerEntity> data, int total, int length) {
                        banner.setValue(data);
                    }
                });
        return this.banner;
    }

    /**
     * 查询用户信息
     */
    public LiveData<List<UserInfoEntity>> searchUserInfos(String keywords) {
        Network.api().queryUserInfos(keywords)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<UserInfoEntity>>(false) {
                    @Override
                    public void onSuccess(List<UserInfoEntity> data, int total, int length) {
                        userInfos.setValue(data);
                    }
                });
        return this.userInfos;
    }
}
