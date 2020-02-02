package com.kiegame.mobile.ui.activity;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityShopCarBinding;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.CouponSelect;

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
        binding.setCache(Cache.ins());
    }

    @Override
    protected void onView() {

    }

    @Override
    protected void onData() {

    }

    /**
     * 使用优惠券
     */
    public void couponUse() {
        CouponSelect.ins().set().show();
    }

    /**
     * 删除会员
     */
    public void deleteVipInfo() {
//        Cache.ins().setUserName("没有选择会员");
    }
}
