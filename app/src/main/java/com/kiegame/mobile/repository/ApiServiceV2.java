package com.kiegame.mobile.repository;

import com.kiegame.mobile.BuildConfig;
import com.kiegame.mobile.repository.entity.receive.ActivityEntity;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.BannerEntity;
import com.kiegame.mobile.repository.entity.receive.BuyOrderEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.PayResultEntity;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.ShopSortEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.repository.entity.receive.VersionEntity;
import com.kiegame.mobile.repository.entity.result.Result;
import com.kiegame.mobile.repository.entity.submit.AddOrder;
import com.kiegame.mobile.repository.entity.submit.CancelOrder;
import com.kiegame.mobile.repository.entity.submit.DeleteOrder;
import com.kiegame.mobile.repository.entity.submit.ListActivityByService;
import com.kiegame.mobile.repository.entity.submit.ListActivityCardResultByTime;
import com.kiegame.mobile.repository.entity.submit.ListBuyOrder;
import com.kiegame.mobile.repository.entity.submit.ListShopTag;
import com.kiegame.mobile.repository.entity.submit.PayResult;
import com.kiegame.mobile.repository.entity.submit.QueryAppVersion;
import com.kiegame.mobile.repository.entity.submit.QueryBanner;
import com.kiegame.mobile.repository.entity.submit.QueryProductStock;
import com.kiegame.mobile.repository.entity.submit.QueryShops;
import com.kiegame.mobile.repository.entity.submit.UserInfo;
import com.kiegame.mobile.repository.entity.submit.UserLogin;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by: var_rain.
 * Created date: 2020/04/14.
 * Description: 服务器接口映射V2
 */
public interface ApiServiceV2 {

    // 服务器地址
    String BASE_URL = BuildConfig.SERVER_BASE;
    // 超时 (秒)
    int TIME_OUT = 15;

    /**
     * 用户登录
     */
    @POST("app/v2/login/userLogin")
    Observable<Result<List<LoginEntity>>> userLogin(@Body UserLogin body);

    /**
     * 查询会员信息
     */
    @POST("app/v2/buyOrder/listCustomerBase")
    Observable<Result<List<UserInfoEntity>>> queryUserInfos(@Body UserInfo body);

    /**
     * 查询Banner图
     */
    @POST("app/v2/bannerManage/queryBannerList")
    Observable<Result<List<BannerEntity>>> queryBannerList(@Body QueryBanner body);

    /**
     * 下订单/结算
     */
    @POST("app/v2/buyOrder/addOrder")
    Observable<Result<List<AddOrderEntity>>> addOrder(@Body AddOrder body);

    /**
     * 售卖商品分类查询
     */
    @POST("app/v2/typeBase/listTypeBase")
    Observable<Result<List<ShopSortEntity>>> listType(@Body Object body);

    /**
     * 售卖商品标签查询
     */
    @POST("app/v2/tagBase/listTagBase")
    Observable<Result<List<ShopSortEntity>>> listTag(@Body ListShopTag body);

    /**
     * 查询售卖商品
     */
    @POST("app/v2/serviceProduct/listServiceBuyProduct")
    Observable<Result<List<ShopEntity>>> queryShops(@Body QueryShops body);

    /**
     * 查询门店订单
     */
    @POST("app/v2/buyOrder/listBuyOrder")
    Observable<Result<List<BuyOrderEntity>>> listBuyOrder(@Body ListBuyOrder body);

    /**
     * 订单支付
     */
    @POST("app/v2/buyOrder/updateOrderPay")
    Observable<Result<List<AddOrderEntity>>> updateOrderPay(@Body AddOrder body);

    /**
     * 门店活动查询
     */
    @POST("app/v2/activity/listActivityByService")
    Observable<Result<List<ActivityEntity>>> listActivityByService(@Body ListActivityByService body);

    /**
     * 门店产品活动查询
     */
    @POST("app/v2/activityCard/listActivityCardResultByTime")
    Observable<Result<List<ActivityEntity>>> listActivityCardResultByTime(@Body ListActivityCardResultByTime body);

    /**
     * 取消订单
     */
    @POST("app/v2/buyOrder/deletePayOrder")
    Observable<Result<Object>> cancelOrder(@Body CancelOrder body);

    /**
     * 删除订单
     */
    @POST("app/v2/buyOrder/deleteOrderBase")
    Observable<Result<Object>> deleteOrder(@Body DeleteOrder body);

    /**
     * 查询在线支付结果
     */
    @POST("app/v2/buyOrder/swiftPayResultByOutOrderNo")
    Observable<Result<List<PayResultEntity>>> payResult(@Body PayResult body);

    /**
     * 检查版本更新
     */
    @POST("app/v2/appVersion/queryAppVersion")
    Observable<Result<List<VersionEntity>>> queryAppVersion(@Body QueryAppVersion body);

    /**
     * 查询商品库存是否足够
     */
    @POST("/app/v2/serviceProduct/checkProductStock")
    Observable<Result<Object>> queryProductStock(@Body QueryProductStock body);
}
