package com.kiegame.mobile.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.entity.receive.VersionEntity;
import com.kiegame.mobile.repository.entity.submit.QueryAppVersion;
import com.kiegame.mobile.utils.Version;

import java.util.List;

/**
 * Created by: var.
 * Created date: 2020/3/31.
 * Description: 主界面
 */
public class MainModel extends ViewModel {

    private MutableLiveData<VersionEntity> update;

    public MainModel() {
        this.update = new MutableLiveData<>();
    }

    /**
     * 检查更新
     */
    public LiveData<VersionEntity> update() {
        QueryAppVersion info = new QueryAppVersion();
        info.setVersionId("android");
        Network.api().queryAppVersion(info)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<VersionEntity>>(false) {
                    @Override
                    public void onSuccess(List<VersionEntity> data, int total, int length) {
                        if (data != null && !data.isEmpty()) {
                            VersionEntity version = data.get(0);
                            if (version != null) {
                                if (Version.needUpdate(version.getAppCode())) {
                                    update.setValue(version);
                                    return;
                                }
                            }
                        }
                        update.setValue(null);
                    }
                });
        return this.update;
    }
}
