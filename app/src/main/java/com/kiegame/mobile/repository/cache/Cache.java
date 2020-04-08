package com.kiegame.mobile.repository.cache;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.kiegame.mobile.consts.Payment;
import com.kiegame.mobile.repository.entity.receive.ActivityEntity;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.repository.entity.submit.BuyShop;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.Prefer;
import com.kiegame.mobile.utils.PreferPlus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: var_rain.
 * Created date: 2020/1/28.
 * Description: 内存缓存
 */
public class Cache extends BaseObservable {

    private static Cache INS;
    // 用户登录信息
    private LoginEntity login;
    // 会员信息
    private UserInfoEntity userInfo;
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
    // 支付类型
    private int payment;
    // 商品数量
    private MutableLiveData<Integer> shopSum;
    // 商品列表更新
    private MutableLiveData<Integer> shopObserver;
    // 网费充值更新
    private MutableLiveData<Integer> netFeeObserver;
    // 订单列表更新
    private MutableLiveData<Integer> orderObserver;
    // 首页页面更新
    private MutableLiveData<Integer> mainPageObserver;
    // 网费优惠
    private ActivityEntity netFeeCoupon;
    // 商品优惠
    private ActivityEntity productCoupon;
    // 产品优惠金额
    private int productCouponMoney;
    // 记录商品的购买数量
    private Map<String, Integer> buySum;

    private Cache() {
        this.buySum = new HashMap<>();
        this.shopSum = new MutableLiveData<>();
        this.shopObserver = new MutableLiveData<>();
        this.netFeeObserver = new MutableLiveData<>();
        this.orderObserver = new MutableLiveData<>();
        this.mainPageObserver = new MutableLiveData<>();
        this.netFee = new MutableLiveData<>();
        this.shops = new MutableLiveData<>();
        this.initialize();
    }

