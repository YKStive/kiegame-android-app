package com.kiegame.mobile.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.entity.receive.UserLogin;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.PreferPlus;

/**
 * Created by: var_rain.
 * Created date: 2020/1/14.
 * Description: 网费
 */
public class NetFeeModel extends ViewModel {

    public MutableLiveData<String> addressName;
    private UserLogin login;

    public NetFeeModel() {
        this.login = PreferPlus.get(Setting.USER_LOGIN_OBJECT, UserLogin.class);

        this.addressName = new MutableLiveData<>();

        this.initUserData();
    }

    /**
     * 初始化用户数据
     */
    private void initUserData() {
        addressName.setValue(String.format("%s / %s", login.getServiceName(), login.getEmpName()));
    }
}
