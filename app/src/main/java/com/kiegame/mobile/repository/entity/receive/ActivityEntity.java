package com.kiegame.mobile.repository.entity.receive;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by: var_rain.
 * Created date: 2020/1/28.
 * Description: 活动/卡券返回数据对象
 */
public class ActivityEntity {

    private String activityId;
    private String activityName;
    private String activityImg;
    private Integer activityTarget;
    private Integer activityType;
    private String activityRule;
    private String customerId;
    private String customerName;
    private String serviceId;
    private String serviceName;
    private String productId;
    // 卡卷状态(1已生成 2 未使用 3已使用)
    private Integer cardState;
    // 1 活动 2 卡券
    private Integer discountType;
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
    private String useScopeServiceId;
    private String useScopeServiceName;
    // 1 可使用 2 不可使用
    private Integer isBoolUse;

    public Integer getIsBoolUse() {
        return isBoolUse;
    }

    public void setIsBoolUse(Integer isBoolUse) {
        this.isBoolUse = isBoolUse;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
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

    public Integer getActivityTarget() {
        return activityTarget;
    }

    public void setActivityTarget(Integer activityTarget) {
        this.activityTarget = activityTarget;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getUseScopeServiceId() {
        return useScopeServiceId;
    }

    public void setUseScopeServiceId(String useScopeServiceId) {
        this.useScopeServiceId = useScopeServiceId;
    }

    public String getUseScopeServiceName() {
        return useScopeServiceName;
    }

    public void setUseScopeServiceName(String useScopeServiceName) {
        this.useScopeServiceName = useScopeServiceName;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    /**
     * 获取充送优惠券的充送上限
     *
     * @return 返回金额 分
     */
    public int getActivityMoneyMax() {
        if (this.activityRule != null) {
            String[] split = this.activityRule.split("#");
            if (split.length >= 2) {
                int money = Integer.parseInt(split[0]);
                BigDecimal source = new BigDecimal(money);
                BigDecimal ratio = new BigDecimal("100");
                return source.multiply(ratio).intValue();
            }
        }
        return 0;
    }

    /**
     * 获取充送优惠金额
     *
     * @return 返回金额的字符串形式
     */
    public String getActivityMoney() {
        if (this.activityRule != null) {
            String[] split = this.activityRule.split("#");
            if (split.length >= 2) {
                return split[1];
            }
        }
        return this.activityRule;
    }

    /**
     * 获取优惠折扣率
     *
     * @return 返回 0-1 {@link BigDecimal}
     */
    public BigDecimal getActivityRatio() {
        if (this.activityRule != null) {
            if (!this.activityRule.equals("0")) {
                BigDecimal decimal = new BigDecimal(this.activityRule);
                BigDecimal ratio = new BigDecimal("100");
                return decimal.divide(ratio, 2, RoundingMode.HALF_UP);
            }
        }
        return new BigDecimal("0");
    }

    /**
     * 获取充值折扣率字符串
     *
     * @return 返回 x折 的形式
     */
    public String getActivityRatioString() {
        if (this.activityRule != null) {
            if (this.activityRule.equals("0")) {
                return "100%折扣";
            } else {
                String rpl = this.activityRule.replace("0", "");
                return String.format("%s折", rpl);
            }
        }
        return "折扣";
    }

    @NotNull
    @Override
    public String toString() {
        return "ActivityEntity{" +
                "activityId='" + activityId + '\'' +
                ", activityName='" + activityName + '\'' +
                ", activityImg='" + activityImg + '\'' +
                ", activityTarget=" + activityTarget +
                ", activityType=" + activityType +
                ", activityRule='" + activityRule + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", productId='" + productId + '\'' +
                ", cardState=" + cardState +
                ", activityCardResultId='" + activityCardResultId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", timingType=" + timingType +
                ", timingStart=" + timingStart +
                ", timingEnd=" + timingEnd +
                ", timingStartTime='" + timingStartTime + '\'' +
                ", timingEndTime='" + timingEndTime + '\'' +
                ", giveType=" + giveType +
                ", giveRule='" + giveRule + '\'' +
                ", cardDesc='" + cardDesc + '\'' +
                ", useScopeServiceId='" + useScopeServiceId + '\'' +
                ", useScopeServiceName='" + useScopeServiceName + '\'' +
                '}';
    }
}
