package com.kiegame.mobile.ui.activity;

import androidx.lifecycle.ViewModelProviders;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityLoginBinding;
import com.kiegame.mobile.model.LoginViewModel;
import com.kiegame.mobile.ui.base.BaseActivity;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: 登录页
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private LoginViewModel model;

    @Override
    protected int onLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onObject() {
        model = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setActivity(this);
        binding.setModel(model);
    }

    @Override
    protected void onView() {

    }

    @Override
    protected void onData() {

    }

    /**
     * 登录事件处理
     */
    public void login() {
        model.login();
    }
}
