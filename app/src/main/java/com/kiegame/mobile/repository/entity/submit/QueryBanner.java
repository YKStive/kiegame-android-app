package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var.
 * Created date: 2020/4/14.
 * Description: 轮播图请求对象
 */
public class QueryBanner {

    // 轮播图位置 1:PC端 2:移动端
    private Integer bannerType;

    public Integer getBannerType() {
        return bannerType;
    }

    public void setBannerType(Integer bannerType) {
        this.bannerType = bannerType;
    }
}
