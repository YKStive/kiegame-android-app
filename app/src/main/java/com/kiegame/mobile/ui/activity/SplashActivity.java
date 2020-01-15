package com.kiegame.mobile.ui.activity;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivitySplashBinding;
import com.kiegame.mobile.model.SplashModel;
import com.kiegame.mobile.repository.entity.receive.UserLogin;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.Prefer;
import com.kiegame.mobile.utils.PreferPlus;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.worker.Worker;

/**
 * Created by: var_rain.
 * Created date: 2020/01/03.
 * Description: 启动页
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    private SplashModel model;

    @Override
    protected int onLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onObject() {
        model = ViewModelProviders.of(this).get(SplashModel.class);
    }

    @Override
    protected void onView() {

    }

    @Override
    protected void onData() {
        if (hasAccount()) {
            autoLogin();
        } else {
            Worker.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            });
        }
    }

    /**
     * 检查账号信息
     *
     * @return 是否存在账号信息 true: 存在 false: 不存在
     */
    private boolean hasAccount() {
        return !(Text.empty(model.account) || Text.empty(model.password));
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        model.autoLogin().observe(this, this::loginResult);
    }

    /**
     * 登录接口返回数据处理
     *
     * @param data 数据对象
     */
    private void loginResult(UserLogin data) {
        if (data != null) {
            Prefer.put(Setting.APP_NETWORK_TOKEN, data.getLoginToken());

            PreferPlus.put(Setting.USER_LOGIN_OBJECT, data);

            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
