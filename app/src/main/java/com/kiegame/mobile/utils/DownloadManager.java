package com.kiegame.mobile.utils;

import android.os.Environment;

import androidx.annotation.NonNull;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.logger.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by: var_rain.
 * Created date: 2019/9/23.
 * Description: 下载管理器
 */
public class DownloadManager {

    private static DownloadManager INS;
    private static File DOWNLOAD_PATH;
    private static File DOWNLOAD_FILE;
    private OkHttpClient client;
    private long contentSize;
    private long downloadSize;
    private String url;

    /**
     * 构造方法
     */
    private DownloadManager() {
        DownloadManager.DOWNLOAD_PATH = Game.ins().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        this.client = new OkHttpClient();
    }

    /**
     * 获取当前对象
     *
     * @return 返回 {@link DownloadManager}
     */
    public static DownloadManager ins() {
        if (DownloadManager.INS == null) {
            DownloadManager.INS = new DownloadManager();
        }
        return DownloadManager.INS;
    }

    /**
     * 下载文件
     *
     * @param url      下载地址
     * @param callback 下载回调
     */
    public void download(String url, DownloadListener callback) {
        this.url = url;
        String fileName = this.analyzeFileName(url);
        this.get(url, fileName, callback);
    }

    /**
     * 下载文件
     *
     * @param url      下载地址
     * @param name     文件名
     * @param callback 下载回调
     */
    public void download(String url, String name, DownloadListener callback) {
        this.url = url;
        this.get(url, name, callback);
    }

    /**
     * 下载文件
     *
     * @param url      下载地址
     * @param name     文件名
     * @param callback 下载回调
     */
    private void get(String url, String name, DownloadListener callback) {
        this.contentSize = 0;
        this.downloadSize = 0;
        this.resolveDownloadFilePath(name);
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.downloadError(url);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                ResponseBody body = response.body();
                if (body != null) {
                    contentSize = body.contentLength();
                    writeStreamToFile(body.byteStream(), callback);
                } else {
                    callback.downloadError(url);
                }
            }
        });
    }

    /**
     * 保存到文件
     *
     * @param in       输入流
     * @param callback 下载回调
     */
    private void writeStreamToFile(InputStream in, DownloadListener callback) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(DownloadManager.DOWNLOAD_FILE, false);
            byte[] bytes = new byte[2048];
            int len;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
                downloadSize += len;
                callback.downloadProcess(((int) (contentSize / 1024)), (int) (downloadSize * 1.0f / contentSize * 100));
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            callback.downloadError(this.url);
            return;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        callback.downloadSuccess(DownloadManager.DOWNLOAD_FILE.getAbsolutePath());
    }

    /**
     * 解析下载文件保存位置
     *
     * @param fileName 文件名
     */
    private void resolveDownloadFilePath(String fileName) {
        DownloadManager.DOWNLOAD_FILE = new File(DownloadManager.DOWNLOAD_PATH, fileName);
        File dir = DownloadManager.DOWNLOAD_FILE.getParentFile();
        if (dir != null) {
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e("Download directory create fail");
                }
            }
        } else {
            String parent = DownloadManager.DOWNLOAD_FILE.getParent();
            if (parent != null) {
                File file = new File(parent);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        Log.e("Download directory create fail");
                    }
                }
            }
        }
        if (!DownloadManager.DOWNLOAD_FILE.exists()) {
            try {
                if (!DownloadManager.DOWNLOAD_FILE.createNewFile()) {
                    Log.e("Download file create fail");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DownloadManager.DOWNLOAD_FILE = new File(DownloadManager.DOWNLOAD_PATH, System.currentTimeMillis() + fileName);
            try {
                if (!DownloadManager.DOWNLOAD_FILE.createNewFile()) {
                    Log.e("Download file create fail");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从URL中解析出下载文件名
     *
     * @param url 下载地址
     * @return 返回解析出来的文件名
     */
    private String analyzeFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 文件下载回调
     */
    public interface DownloadListener {

        /**
         * 下载成功
         *
         * @param name 文件保存位置
         */
        void downloadSuccess(String name);

        /**
         * 下载中
         *
         * @param size  文件总大小
         * @param value 下载进度(0-100)
         */
        void downloadProcess(int size, int value);

        /**
         * 下载出错
         */
        void downloadError(String url);
    }
}
