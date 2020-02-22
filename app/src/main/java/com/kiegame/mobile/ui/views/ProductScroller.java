package com.kiegame.mobile.ui.views;

import android.content.Context;

import androidx.recyclerview.widget.LinearSmoothScroller;

/**
 * Created by: var_rain.
 * Created date: 2020/2/23.
 * Description: 滑动控制
 */
public class ProductScroller extends LinearSmoothScroller {

    public ProductScroller(Context context) {
        super(context);
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }
}
