package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.VersionEntity;
import com.kiegame.mobile.repository.entity.submit.UserLogin;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.MD5;
import com.kiegame.mobile.utils.Prefer;
import com.kiegame.mobile.utils.Version;

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
    private MutableLiveData<VersionEntity> update;

    public SplashModel() {
        this.account = Prefer.get(Setting.USER_LOGIN_ACCOUNT, "");
        this.password = Prefer.get(Setting.USER_LOGIN_PASSWORD, "");

        this.login = new MutableLiveData<>();
        this.update = new MutableLiveData<>();
    }

    /**
     * 登录
     */
    public LiveData<LoginEntity> autoLogin() {
        UserLogin bean = new UserLogin();
        bean.setLoginCode(account);
        bean.setLoginPass(MD5.encrypt(password, 32, false));
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

    /**
     * 检查更新
     */
    public LiveData<VersionEntity> update() {
        Network.api().queryAppVersion("android")
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<VersionEntity>>(false) {
                    @Override
                    public void onSuccess(List<VersionEntity> data, int total, int length) {
                        if (data != null && !data.isEmpty()) {
                            VersionEntity version = data.get(0);
                            if (version != null) {
                                if (Version.needUpdate(version.getAppCode())) {
                                    update.setValue(version);
                                    return;
                                }
                            }
                        }
                        update.setValue(null);
                    }
                });
        return this.update;
    }
}
