package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewDialogBoxBinding;
import com.kiegame.mobile.ui.base.listener.OnAnimationListener;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 对话框
 */
public class DialogBox {

    private static DialogBox INS;
    private ViewDialogBoxBinding binding;
    private ValueAnimator animator;
    private boolean isShowing;

    /**
     * 构造方法
     */
    private DialogBox() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_dialog_box, null, false);
        this.initAnim();
    }

    /**
     * 获取当前对象的静态实例
     *
     * @return {@link DialogBox}
     */
    public static DialogBox ins() {
        if (DialogBox.INS == null) {
            DialogBox.INS = new DialogBox();
        }
        return DialogBox.INS;
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
     * 设置内容
     *
     * @param content 内容字符串
     * @return {@link DialogBox}
     */
    public DialogBox text(String content) {
        binding.tvDialogContent.setText(content);
        binding.tvDialogBtnOk.setVisibility(View.GONE);
        binding.tvDialogBtnCancel.setVisibility(View.GONE);
        return this;
    }

    /**
     * 确认按钮点击事件
     *
     * @param onClickListener {@link OnClickListener}
     * @return {@link DialogBox}
     */
    public DialogBox confirm(OnClickListener onClickListener) {
        binding.tvDialogBtnOk.setVisibility(View.VISIBLE);
        binding.tvDialogBtnOk.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick();
            }
            this.hide();
        });
        return this;
    }

    /**
     * 取消按钮点击事件
     *
     * @param onClickListener {@link OnClickListener}
     * @return {@link DialogBox}
     */
    public DialogBox cancel(OnClickListener onClickListener) {
        binding.tvDialogBtnCancel.setVisibility(View.VISIBLE);
        binding.tvDialogBtnCancel.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick();
            }
            this.hide();
        });
        return this;
    }

    /**
     * 显示商品详情
     */
    public void show() {
        if (!this.isShowing) {
            this.animator.start();
        }
    }

    /**
     * 隐藏商品详情
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
