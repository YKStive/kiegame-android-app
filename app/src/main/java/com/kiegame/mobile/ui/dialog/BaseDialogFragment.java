package com.kiegame.mobile.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kiegame.mobile.R;

import java.lang.reflect.Field;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 对话父类
 */
public abstract class BaseDialogFragment extends DialogFragment {

    //该类对象的唯一标识
    protected final String TAG = String.valueOf(this);
    //dataBinding对象
    protected ViewDataBinding rootBinding;
    //该类对象this
    protected Context context;
    //fragmentManager
    protected FragmentManager fragmentManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        fragmentManager = getChildFragmentManager();
        initView(savedInstanceState);
        return rootBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = getChildFragmentManager();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(R.color.translucent);
            window.setDimAmount(getDimAmount());
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        initEvent(savedInstanceState);
        onInit(savedInstanceState);
    }


    /**
     * 背景黑暗度
     */
    @FloatRange(from = 0, to = 1)
    protected float getDimAmount() {
        return 0.3f;
    }

    /**
     * 设置布局文件
     */
    @LayoutRes
    public abstract int getLayoutId();


    /**
     * 初始化View
     *
     * @param savedInstanceState savedInstanceState
     */
    protected void initView(Bundle savedInstanceState) {

    }

    /**
     * 初始化事件
     *
     * @param savedInstanceState savedInstanceState
     */
    protected void initEvent(Bundle savedInstanceState) {

    }

    /**
     * 初始化
     *
     * @param savedInstanceState savedInstanceState
     */
    protected void onInit(Bundle savedInstanceState) {

    }


    /**
     * 显示dialog
     *
     * @param manager FragmentManager
     */
    public void show(FragmentManager manager) {
        try {
            Field mDismissed = DialogFragment.class.getDeclaredField("mDismissed");
            Field mShownByMe = DialogFragment.class.getDeclaredField("mShownByMe");
            mDismissed.setAccessible(true);
            mDismissed.set(this, false);
            mShownByMe.setAccessible(true);
            mShownByMe.set(this, true);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            if (!isAdded()) {
                fragmentTransaction.add(this, TAG);
            } else {
                fragmentTransaction.show(this);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
