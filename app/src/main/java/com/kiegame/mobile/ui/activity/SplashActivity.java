package com.kiegame.mobile.ui.activity;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivitySplashBinding;
import com.kiegame.mobile.model.SplashModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.ui.base.BaseActivity;
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
        model = new ViewModelProvider(this).get(SplashModel.class);
    }

    @Override
    protected void onView() {

    }

    @Override
    protected void onData() {
        checkLogin();
    }

    /**
     * 检查登录
     */
    private void checkLogin() {
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
    private void loginResult(LoginEntity data) {
        if (data != null) {
            // 保存到内存
            Cache.ins().setToken(data.getLoginToken());
            Cache.ins().setLoginInfo(data);

            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
