package com.kiegame.mobile.utils;

import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewDialogBoxBinding;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 对话框
 */
public class DialogBox {

    private static DialogBox INS;
    private ViewDialogBoxBinding binding;
    private boolean isShowing;

    /**
     * 构造方法
     */
    private DialogBox() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_dialog_box, null, false);
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
     * 设置标题
     *
     * @param content 标题字符串
     * @return {@link DialogBox}
     */
    public DialogBox title(String content) {
        binding.tvDialogTitle.setText(content);
        binding.tvDialogTitle.setVisibility(View.VISIBLE);
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
            isShowing = true;
            InjectView.ins().inject(binding.getRoot());
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
        this.binding.tvDialogTitle.setVisibility(View.GONE);
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
