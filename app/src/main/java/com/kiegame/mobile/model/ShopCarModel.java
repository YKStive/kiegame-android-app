package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.PayResultEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.repository.entity.submit.AddOrder;
import com.kiegame.mobile.repository.entity.submit.BuyShop;
import com.kiegame.mobile.repository.entity.submit.PayResult;
import com.kiegame.mobile.repository.entity.submit.UserInfo;
import com.kiegame.mobile.utils.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 确认订单
 */
public class ShopCarModel extends ViewModel {

    public MutableLiveData<String> searchName;
    public MutableLiveData<String> userName;
    public MutableLiveData<String> memo;

    private LoginEntity login;

    private MutableLiveData<List<UserInfoEntity>> userInfos;
    private MutableLiveData<List<AddOrderEntity>> addOrder;
    private MutableLiveData<List<PayResultEntity>> payResult;
    private MutableLiveData<String> failMessage;

    public ShopCarModel() {
        this.searchName = new MutableLiveData<>();
        this.userName = new MutableLiveData<>();
        this.memo = new MutableLiveData<>();

        this.login = Cache.ins().getLoginInfo();

        this.userInfos = new MutableLiveData<>();
        this.addOrder = new MutableLiveData<>();
        this.payResult = new MutableLiveData<>();
        this.failMessage = new MutableLiveData<>();

        this.initialize();
    }

    /**
     * 初始化数据
     */
    private void initialize() {
        this.searchName.setValue("");
        this.userName.setValue("没有选择会员");
        this.memo.setValue("");
    }

    /**
     * 获取错误信息
     */
    public LiveData<String> getFailMessage() {
        return failMessage;
    }

    /**
     * 查询用户信息
     */
    public LiveData<List<UserInfoEntity>> searchUserInfos(String keywords) {
        UserInfo info = new UserInfo();
        info.setParam(keywords);
        Network.api().queryUserInfos(info)
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
     * @param buyPayPassword 支付密码
     * @param isAddOrder     1: 下单 2:结算
     */
    public LiveData<List<AddOrderEntity>> addOrder(int netMoney, int shopMoney, String seatNum, String customerId, int rewardMoney, Integer discountType, String discountId, Integer discountMoney, int buyPayType, String paidInAmount, String buyPayPassword, int isAddOrder) {
        AddOrder order = new AddOrder();
        if (shopMoney > 0 || Cache.ins().getProductCoupon() != null) {
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
        if (netMoney > 0 || Cache.ins().getNetFeeCoupon() != null) {
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
        } else {
            order.setRechargeMoney(null);
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
        order.setMemo(Text.empty(memo.getValue()) ? null : memo.getValue());
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

    /**
     * 查询支付结果
     */
    public LiveData<List<PayResultEntity>> queryPayState(String payId) {
        PayResult info = new PayResult();
        info.setPaymentPayId(payId);
        info.setOrderBaseId(null);
        Network.api().payResult(info)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<PayResultEntity>>() {
                    @Override
                    public void onSuccess(List<PayResultEntity> data, int total, int length) {
                        payResult.setValue(data);
                    }
                });
        return this.payResult;
    }
}
