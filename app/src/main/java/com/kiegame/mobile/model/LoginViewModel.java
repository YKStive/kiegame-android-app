package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: 登录
 */
public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> username;
    public MutableLiveData<String> password;
    public MutableLiveData<Boolean> keepPassword;

    private MutableLiveData<Object> login;

    public LoginViewModel() {
        this.password = new MediatorLiveData<>();
        this.username = new MediatorLiveData<>();
        this.keepPassword = new MutableLiveData<>();

        this.login = new MutableLiveData<>();
    }

    /**
     * 登录
     */
    public LiveData<Object> login() {
        // TODO: 2020/1/3 登录逻辑处理
        return this.login;
    }
}
