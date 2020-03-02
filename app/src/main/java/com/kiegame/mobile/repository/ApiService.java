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
import com.kiegame.mobile.repository.entity.result.Result;
import com.kiegame.mobile.repository.entity.submit.AddOrder;
import com.kiegame.mobile.repository.entity.submit.CancelOrder;
import com.kiegame.mobile.repository.entity.submit.DeleteOrder;
import com.kiegame.mobile.repository.entity.submit.UserLogin;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by: var_rain.
 * Created date: 2018/10/21.
 * Description: 服务器接口映射
 */
public interface ApiService {

    // 服务器地址
    String BASE_URL = BuildConfig.SERVER_BASE;
    // 超时 (秒)
    int TIME_OUT = 15;

    /**
     * 用户登录
     */
    @POST("app/login/userLogin")
    Observable<Result<List<LoginEntity>>> userLogin(@Body UserLogin body);

    /**
     * 查询会员信息
     */
    @GET("app/buyOrder/listCustomerBase")
    Observable<Result<List<UserInfoEntity>>> queryUserInfos(
            // 机位号/身份证后4位/姓名
            @Query("param") String param
    );

    /**
     * 查询Banner图
     */
    @GET("app/bannerManage/queryBannerList")
    Observable<Result<List<BannerEntity>>> queryBannerList(
            // 轮播图位置 1:PC端 2:移动端
            @Query("bannerType") Integer bannerType
    );

    /**
     * 下订单/结算
     */
    @POST("app/buyOrder/addOrder")
    Observable<Result<List<AddOrderEntity>>> addOrder(@Body AddOrder body);

    /**
     * 售卖商品分类查询
     */
    @GET("app/typeBase/listTypeBase")
    Observable<Result<List<ShopSortEntity>>> listType();

    /**
     * 售卖商品标签查询
     */
    @GET("app/tagBase/listTagBase")
    Observable<Result<List<ShopSortEntity>>> listTag(
            @Query("tagType") Integer tagType
    );

    /**
     * 查询售卖商品
     */
    @GET("app/serviceProduct/listServiceBuyProduct")
    Observable<Result<List<ShopEntity>>> queryShops(
            // 门店ID
            @Query("serviceId") String serviceId,
            // 分类ID
            @Query("productTypeId") String productTypeId,
            // 名称
            @Query("productName") String productName,
            // 标签ID
            @Query("productTagId") String productTagId,
            // *是否移动端 (固定 1)
            @Query("isMobile") Integer isMobile
    );

    /**
     * 查询门店订单
     */
    @GET("app/buyOrder/listBuyOrder")
    Observable<Result<List<BuyOrderEntity>>> listBuyOrder(
            // *门店ID
            @Query("serviceId") String serviceId,
            // 支付状态 1待支付 2支付成功 3支付中 4支付失败
            @Query("payTypeState") Integer payTypeState,
            // 开始时间 (yyyy-MM-dd HH:mm:ss)
            @Query("startTime") String startTime,
            // 结束时间
            @Query("endTime") String endTime,
            // *固定 2
            @Query("payChannel") Integer payChannel,
            // 支付类型
            @Query("payType") Integer payType,
            // 机位号
            @Query("seatNumber") String seatNumber,
            // 会员名
            @Query("customerName") String customerName,
            // *当前登录员工ID
            @Query("empId") String empId
    );

    /**
     * 订单支付
     */
    @POST("app/buyOrder/updateOrderPay")
    Observable<Result<List<AddOrderEntity>>> updateOrderPay(@Body AddOrder body);

    /**
     * 门店活动查询
     */
    @GET("app/activity/listActivityByService")
    Observable<Result<List<ActivityEntity>>> listActivityByService(
            // *门店ID
            @Query("serviceId") String serviceId,
            // 产品ID
            @Query("productId") String productId
    );

    /**
     * 门店产品活动查询
     */
    @GET("app/activityCard/listActivityCardResultByTime")
    Observable<Result<List<ActivityEntity>>> listActivityCardResultByTime(
            // *会员ID
            @Query("customerId") String customerId,
            // 门店ID
            @Query("serviceId") String serviceId,
            // 产品ID
            @Query("productId") String productId
    );

    /**
     * 取消订单
     */
    @POST("app/buyOrder/deletePayOrder")
    Observable<Result<Object>> cancelOrder(@Body CancelOrder body);

    /**
     * 删除订单
     */
    @POST("app/buyOrder/deleteOrderBase")
    Observable<Result<Object>> deleteOrder(@Body DeleteOrder body);

    /**
     * 查询在线支付结果
     */
    @GET("app/buyOrder/swiftPayResultByOutOrderNo")
    Observable<Result<List<PayResultEntity>>> payResult(
            // 支付返回的单号,在线支付时需要
            @Query("paymentPayId") String paymentPayId,
            // 退款时需要
            @Query("orderBaseId") String orderBaseId
    );
}
