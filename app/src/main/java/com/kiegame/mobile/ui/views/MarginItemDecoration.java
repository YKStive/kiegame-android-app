package com.kiegame.mobile.ui.views;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 分割线
 */
public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    /* 边距 */
    private int margin;

    public MarginItemDecoration(float margin) {
        this.margin = dp2px(margin);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, 0, margin);
    }

    /**
     * pd转px
     *
     * @param dp dp值
     * @return 转换后的px值
     */
    private int dp2px(float dp) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
