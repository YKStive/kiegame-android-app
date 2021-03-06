package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.submit.UserLogin;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.MD5;
import com.kiegame.mobile.utils.Prefer;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/14.
 * Description: 启动页
 */
public class SplashModel extends ViewModel {

    public String account;
    public String password;

    private MutableLiveData<LoginEntity> login;

    public SplashModel() {
        this.account = Prefer.get(Setting.USER_LOGIN_ACCOUNT, "");
        this.password = Prefer.get(Setting.USER_LOGIN_PASSWORD, "");

        this.login = new MutableLiveData<>();
    }

    /**
     * 登录
     */
    public LiveData<LoginEntity> autoLogin() {
        UserLogin bean = new UserLogin();
        bean.setLoginCode(account);
        bean.setLoginPass(MD5.encrypt(password, 16, false));
        bean.setLoginType(1);
        Network.api().userLogin(bean)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<LoginEntity>>(false) {
                    @Override
                    public void onSuccess(List<LoginEntity> data, int total, int length) {
                        login.postValue(data.get(0));
                    }

                    @Override
                    public void onFail() {
                        login.postValue(null);
                    }
                });
        return this.login;
    }
}
