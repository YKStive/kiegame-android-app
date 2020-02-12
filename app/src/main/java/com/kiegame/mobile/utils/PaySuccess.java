package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewPaymentSuccessBinding;
import com.kiegame.mobile.ui.base.listener.OnAnimationListener;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 支付成功对话框
 */
public class PaySuccess {

    private static PaySuccess INS;
    private ViewPaymentSuccessBinding binding;
    private ValueAnimator animator;
    private boolean isShowing;

    /**
     * 构造方法
     */
    private PaySuccess() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_payment_success, null, false);
        this.initAnim();
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
        this.animator.addListener(new OnAnimationListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                InjectView.ins().inject(binding.getRoot());
                isShowing = true;
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
     * 点击事件
     *
     * @param onClickListener {@link OnClickListener}
     * @return {@link PaySuccess}
     */
    public PaySuccess confirm(OnClickListener onClickListener) {
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
        void onClick();
    }
}
