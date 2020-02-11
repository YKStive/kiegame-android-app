package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.BannerEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.repository.entity.submit.AddOrder;
import com.kiegame.mobile.repository.entity.submit.BuyShop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/14.
 * Description: 网费
 */
public class NetFeeModel extends ViewModel {

    public MutableLiveData<String> userSearch;
    public MutableLiveData<String> amount;
    public MutableLiveData<String> award;
    public MutableLiveData<String> gabon;
    public MutableLiveData<String> bonus;
    public MutableLiveData<String> recharge;

    public LoginEntity login;

    private MutableLiveData<List<BannerEntity>> banner;
    private MutableLiveData<List<UserInfoEntity>> userInfos;
    private MutableLiveData<List<AddOrderEntity>> addOrder;
    private MutableLiveData<String> failMessage;

    public NetFeeModel() {
        this.login = Cache.ins().getLoginInfo();

        this.userSearch = new MutableLiveData<>();
        this.amount = new MutableLiveData<>();
        this.award = new MutableLiveData<>();
        this.gabon = new MutableLiveData<>();
        this.bonus = new MutableLiveData<>();
        this.recharge = new MutableLiveData<>();

        this.banner = new MutableLiveData<>();
        this.userInfos = new MutableLiveData<>();
        this.addOrder = new MutableLiveData<>();
        this.failMessage = new MutableLiveData<>();

        this.initData();
    }

    /**
     * 初始化用户数据
     */
    private void initData() {
        this.amount.setValue("0");
        this.award.setValue("0");
        this.gabon.setValue("0");
        this.bonus.setValue("0");
        this.recharge.setValue("0.00");
    }

    /**
     * 重置数据
     */
    public void resetData() {
        this.initData();
    }

    /**
     * 获取错误信息
     */
    public LiveData<String> getFailMessage() {
        return failMessage;
    }

    /**
     * 查询banner图
     */
    public LiveData<List<BannerEntity>> queryBanner() {
        Network.api().queryBannerList(2)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<BannerEntity>>(false) {
                    @Override
                    public void onSuccess(List<BannerEntity> data, int total, int length) {
                        banner.setValue(data);
                    }
                });
        return this.banner;
    }

    /**
     * 查询用户信息
     */
    public LiveData<List<UserInfoEntity>> searchUserInfos(String keywords) {
        Network.api().queryUserInfos(keywords)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<UserInfoEntity>>(false) {
                    @Override
                    public void onSuccess(List<UserInfoEntity> data, int total, int length) {
                        userInfos.setValue(data);
                    }
                });
        return this.userInfos;
    }

    /**
     * 下单/结算
     *
     * @param netMoney       网费充值金额
     * @param shopMoney      商品金额
     * @param seatNum        座号
     * @param customerId     会员ID
     * @param rewardMoney    网费充值奖励金,没有为0
     * @param discountType   充值优惠类型
     * @param discountId     充值优惠ID
     * @param discountMoney  充值优惠金额
     * @param buyPayType     支付类型
     * @param paidInAmount   实际支付
     * @param memo           备注
     * @param buyPayPassword 支付密码
     * @param isAddOrder     1: 下单 2:结算
     */
    public LiveData<List<AddOrderEntity>> addOrder(int netMoney, int shopMoney, String seatNum, String customerId, int rewardMoney, Integer discountType, String discountId, Integer discountMoney, int buyPayType, String paidInAmount, String memo, String buyPayPassword, int isAddOrder) {
        AddOrder order = new AddOrder();
        if (shopMoney > 0) {
            // 购买商品
            List<BuyShop> shops = Cache.ins().getShops();
            List<BuyShop> buys = new ArrayList<>();
            for (BuyShop shop : shops) {
                if (shop != null && shop.isBuy()) {
                    buys.add(shop);
                }
            }
            order.setProductList(buys);
        }
        if (netMoney > 0) {
            // 充值
            order.setRechargeMoney(netMoney);
            order.setRechargeRewardMoney(rewardMoney);
            if (discountType != null) {
                order.setRechargeDiscountType(discountType);
            }
            if (discountId != null) {
                order.setRechargeDiscountId(discountId);
            }
            if (discountMoney != null) {
                order.setRechargeDiscountMoney(discountMoney);
            }
        }
        order.setServiceId(login.getServiceId());
        order.setCustomerId(customerId);
        order.setSeatNumber(seatNum);
        order.setIsAddOrder(isAddOrder);
        order.setIsSurfacePos(2);
        order.setBuyPayType(buyPayType);
        order.setBuyPayPassword(buyPayPassword);
        order.setBuyPayChannel(2);
        order.setCommissionId(login.getEmpId());
        order.setPaidInAmount(paidInAmount);
        order.setMemo(memo);
        Network.api().addOrder(order)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<AddOrderEntity>>(true, true) {
                    @Override
                    public void onSuccess(List<AddOrderEntity> data, int total, int length) {
                        addOrder.setValue(data);
                    }

                    @Override
                    public void onPayFailure(String msg) {
                        failMessage.setValue(msg);
                    }
                });
        return this.addOrder;
    }
}