    /**
     * 初始化
     */
    public void initialize() {
        this.token = Prefer.get(Setting.APP_NETWORK_TOKEN, "");
        this.login = PreferPlus.get(Setting.USER_LOGIN_OBJECT, LoginEntity.class);
        this.netFee.postValue(0);
        this.paymentMoney = 0;
        this.userName = "没有选择会员";
        this.payment = Payment.PAY_TYPE_ONLINE;
        this.shopSum.postValue(0);
        this.setNetFeeCoupon(null);
        this.setProductCoupon(null);
        this.productCouponMoney = 0;
        this.setUserInfo(null);
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
     * 获取网费优惠活动
     *
     * @return {@link ActivityEntity}
     */
    @Bindable
    public ActivityEntity getNetFeeCoupon() {
        return netFeeCoupon;
    }

    /**
     * 设置网费优惠活动对象
     *
     * @param netFeeCoupon {@link ActivityEntity}
     */
    public void setNetFeeCoupon(ActivityEntity netFeeCoupon) {
        this.netFeeCoupon = netFeeCoupon;
        this.notifyChange();
    }

    /**
     * 获取商品优惠券
     *
     * @return {@link ActivityEntity}
     */
    @Bindable
    public ActivityEntity getProductCoupon() {
        return productCoupon;
    }

    /**
     * 是否有这种商品
     *
     * @param productId 商品ID
     * @return true: 有 false: 没有
     */
    public boolean hasShop(String productId) {
        List<BuyShop> value = shops.getValue();
        if (value != null) {
            for (BuyShop buy : value) {
                if (buy.getProductId().equals(productId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置商品优惠券数据
     *
     * @param productCoupon {@link ActivityEntity}
     */
    public void setProductCoupon(ActivityEntity productCoupon) {
        this.productCoupon = productCoupon;
        List<BuyShop> buys = shops.getValue();
        if (buys != null) {
            int money = 0;
            for (BuyShop buy : buys) {
                buy.setProductDiscountId(null);
                buy.setProductDiscountType(null);
                if (productCoupon != null && money == 0) {
                    if (buy.getProductId().equals(productCoupon.getProductId()) && buy.isBuy()) {
                        buy.setProductDiscountType(productCoupon.getDiscountType());
                        buy.setProductDiscountId(productCoupon.getDiscountType() == 1 ? productCoupon.getActivityId() : productCoupon.getActivityCardResultId());
                        money = buy.getFee() - (new BigDecimal(buy.getFee()).multiply(productCoupon.getActivityRatio()).intValue());
                    }
                }
            }
            productCouponMoney = money;
            shops.setValue(buys);
            updateTotalMoney();
        }
        this.notifyChange();
    }

    /**
     * 获取订单列表更新
     */
    public MutableLiveData<Integer> getOrderObserver() {
        return orderObserver;
    }

    /**
     * 获取商品列表更新
     */
    public MutableLiveData<Integer> getShopObserver() {
        return shopObserver;
    }

    /**
     * 获取网费信息更新
     */
    public MutableLiveData<Integer> getNetFeeObserver() {
        return netFeeObserver;
    }

    /**
     * 获取首页页面更新
     */
    public MutableLiveData<Integer> getMainPageObserver() {
        return mainPageObserver;
    }

    /**
     * 根据商品ID获取商品数量
     *
     * @param productId 商品ID
     * @return 返回数量
     */
    public int getShopSumById(String productId) {
        Integer src = buySum.get(productId);
        return src == null ? 0 : src;
    }

    /**
     * 根据商品ID设置商品数量
     *
     * @param productId 商品ID
     * @param sum       数量
     */
    public void setShopSumById(String productId, int sum) {
        if (sum == 0) {
            buySum.remove(productId);
        } else {
            buySum.put(productId, sum);
        }
    }

    /**
     * 设置商品数量改变监听
     *
     * @param owner    生命周期观察者
     * @param observer 观察者回调
     */
    public void setOnShopSumChangeListener(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<BuyShop>> observer) {
//        this.shopSum.observe(owner, observer);
        this.shops.observe(owner, observer);
    }

    public UserInfoEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoEntity userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 获取商品列表数量
     */
    public int getShopSum() {
        List<BuyShop> value = shops.getValue();
        return value == null ? 0 : value.size();
    }

    /**
     * 获取支付类型
     *
     * @return {@link Payment}
     */
    @Bindable
    public int getPayment() {
        return payment;
    }

    /**
     * 设置支付类型
     *
     * @param payment {@link Payment}
     */
    public void setPayment(int payment) {
        this.payment = payment;
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
     * 更新支付结算金额
     */
    public void updateTotalMoney() {
        Integer value = this.netFee.getValue();
        int money = value == null ? 0 : value;
        List<BuyShop> shops = this.shops.getValue();
        if (shops != null) {
//            int shopSum = 0;
            for (BuyShop shop : shops) {
                if (shop != null) {
                    if (shop.isBuy()) {
                        money += (shop.getFee() * shop.getProductBuySum());
                    }
//                    shopSum += shop.getProductBuySum();
                }
            }
//            this.shopSum.setValue(shopSum);
            this.shopSum.setValue(buySum.size());
        }
        this.paymentMoney = money - productCouponMoney;
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
     * 获取网费金额, 单位:分
     */
    @Bindable
    public int getNetFeeNum() {
        Integer value = netFee.getValue();
        return value == null ? 0 : value;
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
                if (shop != null && shop.isBuy()) {
                    total += (shop.getFee() * shop.getProductBuySum());
                }
            }
        }
        return cal(total);
    }

    /**
     * 获取商品金额总计
     *
     * @return 返回列表中所有的商品金额总价, 单位: 分
     */
    @Bindable
    public int getShopMoneyTotalNum() {
        int total = 0;
        List<BuyShop> value = shops.getValue();
        if (value != null) {
            for (BuyShop shop : value) {
                if (shop != null && shop.isBuy()) {
                    total += (shop.getFee() * shop.getProductBuySum());
                }
            }
        }
        return total - productCouponMoney;
    }

    /**
     * 添加商品到商品列表
     *
     * @param data   {@link ShopEntity} 商品数据对象
     * @param flavor 口味
     * @param spec   规格
     */
    public void attachShop(ShopEntity data, String flavor, String spec) {
        this.attachShop(data, flavor, spec, 1);
    }

    /**
     * 添加商品到商品列表
     *
     * @param data   {@link ShopEntity} 商品数据对象
     * @param flavor 口味
     * @param spec   规格
     * @param sum    商品数量
     */
    public void attachShop(ShopEntity data, String flavor, String spec, int sum) {
        if (flavor == null) flavor = "";
        if (spec == null) spec = "";

        List<BuyShop> value = this.shops.getValue();
        if (value == null) {
            value = new ArrayList<>();
        }
        boolean hasShop = false;
        // 查找是否有相同商品ID,相同口味,相同规格的商品对象
        for (BuyShop buy : value) {
            // 如果找到了,就增加商品购买数量
            if (buy.getProductId().equals(data.getProductId())
                    && buy.getProductFlavorName().equals(flavor)
                    && buy.getProductSpecName().equals(spec)) {
                buy.setProductBuySum(buy.getProductBuySum() + sum);
                Integer srcSum = buySum.get(buy.getProductId());
                if (srcSum != null) {
                    int shopSum = srcSum + sum;
                    buySum.put(buy.getProductId(), shopSum);
                }
                hasShop = true;
                break;
            }
        }
        // 如果没找到就新建一个并添加到商品列表中
        if (!hasShop) {
            BuyShop shop = new BuyShop();
            shop.setProductBuySum(sum);
            shop.setShopType(data.getProductVariety());
            shop.setShopName(data.getProductName());
//            shop.setShopImage(data.getProductImg());
            shop.setShopImage(data.getShopImage());
            shop.setProductSpecName(spec);
            shop.setProductFlavorName(flavor);
            shop.setProductId(data.getProductId());
            shop.setFee(data.getSellPrice());
            shop.setMax(data.getBarCount());
            value.add(shop);
            Integer count = buySum.get(shop.getProductId());
            buySum.put(shop.getProductId(), count == null ? sum : count + sum);
        }
        // 更新数据
        this.shops.setValue(value);
        this.updateTotalMoney();
        this.notifyChange();
    }


    /**
     * 添加商品到商品列表
     *
     * @param data   {@link BuyShop} 商品数据对象
     * @param flavor 口味
     * @param spec   规格
     * @param sum    商品数量
     */
    public void attachShop(BuyShop data, String flavor, String spec, int sum) {
        if (flavor == null) flavor = "";
        if (spec == null) spec = "";

        List<BuyShop> value = this.shops.getValue();
        if (value == null) {
            value = new ArrayList<>();
        }
        boolean hasShop = false;
        // 查找是否有相同商品ID,相同口味,相同规格的商品对象
        for (BuyShop buy : value) {
            // 如果找到了,就增加商品购买数量
            if (buy.getProductId().equals(data.getProductId())
                    && buy.getProductFlavorName().equals(flavor)
                    && buy.getProductSpecName().equals(spec)) {
                buy.setProductBuySum(buy.getProductBuySum() + sum);
                Integer srcSum = buySum.get(buy.getProductId());
                if (srcSum != null) {
                    int shopSum = srcSum + sum;
                    buySum.put(buy.getProductId(), shopSum);
                }
                hasShop = true;
                break;
            }
        }
        // 如果没找到就新建一个并添加到商品列表中
        if (!hasShop) {
            value.add(data);
            Integer count = buySum.get(data.getProductId());
            buySum.put(data.getProductId(), count == null ? sum : count + sum);
        }
        // 更新数据
        this.shops.setValue(value);
        this.updateTotalMoney();
        this.notifyChange();
    }

    /**
     * 删除商品
     *
     * @param productId 商品ID
     */
    public void detachShop(String productId) {
        List<BuyShop> value = shops.getValue();
        // 如果商品列表不是空的
        if (value != null && !value.isEmpty()) {
            // 查找是否有这种商品
            for (int i = 0; i < value.size(); i++) {
                BuyShop buy = value.get(i);
                // 如果找到了,判断购买数量
                if (buy.getProductId().equals(productId)) {
                    if (buy.getProductBuySum() <= 1) {
                        // 如果商品数量小等于1,减一则相当于不购买,从商品列表中删除
                        value.remove(i);
                    } else {
                        // 如果商品数量大于1,则减少一个商品数量
                        value.get(i).setProductBuySum(buy.getProductBuySum() - 1);
                    }
                    Integer srcSum = buySum.get(productId);
                    if (srcSum != null) {
                        srcSum -= 1;
                        if (srcSum == 0) {
                            buySum.remove(productId);
                        } else {
                            buySum.put(productId, srcSum);
                        }
                    }
                    break;
                }
            }
            // 更新数据
            this.shops.setValue(value);
            this.updateTotalMoney();
            this.notifyChange();
        }
    }

    /**
     * 删除商品
     *
     * @param productId 商品ID
     * @param flavor    口味
     * @param spec      规格
     */
    public void detachShop(String productId, String flavor, String spec) {
        if (flavor == null) flavor = "";
        if (spec == null) spec = "";

        List<BuyShop> value = shops.getValue();
        // 如果商品列表不是空的
        if (value != null && !value.isEmpty()) {
            // 查找是否有这种商品
            for (int i = 0; i < value.size(); i++) {
                BuyShop buy = value.get(i);
                // 如果找到了,判断购买数量
                if (buy.getProductId().equals(productId)
                        && buy.getProductFlavorName().equals(flavor)
                        && buy.getProductSpecName().equals(spec)) {
                    if (buy.getProductBuySum() <= 1) {
                        // 如果商品数量小等于1,减一则相当于不购买,从商品列表中删除
                        value.remove(i);
                    } else {
                        // 如果商品数量大于1,则减少一个商品数量
                        value.get(i).setProductBuySum(buy.getProductBuySum() - 1);
                    }
                    Integer srcSum = buySum.get(productId);
                    if (srcSum != null) {
                        srcSum -= 1;
                        if (srcSum == 0) {
                            buySum.remove(productId);
                        } else {
                            buySum.put(productId, srcSum);
                        }
                    }
                    break;
                }
            }
            // 更新数据
            this.shops.setValue(value);
            this.updateTotalMoney();
            this.notifyChange();
        }
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
