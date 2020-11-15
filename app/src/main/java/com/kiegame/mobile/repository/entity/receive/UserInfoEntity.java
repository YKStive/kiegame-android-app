package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/1/16.
 * Description: 用户信息
 */
public class UserInfoEntity {

    /**
     * customerType : 1
     * gender : 2
     * phone : 18223193517
     * bonusBalance : 2000
     * idCard : 51382319961202003X
     * customerId : 3563317457976320
     * accountBalance : 10000
     * customerLevel : 普通会员
     * customerName : 陈翔宇
     */

    private int customerType;
    private int gender;
    private String phone;
    private int bonusBalance;
    private String idCard;
    private String customerId;
    private int accountBalance;
    private String customerLevel;
    private String customerName;
    private String seatNumber;

    public UserInfoEntity() {
    }

    public UserInfoEntity(String customerName) {
        this.customerName = customerName;
        this.bonusBalance = 0;
        this.idCard = "";
        this.customerId = "";
        this.accountBalance = 0;
        this.seatNumber = "";
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBonusBalance() {
        return bonusBalance;
    }

    public void setBonusBalance(int bonusBalance) {
        this.bonusBalance = bonusBalance;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
