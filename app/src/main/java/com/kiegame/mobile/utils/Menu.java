package com.kiegame.mobile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiegame.mobile.R;

/**
 * Created by: var_rain.
 * Created date: 2020/2/7.
 * Description: 菜单
 */
public class Menu {

    private PopupWindow pw;
    private TextView exit;

    /**
     * 构造方法
     */
    @SuppressLint("InflateParams")
    public Menu(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_menu_auto_fill, null, false);
        exit = view.findViewById(R.id.tv_exit_login);
        pw = new PopupWindow(context);
        pw.setContentView(view);
        pw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.translucent)));
        pw.setOutsideTouchable(true);
        pw.setSplitTouchEnabled(true);
    }

    /**
     * 设置回调
     *
     * @param onClickListener {@link View.OnClickListener}
     * @return {@link Menu}
     */
    public Menu callback(View.OnClickListener onClickListener) {
        if (exit != null) {
            exit.setOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * 显示弹窗
     *
     * @param view {@link View} 依附的View
     */
    public void show(View view) {
        if (pw != null) {
            if (pw.isShowing()) {
                pw.dismiss();
            }
            pw.showAsDropDown(view);
        }
    }
}
