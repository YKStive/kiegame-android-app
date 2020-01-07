package com.kiegame.mobile.ui.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.ViewAdapter;
import com.kiegame.mobile.databinding.ActivityMainBinding;
import com.kiegame.mobile.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/5.
 * Description: 主界面
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> implements ViewPager.OnPageChangeListener {

    // 子页面
    private List<View> views;
    // 点击状态图标数组
    private int[] press;
    // 未点击状态图标数组
    private int[] none;
    // 点击状态文字颜色
    private int pressColor;
    // 未点击状态文字颜色
    private int noneColor;
    // 当前选中的导航按钮
    private TextView selectBtn;

    @Override
    protected int onLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onObject() {
        binding.setActivity(this);

        this.views = new ArrayList<>();

        this.press = new int[]{
                R.drawable.ic_net_fee_press,
                R.drawable.ic_commodity_press,
                R.drawable.ic_order_press,
        };
        this.none = new int[]{
                R.drawable.ic_net_fee_none,
                R.drawable.ic_commodity_none,
                R.drawable.ic_order_none,
        };

        this.pressColor = this.getResources().getColor(R.color.main_nav_btn_press);
        this.noneColor = this.getResources().getColor(R.color.main_nav_btn_none);
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        // 网费充值
        views.add(inflater.inflate(R.layout.view_net_fee, null));
        // 商品
        views.add(inflater.inflate(R.layout.view_commodity, null));
        // 订单
        views.add(inflater.inflate(R.layout.view_order, null));
        ViewAdapter adapter = new ViewAdapter(views);
        binding.vpViews.setAdapter(adapter);
        binding.vpViews.addOnPageChangeListener(this);

        selectBtn = (TextView) binding.clNavBar.getChildAt(0);
    }

    @Override
    protected void onData() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (this.selectBtn != null) {
            this.cleanBtnStyle(this.selectBtn);
        }
        this.selectBtn = (TextView) binding.clNavBar.getChildAt(position);
        this.setBtnStyle(this.selectBtn);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 清除按钮点击样式
     *
     * @param btn 导航按钮
     */
    private void cleanBtnStyle(TextView btn) {
        int index = binding.clNavBar.indexOfChild(btn);
        btn.setTextColor(noneColor);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, none[index], 0, 0);
    }

    /**
     * 设置按钮点击样式
     *
     * @param btn 导航按钮
     */
    private void setBtnStyle(TextView btn) {
        int index = binding.clNavBar.indexOfChild(btn);
        btn.setTextColor(pressColor);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, press[index], 0, 0);
    }

    /**
     * 切换页面
     *
     * @param index 页面索引
     */
    public void setCurrentPage(int index) {
        binding.vpViews.setCurrentItem(index);
    }
}
