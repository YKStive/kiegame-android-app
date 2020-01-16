package com.kiegame.mobile.repository;

import com.kiegame.mobile.BuildConfig;
import com.kiegame.mobile.repository.entity.receive.BannerEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.result.Result;
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
    @POST("/app/login/userLogin")
    Observable<Result<List<LoginEntity>>> userLogin(@Body UserLogin body);

    /**
     * 查询会员信息
     */
    @GET("app/buyOrder/listCustomerBase")
    Observable<Result<List<Object>>> queryUserInfos(
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
}
