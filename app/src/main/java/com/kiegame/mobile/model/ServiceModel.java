package com.kiegame.mobile.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.GoodsOrderEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.ServiceCallEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 服务
 */
public class ServiceModel extends ViewModel {

    private LoginEntity login;

    //用于记录多个请求是否都已经结束
    private MutableLiveData<Boolean> isRefreshFinish;
    //服务列表数据
    private MutableLiveData<List<ServiceCallEntity>> serviceCallListData;
    //商品接单数据
    private MutableLiveData<List<GoodsOrderEntity>> goodsOrderData;

    public ServiceModel() {
        this.login = Cache.ins().getLoginInfo();
        serviceCallListData = new MutableLiveData<>();
        goodsOrderData = new MutableLiveData<>();
        isRefreshFinish = new MutableLiveData<>(true);
    }


    public LoginEntity getLogin() {
        return login;
    }


    public MutableLiveData<List<ServiceCallEntity>> getServiceCallListData() {
        return serviceCallListData;
    }

    public MutableLiveData<List<GoodsOrderEntity>> getGoodsOrderData() {
        return goodsOrderData;
    }

    public void setGoodsOrderData(MutableLiveData<List<GoodsOrderEntity>> goodsOrderData) {
        this.goodsOrderData = goodsOrderData;
    }

    public MutableLiveData<Boolean> getIsRefreshFinish() {
        return isRefreshFinish;
    }

    //刷新服务fragment的数据
    public void refresh() {
        // TODO: 2020/11/11 调用借口更新用户数据

        // TODO: 2020/11/11 调用接口查询服务列表
        List<ServiceCallEntity> serviceCallEntityList = new ArrayList<>();
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(2));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(2));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(2));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallListData.postValue(serviceCallEntityList);
        // TODO: 2020/11/11 调用接口查询商品订单列表
        List<GoodsOrderEntity> goodsOrderEntityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GoodsOrderEntity goodsOrderEntity = new GoodsOrderEntity();
            List<GoodsOrderEntity.SingleOrderEntity> singleOrderEntityList = new ArrayList<>();
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(1));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(2));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(4));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(6));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(3));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(5));
            goodsOrderEntity.setSingleOrderEntityList(singleOrderEntityList);
            goodsOrderEntityList.add(goodsOrderEntity);
        }
        goodsOrderData.postValue(goodsOrderEntityList);
        //刷新完成
        isRefreshFinish.postValue(true);
    }

    //加载更多服务
    public void loadMoreServiceData() {
        // TODO: 2020/11/11 调用接口查询服务列表
        List<ServiceCallEntity> serviceCallEntityList = new ArrayList<>();
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(2));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(2));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.add(new ServiceCallEntity(2));
        serviceCallEntityList.add(new ServiceCallEntity(1));
        serviceCallEntityList.addAll(0, serviceCallListData.getValue());
        serviceCallListData.postValue(serviceCallEntityList);
    }

    //加载跟多商品订单
    public void loadMoreGoodsOrderData() {
        // TODO: 2020/11/11 调用接口查询商品订单列表
        List<GoodsOrderEntity> goodsOrderEntityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GoodsOrderEntity goodsOrderEntity = new GoodsOrderEntity();
            List<GoodsOrderEntity.SingleOrderEntity> singleOrderEntityList = new ArrayList<>();
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(1));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(2));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(4));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(6));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(3));
            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(5));
            goodsOrderEntity.setSingleOrderEntityList(singleOrderEntityList);
            goodsOrderEntityList.add(goodsOrderEntity);
        }
        goodsOrderEntityList.addAll(0, goodsOrderData.getValue());
        goodsOrderData.postValue(goodsOrderEntityList);
    }
}
