package com.kiegame.mobile.ui.fragment;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentCommodityBinding;
import com.kiegame.mobile.ui.base.BaseFragment;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: 商品
 */
public class CommodityFragment extends BaseFragment<FragmentCommodityBinding> {

    @Override
    protected int onLayout() {
        return R.layout.fragment_commodity;
    }

    @Override
    protected void onObject() {

    }

    @Override
    protected void onView() {
        new QBadgeView(getContext())
                .bindTarget(binding.tvBadgeNum)
                .setBadgeNumber(20);
    }

    @Override
    protected void onData() {

    }
}
