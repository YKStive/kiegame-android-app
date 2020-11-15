package com.kiegame.mobile.model;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.logger.Log;
import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.GoodsOrderEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.ServiceCallEntity;
import com.kiegame.mobile.repository.entity.submit.CallServiceRequest;
import com.kiegame.mobile.repository.entity.submit.ProduceOrderOperate;
import com.kiegame.mobile.repository.entity.submit.ProductOrderOperateState;
import com.kiegame.mobile.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_COMPLETE;
import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_GRAB;
import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_PRODUCE;
import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_TAKE;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 服务
 */
public class ServiceModel extends ViewModel {

    private LoginEntity login;
    private int currentCallServicePage = 0;
    private int currentProductOrderPage = 0;


    //用于记录多个请求是否都已经结束
    private MutableLiveData<Boolean> isRefreshFinish;

    //是否转接成功
    public MutableLiveData<Boolean> isTransferSuccess;

    //倒数计时器
    public MutableLiveData<Long> counter;

    //服务列表数据
    private MutableLiveData<List<ServiceCallEntity>> serviceCallListData;

    //商品接单数据
    private MutableLiveData<List<GoodsOrderEntity>> goodsOrderData;

    //商品订单操作是否成功
    public MutableLiveData<ProductOrderOperateState> goodsOrderOperateSuccess = new MutableLiveData<>();


    public ServiceModel() {
        this.login = Cache.ins().getLoginInfo();
        serviceCallListData = new MutableLiveData<>();
        isTransferSuccess = new MutableLiveData<>();
        counter = new MutableLiveData<>();
        goodsOrderData = new MutableLiveData<>();
        isRefreshFinish = new MutableLiveData<>(true);

        CountDownTimer timer = new CountDownTimer(Integer.MAX_VALUE, 1000) {
            @Override
            public void onTick(long left) {
                counter.postValue(left);
            }

            @Override
            public void onFinish() {

            }
        }.start();
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
        currentCallServicePage = 0;
        currentProductOrderPage = 0;
        requestCallServices();

//        List<ServiceCallEntity> serviceCallEntityList = new ArrayList<>();
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(2));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(2));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(2));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallListData.postValue(serviceCallEntityList);
        // TODO: 2020/11/11 调用接口查询商品订单列表
        requestProductOrders();
