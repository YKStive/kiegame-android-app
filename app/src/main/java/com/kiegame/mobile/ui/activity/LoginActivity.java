package com.kiegame.mobile.ui.activity;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityLoginBinding;
import com.kiegame.mobile.model.LoginModel;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.Prefer;
import com.kiegame.mobile.utils.PreferPlus;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: 登录页
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private LoginModel model;

    @Override
    protected int onLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onObject() {
        model = ViewModelProviders.of(this).get(LoginModel.class);
        binding.setActivity(this);
        binding.setModel(model);
    }

    @Override
    protected void onView() {

    }

    @Override
    protected void onData() {
        boolean keep = Prefer.get(Setting.USER_ACCOUNT_KEEP, false);
        model.keepPassword.setValue(keep);
        model.username.setValue(Prefer.get(Setting.USER_LOGIN_ACCOUNT, ""));
        if (keep) {
            model.password.setValue(Prefer.get(Setting.USER_LOGIN_PASSWORD, ""));
        }
    }

    /**
     * 登录事件处理
     */
    public void login() {
        String account = model.username.getValue();
        String password = model.password.getValue();
        if (Text.empty(account)) {
            Toast.show("请填写登录账号");
            return;
        }
        if (Text.empty(password)) {
            Toast.show("请填写登录密码");
            return;
        }
        model.login().observe(this, this::loginResult);
    }

    /**
     * 登录接口返回数据处理
     *
     * @param data 数据对象
     */
    private void loginResult(LoginEntity data) {
        Boolean keep = model.keepPassword.getValue();
        Prefer.put(Setting.USER_ACCOUNT_KEEP, keep == null ? false : keep);
        Prefer.put(Setting.USER_LOGIN_ACCOUNT, model.username.getValue());
        if (keep != null && keep) {
            Prefer.put(Setting.USER_LOGIN_PASSWORD, model.password.getValue());
        } else {
            Prefer.remove(Setting.USER_LOGIN_PASSWORD);
        }
        Prefer.put(Setting.APP_NETWORK_TOKEN, data.getLoginToken());

        PreferPlus.put(Setting.USER_LOGIN_OBJECT, data);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
