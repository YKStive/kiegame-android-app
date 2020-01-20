package com.kiegame.mobile.ui.fragment;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentCommodityBinding;
import com.kiegame.mobile.model.CommodityModel;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.ShopSortEntity;
import com.kiegame.mobile.ui.activity.ShopCarActivity;
import com.kiegame.mobile.ui.base.BaseFragment;

import java.util.ArrayList;
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
    private List<ShopEntity> shops;

    @Override
    protected int onLayout() {
        return R.layout.fragment_commodity;
    }

    @Override
    protected void onObject() {
        this.shops = new ArrayList<>();
        model = ViewModelProviders.of(this).get(CommodityModel.class);
        binding.setFragment(this);
        binding.setModel(model);
    }

    @Override
    protected void onView() {
        binding.mlvShopList.setCallback(this::joinShop);
        badge = new QBadgeView(getContext())
                .bindTarget(binding.tvBadgeNum);
        badge.setBadgeNumber(shops.size());
    }

    @Override
    protected void onData() {
        model.listShopShot().observe(this, this::listShotResult);
        model.listShops().observe(this, this::lisShopResult);
    }

    /**
     * 添加到购物车
     *
     * @param shop 商品
     */
    private void joinShop(ShopEntity shop) {
        if (shop.getBuySize() == 0) {
            shops.remove(shop);
        } else {
            if (!shops.contains(shop)) {
                shops.add(shop);
            }
        }
        int size = 0;
        for (ShopEntity entity : shops) {
            size += entity.getBuySize();
        }
        badge.setBadgeNumber(size);
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
