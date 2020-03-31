package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.submit.UserLogin;
import com.kiegame.mobile.utils.MD5;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: 登录
 */
public class LoginModel extends ViewModel {

    public MutableLiveData<String> username;
    public MutableLiveData<String> password;
    public MutableLiveData<Boolean> keepPassword;

    private MutableLiveData<LoginEntity> login;

    public LoginModel() {
        this.password = new MediatorLiveData<>();
        this.username = new MediatorLiveData<>();
        this.keepPassword = new MutableLiveData<>();

        this.login = new MutableLiveData<>();
    }

    /**
     * 登录
     */
    public LiveData<LoginEntity> login(String username, String password) {
        UserLogin bean = new UserLogin();
        bean.setLoginCode(username == null ? this.username.getValue() : username);
        String pass = password == null ? this.password.getValue() : password;
        if (pass != null) {
            bean.setLoginPass(MD5.encrypt(pass, 16, false));
        }
        bean.setLoginType(1);
        Network.api().userLogin(bean)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<LoginEntity>>() {
                    @Override
                    public void onSuccess(List<LoginEntity> data, int total, int length) {
                        login.setValue(data.get(0));
                    }
                });
        return this.login;
    }
}
