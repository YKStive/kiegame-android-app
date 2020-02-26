package com.kiegame.mobile.ui.fragment;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentCommodityBinding;
import com.kiegame.mobile.model.CommodityModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.ShopSortEntity;
import com.kiegame.mobile.ui.activity.ShopCarActivity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

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
    public void onResume() {
        super.onResume();
        if (binding != null && binding.mlvShopList != null) {
            binding.mlvShopList.update();
        }
    }

    @Override
    protected int onLayout() {
        return R.layout.fragment_commodity;
    }

    @Override
    protected void onObject() {
        this.model = new ViewModelProvider(this).get(CommodityModel.class);
        binding.setFragment(this);
        binding.setModel(this.model);
        Cache.ins().getShopObserver().observe(this, integer -> {
            if (integer != null) {
                if (integer == -1) {
                    binding.mlvShopList.update();
                } else {
                    LiveData<List<ShopEntity>> liveData = model.listShops();
                    if (!liveData.hasObservers()) {
                        liveData.observe(this, this::lisShopResult);
                    }
                }
            }
        });
        model.searchShop.observe(this, this::shopSearch);
    }

    @Override
    protected void onView() {
        badge = new QBadgeView(getContext())
                .bindTarget(binding.tvBadgeNum);
        badge.setBadgeNumber(Cache.ins().getShopSum());
        binding.srlLayout.setOnRefreshListener(refreshLayout -> {
            LiveData<List<ShopEntity>> liveData = model.listShops();
            if (!liveData.hasObservers()) {
                liveData.observe(this, this::lisShopResult);
            }
        });
        binding.etSearchShop.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                model.searchShop.setValue("");
            }
        });
    }

    @Override
    protected void onData() {
        model.listShopShot().observe(this, this::listShotResult);
        Cache.ins().setOnShopSumChangeListener(this, this::setShopSum);
        model.listShops().observe(this, this::lisShopResult);
    }

    /**
     * 商品搜索
     *
     * @param name 商品关键字
     */
    private void shopSearch(String name) {
        if (!Text.empty(name)) {
            List<ShopEntity> shops = Cache.ins().getEntities();
            if (shops != null && !shops.isEmpty()) {
                for (ShopEntity shop : shops) {
                    if (shop != null && shop.getProductName() != null && shop.getProductName().contains(name)) {
                        binding.mlvShopList.scrollToProtectId(shop.getProductId());
                        break;
                    }
                }
            }
        }
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
        binding.srlLayout.finishRefresh();
        binding.tvShopEmpty.setVisibility((data == null || data.isEmpty()) ? View.VISIBLE : View.GONE);
        binding.mlvShopList.setShops(data);
    }

    /**
     * 跳转到购物车
     */
    public void startShopCarActivity() {
        if (badge.getBadgeNumber() > 0) {
            startActivity(new Intent(getActivity(), ShopCarActivity.class));
        } else {
            Toast.show("请选择商品");
        }
    }
}
