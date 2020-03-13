package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewPaymentFailureBinding;
import com.kiegame.mobile.ui.base.listener.OnAnimationListener;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 支付成功对话框
 */
public class PayFailure {

    private static PayFailure INS;
    private ViewPaymentFailureBinding binding;
    private ValueAnimator animator;
    private boolean isShowing;

    /**
     * 构造方法
     */
    private PayFailure() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_payment_failure, null, false);
        this.binding.tvBtnOk.setOnClickListener(v -> this.hide());
        this.initAnim();
    }

    /**
     * 获取当前对象的静态实例
     *
     * @return {@link PayFailure}
     */
    public static PayFailure ins() {
        if (PayFailure.INS == null) {
            PayFailure.INS = new PayFailure();
        }
        return PayFailure.INS;
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
     * 设置失败信息
     *
     * @param msg 失败信息
     * @return {@link PayFailure}
     */
    public PayFailure message(String msg) {
        binding.tvPayMsg.setText(msg);
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
}
