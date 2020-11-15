package com.kiegame.mobile.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.ViewAdapter;
import com.kiegame.mobile.databinding.ActivityMainBinding;
import com.kiegame.mobile.model.MainModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.repository.entity.receive.VersionEntity;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.Access;
import com.kiegame.mobile.utils.DialogBox;
import com.kiegame.mobile.utils.DownloadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/5.
 * Description: 主界面
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> implements ViewPager.OnPageChangeListener, DownloadManager.DownloadListener {

    // 子页面
    private List<View> views;
    // 点击状态图标数组
    private int[] press;
    // 未点击状态图标数组
    private int[] none;
    // 点击状态文字颜色
    private int pressColor;
    // 未点击状态文字颜色
    private int noneColor;
    // 当前选中的导航按钮
    private TextView selectBtn;
    private MainModel model;
    private ProgressDialog dialog;
    private boolean insCall = false;
    private List<String> pageConst;
    private List<String> pageName;

    @Override
    protected int onLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(MainModel.class);
        binding.setActivity(this);

        this.pageName = new ArrayList<>();
        this.views = new ArrayList<>();
        this.pageConst = new ArrayList<>();

        this.pageConst.add("recharge"); // index 0
        this.pageConst.add("notice"); // index 1
        this.pageConst.add("product"); // index 2
        this.pageConst.add("order"); // index 3

        this.press = new int[]{
                R.drawable.ic_net_fee_press,
                R.mipmap.ic_notifications_press,
                R.drawable.ic_commodity_press,
                R.drawable.ic_order_press,
        };
        this.none = new int[]{
                R.drawable.ic_net_fee_none,
                R.mipmap.ic_notifications_none,
                R.drawable.ic_commodity_none,
                R.drawable.ic_order_none,
        };

        this.pressColor = this.getResources().getColor(R.color.main_nav_btn_press);
        this.noneColor = this.getResources().getColor(R.color.main_nav_btn_none);

        Cache.ins().getMainPageObserver().observe(this, page -> {
            if (page != null) {
                if (page < views.size()) {
                    binding.vpViews.setCurrentItem(page);
                }
            }
        });

        requestSelfPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, (authorize, permissions) -> {
            // 只请求权限,不做任何操作
        });
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onView() {
        LayoutInflater inflater = LayoutInflater.from(this);
//        if (Access.canRecharge()) {
        // 网费充值
        views.add(inflater.inflate(R.layout.view_net_fee, null));
        binding.tvBtnNetFee.setVisibility(View.VISIBLE);
        pageName.add("recharge");
//        }
//        if (Access.canNotice()) {
        // 服务
        views.add(inflater.inflate(R.layout.view_service, null));
        binding.tvBtnService.setVisibility(View.VISIBLE);
        pageName.add("notice");
//        }
//        if (Access.canProduct()) {
        // 商品
        views.add(inflater.inflate(R.layout.view_commodity, null));
        binding.tvBtnCommodity.setVisibility(View.VISIBLE);
        pageName.add("product");
//        }
//        if (Access.canOrder()) {
        // 订单
        views.add(inflater.inflate(R.layout.view_order, null));
        binding.tvBtnOrder.setVisibility(View.VISIBLE);
        pageName.add("order");
//        }

        // 没有充值权限但是有商品权限,默认使用散客
        if (!Access.canRecharge()) {
            if (Access.canProduct()) {
                Cache.ins().setUserInfo(new UserInfoEntity("散客"));
            }
        }

        ViewAdapter adapter = new ViewAdapter(views);
        binding.vpViews.setAdapter(adapter);
        binding.vpViews.setOffscreenPageLimit(views.size());
        binding.vpViews.addOnPageChangeListener(this);

        selectBtn = (TextView) binding.clNavBar.getChildAt(0);
    }

    @Override
    protected void onData() {
        insCall = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!insCall) {
            insCall = true;
            model.update().observe(this, this::checkUpdate);
        }
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) {
//            Cache.ins().getUserSearchObserver().setValue(keyCode);
//        }
//        return super.onKeyUp(keyCode, event);
//    }

    /**
     * 检查更新
     *
     * @param data 版本数据对象
     */
    private void checkUpdate(VersionEntity data) {
        if (data != null) {
            // 需要更新
            if (data.getIsMustUpdate().equals("1")) {
                // 强制更新
                DialogBox.ins()
                        .title(String.format("需要升级到%s", data.getAppCode()))
                        .text(data.getAppName())
                        .confirm(() -> showDownloadDialog(data.getDownloadUrl()))
                        .show();
            } else {
                // 非强制
                DialogBox.ins()
                        .title(String.format("是否升级到%s", data.getAppCode()))
                        .text(data.getAppName())
                        .confirm(() -> showDownloadDialog(data.getDownloadUrl()))
                        .cancel(null)
                        .show();
            }
        }
    }

    /**
     * 显示下载对话框
     */
    private void showDownloadDialog(String url) {
        dialog = new ProgressDialog(this);
        dialog.setProgress(0);
        dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.setTitle("正在下载");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        DownloadManager.ins().download(url, this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (this.selectBtn != null) {
            this.cleanBtnStyle(this.selectBtn);
        }
        this.selectBtn = (TextView) binding.clNavBar.getChildAt(pageConst.indexOf(pageName.get(position)));
        this.setBtnStyle(this.selectBtn);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 清除按钮点击样式
     *
     * @param btn 导航按钮
     */
    private void cleanBtnStyle(TextView btn) {
        int index = binding.clNavBar.indexOfChild(btn);
        btn.setTextColor(noneColor);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, none[index], 0, 0);
    }

    /**
     * 设置按钮点击样式
     *
     * @param btn 导航按钮
     */
    private void setBtnStyle(TextView btn) {
        int index = binding.clNavBar.indexOfChild(btn);
        btn.setTextColor(pressColor);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, press[index], 0, 0);
    }

    /**
     * 切换页面
     *
     * @param index 页面索引
     */
    public void setCurrentPage(int index) {
        binding.vpViews.setCurrentItem(pageName.indexOf(pageConst.get(index)));
    }


    @Override
    public void downloadSuccess(String name) {
        dialog.cancel();
        startInstallNewVersion(name);
    }

    @Override
    public void downloadProcess(int size, int value) {
        if (dialog != null) {
            dialog.setProgress(value);
        }
    }

    @Override
    public void downloadError(String url) {
        dialog.cancel();
        runOnUiThread(() -> DialogBox.ins()
                .text("下载出错,请尝试重新下载")
                .confirm(() -> showDownloadDialog(url))
                .show());
    }

    /**
     * 跳转安装
     *
     * @param apk 下载文件
     */
    private void startInstallNewVersion(String apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String authority = "com.kiegame.mobile.FileProvider";
            Uri fileUri = FileProvider.getUriForFile(this, authority, new File(apk));
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(apk)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
}
