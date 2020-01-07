package com.kiegame.mobile.ui.activity;

import android.content.Intent;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivitySplashBinding;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.worker.Worker;

/**
 * Created by: var_rain.
 * Created date: 2020/01/03.
 * Description: 启动页
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Override
    protected int onLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onObject() {
        Worker.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onView() {

    }

    @Override
    protected void onData() {

    }
}
