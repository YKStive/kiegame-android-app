package com.kiegame.mobile.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityLoginBinding;
import com.kiegame.mobile.model.LoginModel;
import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.Prefer;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: 登录页
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private LoginModel model;
    private ValueAnimator switchShow;
    private ValueAnimator hostShow;

    @Override
    protected int onLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(LoginModel.class);
        binding.setActivity(this);
        binding.setModel(model);
        initEffect();
    }

    @Override
    protected void onView() {
        binding.ivLogo.setOnLongClickListener(v -> {
            if (binding.switchTestMode.getVisibility() == View.GONE) {
                switchShow.start();
            } else {
                switchShow.reverse();
            }
            return false;
        });
        binding.switchTestMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                hostShow.start();
            } else {
                hostShow.reverse();
            }
        });
        String host = Prefer.get(Setting.APP_HOST_TEST, "");
        if (!Text.empty(host)) {
            binding.etAddress.setText(host);
        }
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
     * 初始化动画特效
     */
    private void initEffect() {
        switchShow = ValueAnimator.ofFloat(0.0f, 1.0f);
        switchShow.setDuration(200);
        switchShow.addUpdateListener(animation -> {
            binding.switchTestMode.setAlpha((Float) animation.getAnimatedValue());
            binding.tvTestMode.setAlpha((Float) animation.getAnimatedValue());
        });
        switchShow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                binding.switchTestMode.setVisibility(View.VISIBLE);
                binding.tvTestMode.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (isReverse) {
                    binding.switchTestMode.setVisibility(View.GONE);
                    binding.tvTestMode.setVisibility(View.GONE);
                }
            }
        });
        hostShow = ValueAnimator.ofFloat(0.0f, 1.0f);
        hostShow.setDuration(200);
        hostShow.addUpdateListener(animation -> binding.llAddressInput.setAlpha((Float) animation.getAnimatedValue()));
        hostShow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                binding.llAddressInput.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (isReverse) {
                    binding.llAddressInput.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 登录事件处理
     */
    public void login() {
        if (binding.switchTestMode.isChecked()) {
            String host = binding.etAddress.getText().toString();
            if (!Text.empty(host)) {
                Prefer.put(Setting.APP_HOST_TEST, host);
                Network.change(host);
            }
        }
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
        // 保存到内存中
        Cache.ins().setToken(data.getLoginToken());
        Cache.ins().setLoginInfo(data);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
