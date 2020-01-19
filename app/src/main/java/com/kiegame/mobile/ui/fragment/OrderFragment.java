package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.OrderAdapter;
import com.kiegame.mobile.databinding.FragmentOrderBinding;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.utils.PreferPlus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: 订单
 */
public class OrderFragment extends BaseFragment<FragmentOrderBinding> {

    private List<View> views;
    private String[] titles;

    @Override
    protected int onLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void onObject() {
        binding.setLogin(PreferPlus.get(Setting.USER_LOGIN_OBJECT, LoginEntity.class));
        this.views = new ArrayList<>();
        this.titles = new String[]{
                getString(R.string.order_wait_payment),
                getString(R.string.order_all),
        };
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onView() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        // 待支付
        views.add(inflater.inflate(R.layout.view_order_wait_pay, null));
        // 全部订单
        views.add(inflater.inflate(R.layout.view_order_all, null));
        OrderAdapter adapter = new OrderAdapter(views, titles);
        binding.vpOrderViews.setAdapter(adapter);
        binding.tlOrderTab.setupWithViewPager(binding.vpOrderViews);
    }

    @Override
    protected void onData() {

    }
}
