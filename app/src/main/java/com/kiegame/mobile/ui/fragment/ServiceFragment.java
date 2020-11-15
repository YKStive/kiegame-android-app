package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.ServiceAdapter;
import com.kiegame.mobile.databinding.FragmentServiceBinding;
import com.kiegame.mobile.logger.Log;
import com.kiegame.mobile.model.ServiceModel;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.dialog.ChangeUserDialog;
import com.kiegame.mobile.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 服务
 */
public class ServiceFragment extends BaseFragment<FragmentServiceBinding> {

    private ServiceModel model;
    private String date;
    private Calendar cal;
    private SimpleDateFormat format;
    private CountDownTimer refresTimer;
    private SpeechSynthesizer mTts;


    @Override
    protected int onLayout() {
        return R.layout.fragment_service;
    }

    @Override
    protected void onObject() {
        this.model = new ViewModelProvider(this).get(ServiceModel.class);
        binding.setModel(model);
        binding.setFragment(this);
        cal = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        toDay();



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        refresTimer = new CountDownTimer(Integer.MAX_VALUE, 2000) {
            @Override
            public void onTick(long left) {
//                model.refresh();
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        refresTimer.cancel();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onView() {
        List<Fragment> views = new ArrayList<>();
        //呼叫服务
        ServiceCallFragment serviceCallFragment = new ServiceCallFragment();
        views.add(serviceCallFragment);
        //商品订单
        ServiceGoodsOrderFragment goodsOrderFragment = new ServiceGoodsOrderFragment();
        views.add(goodsOrderFragment);
        String[] titles = new String[]{
                getString(R.string.service_tab_call_service),
                getString(R.string.service_tab_goods_order),
        };
        ServiceAdapter adapter = new ServiceAdapter(getChildFragmentManager(), views, titles);
        binding.vpViews.setAdapter(adapter);
        binding.vpViews.setOffscreenPageLimit(2);
        binding.tlOrderTab.setupWithViewPager(binding.vpViews);
        binding.tvOrderCreateTime.setText(date);
        //刷新事件
        binding.srlLayout.setOnRefreshListener(refreshLayout -> model.refresh());
        //刷新完成
        model.getIsRefreshFinish().observe(this, isRefreshFinish -> {
            if (isRefreshFinish) {
                binding.srlLayout.finishRefresh();
            }
        });

        binding.tvOrderCreateTime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initTts();
                return true;
            }
        });
    }


    private void initTts() {
        mTts = SpeechSynthesizer.createSynthesizer(getContext(), new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS) {
                    Log.i("TTS", "语音初始化失败,错误码：" + i);
                } else {
                    Log.i("TTS", "语言初始化成功");
//                    //设置发音人
//                    mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi");
//                    //设置语速,值范围：[0, 100],默认值：50
//                    mTts.setParameter(SpeechConstant.SPEED, "49");
//                    //设置音量
//                    mTts.setParameter(SpeechConstant.VOLUME, "100");
//                    //设置语调
//
//                    mTts.setParameter(SpeechConstant.PITCH, "50");

                    mTts.startSpeaking("今天提琴器不错", new SynthesizerListener() {
                        @Override
                        public void onSpeakBegin() {
                            Log.i("TTS", "语言初始化成功");
                        }

                        @Override
                        public void onBufferProgress(int i, int i1, int i2, String s) {
                            Log.i("TTS", "语言初始化成功");
                        }

                        @Override
                        public void onSpeakPaused() {
                            Log.i("TTS", "语言初始化成功");
                        }

                        @Override
                        public void onSpeakResumed() {
                            Log.i("TTS", "语言初始化成功");
                        }

                        @Override
                        public void onSpeakProgress(int i, int i1, int i2) {
                            Log.i("TTS", "语言初始化成功");
                        }

                        @Override
                        public void onCompleted(SpeechError speechError) {
                            Log.i("TTS", "播放失败--"+speechError.getErrorDescription());
                        }

                        @Override
                        public void onEvent(int i, int i1, int i2, Bundle bundle) {
                            Log.i("TTS", "播放失败--"+i);
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onData() {
        model.refresh();
    }


    private void refresh() {
        model.refresh();
    }

    /**
     * 显示菜单
     */
    public void showMenu() {
        ChangeUserDialog.getInstance(user -> {
            binding.tvServiceId.setText(String.format("%s / %s", model.getLogin().getServiceName(), user));
        }).show(getChildFragmentManager());
    }


    /**
     * 下一天
     */
    public void nextDay() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(format.parse(date)));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date time = calendar.getTime();
            cal.setTime(time);
            date = format.format(time);
            binding.tvOrderCreateTime.setText(date);
            binding.srlLayout.autoRefresh();
            calendar.clear();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上一天
     */
    public void pastDay() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(format.parse(date)));
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
            Date time = calendar.getTime();
            cal.setTime(time);
            date = format.format(time);
            binding.tvOrderCreateTime.setText(date);
            binding.srlLayout.autoRefresh();
            calendar.clear();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当天时间
     */
    private void toDay() {
        Date date = new Date(System.currentTimeMillis());
        this.date = format.format(date);
        this.updateDate(this.date);
    }

    /**
     * 更新日期
     *
     * @param date 日期字符串
     */
    private void updateDate(String date) {
        try {
            cal.setTime(Objects.requireNonNull(format.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
