package com.kiegame.mobile.ui.activity;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityShopCarBinding;
import com.kiegame.mobile.ui.base.BaseActivity;

/**
 * Created by: var_rain.
 * Created date: 2020/1/20.
 * Description: 确认订单
 */
public class ShopCarActivity extends BaseActivity<ActivityShopCarBinding> {

    @Override
    protected int onLayout() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected void onObject() {
        binding.setActivity(this);
    }

    @Override
    protected void onView() {

    }

    @Override
    protected void onData() {

    }
}
