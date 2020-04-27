package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewServiceConfirmBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 客维支付对话框
 */
public class ServicePay {

    private static ServicePay INS;
    private ViewServiceConfirmBinding binding;
    private ValueAnimator animator;
    private boolean isShowing;

    /**
     * 构造方法
     */
    private ServicePay() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_service_confirm, null, false);
        binding.tvBtnCancel.setOnClickListener(v -> this.hide());
        this.initAnim();
    }

    /**
     * 获取当前对象的静态实例
     *
     * @return {@link ServicePay}
     */
    public static ServicePay ins() {
        if (ServicePay.INS == null) {
            ServicePay.INS = new ServicePay();
        }
        return ServicePay.INS;
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        this.animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator.setDuration(100);
        this.animator.addUpdateListener(animation -> {
            if (this.binding != null) {
                this.binding.getRoot().setAlpha((Float) animation.getAnimatedValue());
            }
        });
        this.animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                if (!isReverse) {
                    InjectView.ins().inject(binding.getRoot());
                    isShowing = true;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (isReverse) {
                    InjectView.ins().clean(binding.getRoot());
                    isShowing = false;
                }
            }
        });
    }

    /**
     * 计算金额,分转换位元,保留两位
     *
     * @param money 金额, 分
     * @return 转换后的金额, 元
     */
    private String cal(int money) {
        BigDecimal source = new BigDecimal(money);
        BigDecimal ratio = new BigDecimal(100);
        BigDecimal divide = source.divide(ratio, 2, RoundingMode.HALF_UP);
        return divide.toString();
    }

    /**
     * 设置金额
     *
     * @param money 金额 单位: 分
     * @return {@link ServicePay}
     */
    public ServicePay money(int money) {
        binding.etServicePassword.setText("");
        binding.tvMustPayMoney.setText(cal(money));
        return this;
    }

    /**
     * 确认按钮点击事件
     *
     * @param onClickListener {@link OnClickListener}
     * @return {@link ServicePay}
     */
    public ServicePay confirm(OnClickListener onClickListener) {
        binding.tvBtnOk.setOnClickListener(v -> {
            if (onClickListener != null) {
                String password = binding.etServicePassword.getText().toString();
                if (!Text.empty(password)) {
                    onClickListener.onClick(password);
                    this.hide();
                } else {
                    Toast.show("请输入客维密码");
                }
            }
        });
        return this;
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (!this.isShowing) {
            this.animator.start();
        }
    }

    /**
     * 隐藏对话框
     */
    private void hide() {
        if (this.animator != null && this.isShowing) {
            this.animator.reverse();
        }
    }

    /**
     * 点击事件
     */
    public interface OnClickListener {

        /**
         * 点击回调方法
         */
        void onClick(String password);
    }
}
