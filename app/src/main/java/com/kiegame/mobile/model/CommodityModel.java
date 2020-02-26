package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.ShopSortEntity;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 商品
 */
public class CommodityModel extends ViewModel {

    public MutableLiveData<String> searchShop;

    private MutableLiveData<List<ShopSortEntity>> shopType;
    private MutableLiveData<List<ShopSortEntity>> shopTag;
    private MutableLiveData<List<ShopEntity>> shops;

    public LoginEntity login;

    public CommodityModel() {
        this.searchShop = new MutableLiveData<>();

        this.shopType = new MutableLiveData<>();
        this.shopTag = new MutableLiveData<>();
        this.shops = new MutableLiveData<>();

        this.login = Cache.ins().getLoginInfo();
    }

    /**
     * 商品分类
     */
    public LiveData<List<ShopSortEntity>> listShopType() {
        Network.api().listType()
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<ShopSortEntity>>(false) {
                    @Override
                    public void onSuccess(List<ShopSortEntity> data, int total, int length) {
                        shopType.setValue(data);
                    }
                });
        return this.shopType;
    }

    /**
     * 商品标签
     */
    public LiveData<List<ShopSortEntity>> listShopTag() {
        Network.api().listTag(1)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<ShopSortEntity>>(false) {
                    @Override
                    public void onSuccess(List<ShopSortEntity> data, int total, int length) {
                        shopTag.setValue(data);
                    }
                });
        return this.shopTag;
    }

    /**
     * 查询商品
     */
    public LiveData<List<ShopEntity>> listShops(String productTypeId, String productName, String productTagId, boolean isShowLoading) {
        Network.api().queryShops(login.getServiceId(), productTypeId, productName, productTagId, 1)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<ShopEntity>>(isShowLoading) {
                    @Override
                    public void onSuccess(List<ShopEntity> data, int total, int length) {
                        shops.setValue(data);
                    }

                    @Override
                    public void onFail() {
                        shops.setValue(null);
                    }
                });
        return this.shops;
    }
}
