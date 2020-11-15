package com.kiegame.mobile.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: ViewPager适配器
 */
public class OrderAdapter extends FragmentPagerAdapter {

    // 页面
    private List<Fragment> views;
    private String[] titles;

    /**
     * 构造方法
     *
     * @param views  页面数据
     * @param titles 标题数据
     */
    public OrderAdapter(FragmentManager fm, List<Fragment> views, String... titles) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.views = views;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return views == null ? 0 : views.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return views.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
