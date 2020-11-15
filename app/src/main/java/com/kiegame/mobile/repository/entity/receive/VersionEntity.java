package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var.
 * Created date: 2020/3/30.
 * Description: 版本更新数据对象
 */
public class VersionEntity {
    // 版本ID
    private String versionId;
    // 版本编号
    private String appCode;
    // 更新说明
    private String appName;
    // 平台类型 1:安卓 2:苹果
    private String appType;
    // 是否强制更新 1:强制 其他:不强制
    private String isMustUpdate;
    // 下载地址
    private String downloadUrl;

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getIsMustUpdate() {
        return isMustUpdate;
    }

    public void setIsMustUpdate(String isMustUpdate) {
        this.isMustUpdate = isMustUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
