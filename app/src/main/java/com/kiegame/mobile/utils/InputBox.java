package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewInputBoxBinding;
import com.kiegame.mobile.ui.base.listener.OnAnimationListener;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 对话框
 */
public class InputBox {

    private static InputBox INS;
    private ViewInputBoxBinding binding;
    private ValueAnimator animator;
    private boolean isShowing;

    /**
     * 构造方法
     */
    private InputBox() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_input_box, null, false);
        binding.tvDialogBtnCancel.setOnClickListener(v -> this.hide());
        this.initAnim();
    }

    /**
     * 获取当前对象的静态实例
     *
     * @return {@link InputBox}
     */
    public static InputBox ins() {
        if (InputBox.INS == null) {
            InputBox.INS = new InputBox();
        }
        return InputBox.INS;
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
     * 确认按钮点击事件
     *
     * @param onClickListener {@link OnClickListener}
     * @return {@link InputBox}
     */
    public InputBox confirm(OnClickListener onClickListener) {
        binding.tvDialogBtnOk.setOnClickListener(v -> {
            if (!Text.empty(binding.tvInputContent.getText().toString())) {
                int money = getInputMoney();
                if (money > 0) {
                    if (onClickListener != null) {
                        onClickListener.onClick(money);
                    }
                    this.hide();
                }
            } else {
                Toast.show("请输入充值金额");
            }
        });
        return this;
    }

    /**
     * 获取充值金额
     *
     * @return 返回充值金额
     */
    private int getInputMoney() {
        int money = -1;
        String text = binding.tvInputContent.getText().toString();
        try {
            money = Integer.parseInt(text);
            if (money <= 0) {
                Toast.show("请输入正确的充值金额");
                return -1;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.show("请输入正确的整数金钱数值");
        }
        return money;
    }

    /**
     * 显示商品详情
     */
    public void show() {
        if (!this.isShowing) {
            this.animator.start();
            binding.tvInputContent.setText("");
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
        void onClick(int money);
    }
}