//        List<GoodsOrderEntity> goodsOrderEntityList = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            GoodsOrderEntity goodsOrderEntity = new GoodsOrderEntity();
//            List<GoodsOrderEntity.SingleOrderEntity> singleOrderEntityList = new ArrayList<>();
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(1));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(2));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(4));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(6));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(3));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(5));
//            goodsOrderEntity.setSingleOrderEntityList(singleOrderEntityList);
//            goodsOrderEntityList.add(goodsOrderEntity);
//        }
//        goodsOrderData.postValue(goodsOrderEntityList);
        //刷新完成

    }

    /**
     * 请求商品接单列表
     */
    private void requestProductOrders() {
        CallServiceRequest callServiceRequest = new CallServiceRequest();
        callServiceRequest.setPage(currentProductOrderPage);
        callServiceRequest.setServiceId(Cache.ins().getLoginInfo().getServiceId());
        Network.api().getProductGoodsOrders(callServiceRequest)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<GoodsOrderEntity>>() {

                    @Override
                    public void onSuccess(List<GoodsOrderEntity> data, int total, int length) {
                        if (currentProductOrderPage > 0) {
                            data.add(0, (GoodsOrderEntity) goodsOrderData.getValue());
                        } else {
                            isRefreshFinish.postValue(true);
                        }
                        goodsOrderData.postValue(data);
                    }
                });
    }

    /**
     * 请求呼叫服务列表
     */
    private void requestCallServices() {
        CallServiceRequest callServiceRequest = new CallServiceRequest();
        callServiceRequest.setPage(currentCallServicePage);
        callServiceRequest.setServiceId(Cache.ins().getLoginInfo().getServiceId());
        Network.api().getCallServices(callServiceRequest)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<ServiceCallEntity>>() {

                    @Override
                    public void onSuccess(List<ServiceCallEntity> data, int total, int length) {
                        if (currentCallServicePage > 0) {
                            data.add(0, (ServiceCallEntity) serviceCallListData.getValue());
                        } else {
                            isRefreshFinish.postValue(true);
                        }
                        serviceCallListData.postValue(data);
                    }
                });
    }


    /**
     * 呼叫转接
     *
     * @param cancelOperatorId 转接人id
     * @param cpId             服务id
     */
    public void callServiceTransfer(String cancelOperatorId, String cpId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cancelOperatorId", cancelOperatorId);
            jsonObject.put("cpId", cpId);
            Network.api().transferCallServices(jsonObject)
                    .compose(Scheduler.apply())
                    .subscribe(new Subs<Object>() {

                                   @Override
                                   public void onSuccess(Object data, int total, int length) {
                                       isTransferSuccess.postValue(true);
                                   }
                               }
                    );
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //加载更多服务
    public void loadMoreServiceData() {
//         TODO: 2020/11/11 调用接口查询服务列表
        currentCallServicePage++;
        requestCallServices();
//        List<ServiceCallEntity> serviceCallEntityList = new ArrayList<>();
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(2));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(2));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.add(new ServiceCallEntity(2));
//        serviceCallEntityList.add(new ServiceCallEntity(1));
//        serviceCallEntityList.addAll(0, serviceCallListData.getValue());
//        serviceCallListData.postValue(serviceCallEntityList);
    }

    //加载跟多商品订单
    public void loadMoreGoodsOrderData() {
        // TODO: 2020/11/11 调用接口查询商品订单列表
        currentProductOrderPage++;
        requestProductOrders();
//        List<GoodsOrderEntity> goodsOrderEntityList = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            GoodsOrderEntity goodsOrderEntity = new GoodsOrderEntity();
//            List<GoodsOrderEntity.SingleOrderEntity> singleOrderEntityList = new ArrayList<>();
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(1));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(2));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(4));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(6));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(3));
//            singleOrderEntityList.add(new GoodsOrderEntity.SingleOrderEntity(5));
//            goodsOrderEntity.setSingleOrderEntityList(singleOrderEntityList);
//            goodsOrderEntityList.add(goodsOrderEntity);
//        }
//        goodsOrderEntityList.addAll(0, goodsOrderData.getValue());
//        goodsOrderData.postValue(goodsOrderEntityList);
    }

    /**
     * 抢单
     */
    public void grabProductOrder(String orderId, String productId) {
        ProduceOrderOperate request = new ProduceOrderOperate();
        request.setOrderId(orderId);
        request.setProductId(productId);
        Network.api().productOrderGrab(request)
                .compose(Scheduler.apply())
                .subscribe(new Subs<Object>() {

                    @Override
                    public void onSuccess(Object data, int total, int length) {
                        ProductOrderOperateState result = new ProductOrderOperateState(DO_ORDER_TYPE_GRAB, true);
                        goodsOrderOperateSuccess.postValue(result);
                    }
                });

    }

    /**
     * 出品
     */
    public void produceProductOrder(String orderId, String productId) {
        ProduceOrderOperate request = new ProduceOrderOperate();
        request.setOrderId(orderId);
        request.setProductId(productId);
        Network.api().productOrderProduce(request)
                .compose(Scheduler.apply())
                .subscribe(new Subs<Object>() {

                    @Override
                    public void onSuccess(Object data, int total, int length) {
                        ProductOrderOperateState result = new ProductOrderOperateState(DO_ORDER_TYPE_PRODUCE, true);
                        goodsOrderOperateSuccess.postValue(result);
                    }
                });
    }

    /**
     * 接单
     */
    public void takeProductOrder(String orderId, String productId) {
        ProduceOrderOperate request = new ProduceOrderOperate();
        request.setOrderId(orderId);
        request.setProductId(productId);
        Network.api().productOrderTake(request)
                .compose(Scheduler.apply())
                .subscribe(new Subs<Object>() {

                    @Override
                    public void onSuccess(Object data, int total, int length) {
                        ProductOrderOperateState result = new ProductOrderOperateState(DO_ORDER_TYPE_TAKE, true);
                        goodsOrderOperateSuccess.postValue(result);
                    }
                });
    }

    /**
     * 完成
     */
    public void completeProductOrder(String orderId, String productId) {
        ProduceOrderOperate request = new ProduceOrderOperate();
        request.setOrderId(orderId);
        request.setProductId(productId);
        Network.api().productOrderComplete(request)
                .compose(Scheduler.apply())
                .subscribe(new Subs<Object>() {

                    @Override
                    public void onSuccess(Object data, int total, int length) {
                        ProductOrderOperateState result = new ProductOrderOperateState(DO_ORDER_TYPE_COMPLETE, true);
                        goodsOrderOperateSuccess.postValue(result);
                    }
                });
    }
}
