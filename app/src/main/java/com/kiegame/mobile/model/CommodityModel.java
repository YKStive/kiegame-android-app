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

    private MutableLiveData<List<ShopSortEntity>> shopSort;
    private MutableLiveData<List<ShopEntity>> shops;

    public LoginEntity login;

    public CommodityModel() {
        this.searchShop = new MutableLiveData<>();

        this.shopSort = new MutableLiveData<>();
        this.shops = new MutableLiveData<>();

        this.login = Cache.ins().getLoginInfo();
    }

    /**
     * 商品分类
     */
    public LiveData<List<ShopSortEntity>> listShopShot() {
        Network.api().listTag(1)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<ShopSortEntity>>(false) {
                    @Override
                    public void onSuccess(List<ShopSortEntity> data, int total, int length) {
                        shopSort.setValue(data);
                    }
                });
        return this.shopSort;
    }

    /**
     * 查询商品
     */
    public LiveData<List<ShopEntity>> listShops() {
        Network.api().queryShops(login.getServiceId(), null, null, null)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<ShopEntity>>(false) {
                    @Override
                    public void onSuccess(List<ShopEntity> data, int total, int length) {
                        List<ShopEntity> entities = Cache.ins().getEntities();
                        if (entities != null && !entities.isEmpty()) {
                            for (ShopEntity entity : entities) {
                                if (entity != null) {
                                    for (ShopEntity shop : data) {
                                        if (shop != null) {
                                            if (entity.getProductId().equals(shop.getProductId())) {
                                                shop.setBuySize(entity.getBuySize());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        shops.setValue(data);
                        Cache.ins().setEntities(data);
                    }

                    @Override
                    public void onFail() {
                        shops.setValue(null);
                    }
                });
        return this.shops;
    }
}
