package com.kiegame.mobile.ui.activity;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityShopDetailBinding;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.ui.views.FlowLayout;
import com.kiegame.mobile.utils.Pixel;

/**
 * Created by: var_rain.
 * Created date: 2020/1/20.
 * Description: 商品详情
 */
public class ShopDetailActivity extends BaseActivity<ActivityShopDetailBinding> {

    private TextView select;

    @Override
    protected int onLayout() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void onObject() {
        binding.setActivity(this);
    }

    @Override
    protected void onView() {
        for (int i = 0; i < 16; i++) {
            if (i <= 8) {
                binding.flTasteLayout.addView(makeTextView("TAG"));
            } else {
                binding.flNormLayout.addView(makeTextView("TAG"));
            }
        }
    }

    @Override
    protected void onData() {

    }

    /**
     * 创建口味/规格选择按钮
     */
    private TextView makeTextView(String text) {
        FlowLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Pixel.dp2px(30));
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(Pixel.dp2px(16), 0, Pixel.dp2px(16), 0);
        textView.setTextColor(getResources().getColor(R.color.gray_white));
        textView.setBackgroundResource(R.drawable.shape_shop_norm_btn_none_background);
        textView.setTextSize(12);
        textView.setOnClickListener(v -> setBtnStyle((TextView) v));
        return textView;
    }

    /**
     * 设置点击样式
     *
     * @param view {@link TextView}
     */
    private void setBtnStyle(TextView view) {
        if (select != null) {
            cleanBtnStyle(select);
        }
        select = view;
        select.setTextColor(getResources().getColor(R.color.black_dark));
        select.setBackgroundResource(R.drawable.shape_shop_norm_btn_press_background);
    }

    /**
     * 清除点击样式
     *
     * @param view {@link TextView}
     */
    private void cleanBtnStyle(TextView view) {
        view.setBackgroundResource(R.drawable.shape_shop_norm_btn_none_background);
        view.setTextColor(getResources().getColor(R.color.gray_white));
    }
}
