package com.kiegame.mobile.utils;

import android.content.Context;
import android.os.Handler;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewInputBoxBinding;
import com.kiegame.mobile.ui.views.MoneyFilter;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 对话框
 */
public class InputBox {

    private static InputBox INS;
    private ViewInputBoxBinding binding;
    private boolean isShowing;

    /**
     * 构造方法
     */
    private InputBox() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_input_box, null, false);
        binding.tvDialogBtnCancel.setOnClickListener(v -> this.hide());
        binding.tvInputContent.setFilters(new InputFilter[]{new MoneyFilter()});
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
     * 确认按钮点击事件
     *
     * @param onClickListener {@link OnClickListener}
     * @return {@link InputBox}
     */
    public InputBox confirm(OnClickListener onClickListener) {
        binding.tvDialogBtnOk.setOnClickListener(v -> {
            if (!Text.empty(binding.tvInputContent.getText().toString())) {
                double money = getInputMoney();
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
    private double getInputMoney() {
        double money = -1;
        String text = binding.tvInputContent.getText().toString();
        String[] split = text.split("\\.");
        if (split.length > 2) {
            Toast.show("请输入正确的充值金额");
            return -1;
        }
        if (split.length == 2) {
            if (split[1].length() > 2) {
                Toast.show("请输入正确的充值金额");
                return -1;
            }
        }
        try {
            money = Double.parseDouble(text);
            if (money <= 0) {
                Toast.show("请输入正确的充值金额");
                return -1;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.show("请输入正确的金钱数值");
        }
        return money;
    }

    /**
     * 显示商品详情
     */
    public void show() {
        if (!this.isShowing) {
            isShowing = true;
            InjectView.ins().inject(binding.getRoot());
            binding.getRoot().postDelayed(() -> {
                binding.tvInputContent.setText("");
                binding.tvInputContent.setFocusable(true);
                binding.tvInputContent.requestFocus();
                this.onFocusChange(binding.tvInputContent.isFocused());
            }, 200);
        }
    }

    /**
     * 隐藏商品详情
     */
    private void hide() {
        if (this.isShowing) {
            isShowing = false;
            InjectView.ins().clean(binding.getRoot());
        }
    }

    /**
     * 焦点改变
     *
     * @param hasFocus 是否有焦点
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) binding.tvInputContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (isFocus) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    imm.hideSoftInputFromWindow(binding.tvInputContent.getWindowToken(), 0);
                }
            }
        }, 100);
    }


    /**
     * 点击事件
     */
    public interface OnClickListener {

        /**
         * 点击回调方法
         */
        void onClick(double money);
    }
}
