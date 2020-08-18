package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewPaymentSuccessBinding;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 支付成功对话框
 */
public class PaySuccess {

    private static PaySuccess INS;
    private ViewPaymentSuccessBinding binding;
    private boolean isShowing;
    private OnClickListener onClickListener;

    /**
     * 构造方法
     */
    private PaySuccess() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_payment_success, null, false);
    }

    /**
     * 获取当前对象的静态实例
     *
     * @return {@link PaySuccess}
     */
    public static PaySuccess ins() {
        if (PaySuccess.INS == null) {
            PaySuccess.INS = new PaySuccess();
        }
        return PaySuccess.INS;
    }

    /**
     * 开始倒计时3秒后退出
     */
    private void startCountdown() {
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(3000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (onClickListener != null) {
                    onClickListener.onClick();
                }
                hide();
            }
        });
        animator.start();
    }

    /**
     * 点击事件
     *
     * @param onClickListener {@link OnClickListener}
     * @return {@link PaySuccess}
     */
    public PaySuccess confirm(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        binding.clRoot.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick();
            }
            this.hide();
        });
        return this;
    }

    /**
     * 设置订单号
     *
     * @param order 订单号
     * @return {@link PaySuccess}
     */
    public PaySuccess order(String order) {
        if (!Text.empty(order)) {
            binding.tvOrderId.setText(String.format("订单编号：%s", order));
            binding.tvOrderId.setVisibility(View.VISIBLE);
        } else {
            binding.tvOrderId.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (!this.isShowing) {
            isShowing = true;
            InjectView.ins().inject(binding.getRoot());
            startCountdown();
        }
    }

    /**
     * 隐藏对话框
     */
    private void hide() {
        if (this.isShowing) {
            isShowing = false;
            InjectView.ins().clean(binding.getRoot());
        }
    }

    /**
     * 点击事件
     */
    public interface OnClickListener {

        /**
         * 点击回调方法
         */
        void onClick();
    }
}
