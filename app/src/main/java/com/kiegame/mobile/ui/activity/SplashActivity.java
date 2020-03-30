package com.kiegame.mobile.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivitySplashBinding;
import com.kiegame.mobile.model.SplashModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.repository.entity.receive.VersionEntity;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.DialogBox;
import com.kiegame.mobile.utils.DownloadManager;
import com.kiegame.mobile.utils.Text;

import java.io.File;

/**
 * Created by: var_rain.
 * Created date: 2020/01/03.
 * Description: 启动页
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> implements DownloadManager.DownloadListener {

    private SplashModel model;
    private ProgressDialog dialog;

    @Override
    protected int onLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(SplashModel.class);
    }

    @Override
    protected void onView() {

    }

    @Override
    protected void onData() {
        model.update().observe(this, this::checkUpdate);
    }

    /**
     * 检查更新
     *
     * @param data 版本数据对象
     */
    private void checkUpdate(VersionEntity data) {
        if (data == null) {
            // 不需要更新
            checkLogin();
        } else {
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
                        .cancel(this::checkLogin)
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

    /**
     * 检查登录
     */
    private void checkLogin() {
        if (hasAccount()) {
            autoLogin();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
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

    /**
     * 检查账号信息
     *
     * @return 是否存在账号信息 true: 存在 false: 不存在
     */
    private boolean hasAccount() {
        return !(Text.empty(model.account) || Text.empty(model.password));
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        model.autoLogin().observe(this, this::loginResult);
    }

    /**
     * 登录接口返回数据处理
     *
     * @param data 数据对象
     */
    private void loginResult(LoginEntity data) {
        if (data != null) {
            // 保存到内存
            Cache.ins().setToken(data.getLoginToken());
            Cache.ins().setLoginInfo(data);

            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
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
}
