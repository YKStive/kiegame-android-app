package com.kiegame.mobile.ui.fragment;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentCommodityBinding;
import com.kiegame.mobile.model.CommodityModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.ShopSortEntity;
import com.kiegame.mobile.ui.activity.ShopCarActivity;
import com.kiegame.mobile.ui.base.BaseFragment;

import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: 商品
 */
public class CommodityFragment extends BaseFragment<FragmentCommodityBinding> {

    private CommodityModel model;
    private Badge badge;

    @Override
    protected int onLayout() {
        return R.layout.fragment_commodity;
    }

    @Override
    protected void onObject() {
        this.model = new ViewModelProvider(this).get(CommodityModel.class);
        binding.setFragment(this);
        binding.setModel(this.model);
    }

    @Override
    protected void onView() {
        badge = new QBadgeView(getContext())
                .bindTarget(binding.tvBadgeNum);
        badge.setBadgeNumber(Cache.ins().getShopSum());
    }

    @Override
    protected void onData() {
        model.listShopShot().observe(this, this::listShotResult);
        model.listShops().observe(this, this::lisShopResult);
        Cache.ins().setOnShopSumChangeListener(this, this::setShopSum);
    }

    /**
     * 添加到购物车
     *
     * @param shopSum 商品
     */
    private void setShopSum(int shopSum) {
        badge.setBadgeNumber(shopSum);
    }

    /**
     * 商品分类处理
     *
     * @param data 数据对象
     */
    private void listShotResult(List<ShopSortEntity> data) {

    }

    /**
     * 商品处理
     *
     * @param data 数据对象
     */
    private void lisShopResult(List<ShopEntity> data) {
        binding.mlvShopList.setShops(data);
    }

    /**
     * 跳转到购物车
     */
    public void startShopCarActivity() {
        startActivity(new Intent(getActivity(), ShopCarActivity.class));
    }
}
