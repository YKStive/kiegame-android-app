package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.entity.receive.UserLogin;
import com.kiegame.mobile.repository.entity.submit.SUserLogin;

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

    private MutableLiveData<UserLogin> login;

    public LoginModel() {
        this.password = new MediatorLiveData<>();
        this.username = new MediatorLiveData<>();
        this.keepPassword = new MutableLiveData<>();

        this.login = new MutableLiveData<>();
    }

    /**
     * 登录
     */
    public LiveData<UserLogin> login() {
        SUserLogin bean = new SUserLogin();
        bean.setLoginCode(username.getValue());
        bean.setLoginPass(password.getValue());
        bean.setLoginType(1);
        Network.api().userLogin(bean)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<UserLogin>>() {
                    @Override
                    public void onSuccess(List<UserLogin> data, int total, int length) {
                        login.postValue(data.get(0));
                    }
                });
        return this.login;
    }
}
