package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/1/16.
 * Description: banner图查询接口
 */
public class BannerEntity {

    /**
     * bannerId : 1
     * bannerName : pc端首页
     * bannerUrl : http://kie-rt-test.oss-cn-hangzhou.aliyuncs.com/kie-rt-test/2.jpg
     * bannerType : 1
     * sort : 1
     * isShow : 1
     * createDate : 2020-01-10 13:39:57
     */

    private String bannerId;
    private String bannerName;
    private String bannerUrl;
    private int bannerType;
    private int sort;
    private int isShow;
    private String createDate;

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public int getBannerType() {
        return bannerType;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
