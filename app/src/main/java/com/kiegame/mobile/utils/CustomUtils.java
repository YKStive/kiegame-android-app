package com.kiegame.mobile.utils;

import android.text.TextUtils;


import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_COMPLETE;
import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_GRAB;
import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_PRODUCE;
import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_TAKE;

/**
 * 对订单商品重复操作
 */
public class CustomUtils {
    public static String splitIdCard(String idCard) {
        if (!TextUtils.isEmpty(idCard)) {
            return idCard.substring(idCard.length() - 4);
        }
        return null;
    }

    public static String convertPayType(int payType) {
        String result;
        switch (payType) {
            case 0:
                result = "在线支付";
                break;
            case 1:
                result = "吧台支付";
                break;
            default:
                result = "未知类型";
                break;

        }

        return result;
    }

    public static String convertOperateType(int operateType) {
        String result;
        switch (operateType) {
            case DO_ORDER_TYPE_COMPLETE:
                result = "完成";
                break;
            case DO_ORDER_TYPE_TAKE:
                result = "接单";
                break;
            case DO_ORDER_TYPE_GRAB:
                result = "抢单";
                break;
            case DO_ORDER_TYPE_PRODUCE:
                result = "出品";
                break;
            default:
                result = "未知类型";
                break;

        }

        return result;
    }


    public static String convertTimeCounter(int left){
        String[] strings = DateUtil.second2MS(left);
        return strings[0]+":"+strings[1]+"s";

    }
}
