package com.kiegame.mobile.ui.views;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewToastBinding;

/**
 * Created by: var_rain.
 * Created time: 2019/5/22.
 * Description: Toast提示框
 */
public enum ToastView {
    @SuppressLint("StaticFieldLeak") INS;
    private Toast mToast;
    private TextView mTextView;

    /**
     * 显示Toast
     *
     * @param content  显示内容
     * @param gravity  显示位置
     * @param duration 显示时间
     */
    public void showToast(String content, int gravity, int duration) {
        if (mToast == null) {
            mToast = new Toast(Game.ins().activity());
            mToast.setDuration(duration);
            mToast.setGravity(gravity, 0, 0);
            ViewToastBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_toast, null, false);
            mTextView = binding.tvToastText;
            mToast.setView(binding.getRoot());
        }
        mTextView.setText(content);
        mToast.show();
    }
}
