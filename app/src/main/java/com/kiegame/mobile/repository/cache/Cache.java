package com.kiegame.mobile.repository.cache;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.submit.BuyShop;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.Prefer;
import com.kiegame.mobile.utils.PreferPlus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/28.
 * Description: 内存缓存
 */
public class Cache extends BaseObservable {

    private static Cache INS;
    // 用户登录信息
    private LoginEntity login;
    // token
    private String token;
    // 网费
    private MutableLiveData<Integer> netFee;
    // 商品
    private MutableLiveData<List<BuyShop>> shops;
    // 费用合计
    private int paymentMoney;
    // 用户上机信息
    private String userName;
    // 在线支付
    private boolean paymentOnline;
    // 客维支付
    private boolean paymentOffline;

    private Cache() {
        this.token = Prefer.get(Setting.APP_NETWORK_TOKEN, "");
        this.login = PreferPlus.get(Setting.USER_LOGIN_OBJECT, LoginEntity.class);
        this.netFee = new MutableLiveData<>();
        this.netFee.postValue(0);
        this.shops = new MutableLiveData<>();
        this.paymentMoney = 0;
        this.userName = "没有选择会员";
        this.paymentOnline = true;
        this.paymentOffline = false;
    }

    /**
     * 获取 {@link Cache} 对象
     *
     * @return {@link Cache}
     */
    public static Cache ins() {
        if (Cache.INS == null) {
            Cache.INS = new Cache();
        }
        return Cache.INS;
    }

    /**
     * 是否在线支付
     */
    @Bindable
    public boolean isPaymentOnline() {
        return paymentOnline;
    }

    /**
     * 设置在线支付
     */
    public void setPaymentOnline(boolean paymentOnline) {
        this.paymentOnline = paymentOnline;
        this.notifyChange();
    }

    /**
     * 是否客维支付
     */
    @Bindable
    public boolean isPaymentOffline() {
        return paymentOffline;
    }

    /**
     * 设置客维支付
     */
    public void setPaymentOffline(boolean paymentOffline) {
        this.paymentOffline = paymentOffline;
        this.notifyChange();
    }

    /**
     * 获取用户上机信息
     */
    @Bindable
    public String getUserName() {
        return this.userName;
    }

    /**
     * 设置用户上机信息
     */
    public void setUserName(String userName) {
        this.userName = userName;
        this.notifyChange();
    }

    /**
     * 获取支付金额合计
     *
     * @return 返回支付金额合计, 单位: 元
     */
    @Bindable
    public String getPaymentMoney() {
        return cal(paymentMoney);
    }

    /**
     * 设置支付金额合计,单位: 分
     *
     * @param paymentMoney 支付金额,单位: 分
     */
    public void setPaymentMoney(int paymentMoney) {
        this.paymentMoney = paymentMoney;
        this.notifyChange();
    }

    /**
     * 更新支付结算金额
     */
    private void updateTotalMoney() {
        Integer value = this.netFee.getValue();
        int money = value == null ? 0 : value;
        List<BuyShop> shops = this.shops.getValue();
        if (shops != null) {
            for (BuyShop shop : shops) {
                money += shop.getFee();
            }
        }
        this.paymentMoney = money;
    }

    /**
     * 获取网费
     *
     * @return 返回网费字符串, 单位:元
     */
    @Bindable
    public String getNetFee() {
        Integer value = netFee.getValue();
        return this.cal(value == null ? 0 : value);
    }

    /**
     * 设置网费
     *
     * @param netFee 网费金额,单位:分
     */
    public void setNetFee(Integer netFee) {
        this.netFee.setValue(netFee);
        this.updateTotalMoney();
        this.notifyChange();
    }

    /**
     * 获取商品列表
     *
     * @return {@link List<BuyShop>}
     */
    @Bindable
    public List<BuyShop> getShops() {
        return this.shops.getValue();
    }

    /**
     * 设置商品数据列表
     *
     * @param shops {@link List<BuyShop>}
     */
    public void setShops(List<BuyShop> shops) {
        this.shops.setValue(shops);
        this.updateTotalMoney();
        this.notifyChange();
    }

    /**
     * 获取商品金额总计
     *
     * @return 返回列表中所有的商品金额总价, 单位: 元
     */
    @Bindable
    public String getShopMoneyTotal() {
        int total = 0;
        List<BuyShop> value = shops.getValue();
        if (value != null) {
            for (BuyShop shop : value) {
                if (shop != null) {
                    total += shop.getFee();
                }
            }
        }
        return cal(total);
    }

    /**
     * 添加商品到商品列表
     *
     * @param shop {@link BuyShop} 商品数据对象
     */
    public void attachShop(BuyShop shop) {
        List<BuyShop> value = this.shops.getValue();
        if (value == null) {
            value = new ArrayList<>();
        }
        value.add(shop);
        this.shops.setValue(value);
        this.updateTotalMoney();
        this.notifyChange();
    }

    /**
     * 获取用户登录Token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * 保存用户登录的Token
     */
    public void setToken(String token) {
        this.token = token;
        Prefer.put(Setting.APP_NETWORK_TOKEN, token);
    }

    /**
     * 获取用户登录数据对象
     *
     * @return {@link LoginEntity}
     */
    public LoginEntity getLoginInfo() {
        return this.login;
    }

    /**
     * 保存用户登录的数据对象
     *
     * @param login {@link LoginEntity}
     */
    public void setLoginInfo(LoginEntity login) {
        this.login = login;
        PreferPlus.put(Setting.USER_LOGIN_OBJECT, login);
    }

    /**
     * 计算金额,分转换位元,保留两位
     *
     * @param money 金额, 分
     * @return 转换后的金额, 元
     */
    private String cal(int money) {
        BigDecimal source = new BigDecimal(money);
        BigDecimal ratio = new BigDecimal(100);
        BigDecimal divide = source.divide(ratio, 2, RoundingMode.HALF_UP);
        return divide.toString();
    }
}
