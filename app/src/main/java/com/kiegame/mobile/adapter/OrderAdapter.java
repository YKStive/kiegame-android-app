package com.kiegame.mobile.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: ViewPager适配器
 */
public class OrderAdapter extends PagerAdapter {

    // 页面
    private List<View> views;
    private String[] titles;

    /**
     * 构造方法
     *
     * @param views  页面数据
     * @param titles 标题数据
     */
    public OrderAdapter(List<View> views, String... titles) {
        this.views = views;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return views == null ? 0 : views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
