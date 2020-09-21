package com.kiegame.mobile.utils;

import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.MenuEntry;

import java.util.Arrays;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/9/21.
 * Description: 访问权限控制
 */
public class Access {

    // 权限控制,分别对应 充值,商品,订单,为0则表示无权限,为1则表示有权限
    private static int[] permission = {0, 0, 0};

    /**
     * 权限转化
     *
     * @param data 登录数据对象
     */
    public static void access(LoginEntity data) {
        Arrays.fill(permission, 0);
        if (data != null) {
            List<MenuEntry> menus = data.getMenuList();
            if (menus != null && !menus.isEmpty()) {
                for (MenuEntry menu : menus) {
                    String url = menu.getMenuUrl();
                    if (!Text.empty(url)) {
                        switch (url) {
                            // 网费充值
                            case "app-recharge":
                                permission[0] = 1;
                                break;
                            // 商品下单
                            case "app-product":
                                permission[1] = 1;
                                break;
                            // 订单管理
                            case "app-order":
                                permission[2] = 1;
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 是否允许访问充值
     *
     * @return true:允许 false:不允许
     */
    public static boolean canRecharge() {
        return permission[0] == 1;
    }

    /**
     * 是否允许访问商品管理
     *
     * @return true:允许 false:不允许
     */
    public static boolean canProduct() {
        return permission[1] == 1;
    }

    /**
     * 是否允许访问订单
     *
     * @return true:允许 false:不允许
     */
    public static boolean canOrder() {
        return permission[2] == 1;
    }
}
