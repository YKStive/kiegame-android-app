package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/1/28.
 * Description: 充值活动返回
 */
public class ActivityEntity {

    private int activityId;
    private String activityName;
    private String activityImg;
    private int activityTarget;
    private int activityType;
    private String activityRule;
    private String customerId;
    private String customerName;
    private String serviceId;
    private String serviceName;
    // 卡卷状态(1已生成 2 未使用 3已使用)
    private Integer cardState;
    private String activityCardResultId;
    private String startTime;
    private String endTime;
    private Integer timingType;
    private Integer timingStart;
    private Integer timingEnd;
    private String timingStartTime;
    private String timingEndTime;
    private Integer giveType;
    private String giveRule;
    private String cardDesc;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    public int getActivityTarget() {
        return activityTarget;
    }

    public void setActivityTarget(int activityTarget) {
        this.activityTarget = activityTarget;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(String activityRule) {
        this.activityRule = activityRule;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getCardState() {
        return cardState;
    }

    public void setCardState(Integer cardState) {
        this.cardState = cardState;
    }

    public String getActivityCardResultId() {
        return activityCardResultId;
    }

    public void setActivityCardResultId(String activityCardResultId) {
        this.activityCardResultId = activityCardResultId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTimingType() {
        return timingType;
    }

    public void setTimingType(Integer timingType) {
        this.timingType = timingType;
    }

    public Integer getTimingStart() {
        return timingStart;
    }

    public void setTimingStart(Integer timingStart) {
        this.timingStart = timingStart;
    }

    public Integer getTimingEnd() {
        return timingEnd;
    }

    public void setTimingEnd(Integer timingEnd) {
        this.timingEnd = timingEnd;
    }

    public String getTimingStartTime() {
        return timingStartTime;
    }

    public void setTimingStartTime(String timingStartTime) {
        this.timingStartTime = timingStartTime;
    }

    public String getTimingEndTime() {
        return timingEndTime;
    }

    public void setTimingEndTime(String timingEndTime) {
        this.timingEndTime = timingEndTime;
    }

    public Integer getGiveType() {
        return giveType;
    }

    public void setGiveType(Integer giveType) {
        this.giveType = giveType;
    }

    public String getGiveRule() {
        return giveRule;
    }

    public void setGiveRule(String giveRule) {
        this.giveRule = giveRule;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }
}
