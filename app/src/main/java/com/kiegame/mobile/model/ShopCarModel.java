package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 确认订单
 */
public class ShopCarModel extends ViewModel {

    public MutableLiveData<String> searchName;
    public MutableLiveData<String> userName;

    private MutableLiveData<List<UserInfoEntity>> userInfos;

    public ShopCarModel() {
        this.searchName = new MutableLiveData<>();
        this.userName = new MutableLiveData<>();

        this.userInfos = new MutableLiveData<>();

        this.initialize();
    }

    /**
     * 初始化数据
     */
    private void initialize() {
        this.searchName.setValue("");
        this.userName.setValue("没有选择会员");
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
