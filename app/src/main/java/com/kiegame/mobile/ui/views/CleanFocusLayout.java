package com.kiegame.mobile.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by: var_rain.
 * Created date: 2020/2/10.
 * Description: 焦点清除
 */
public class CleanFocusLayout extends ConstraintLayout {

    public CleanFocusLayout(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public CleanFocusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public CleanFocusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        hideInputMethod();
        requestFocus();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 隐藏输入法
     */
    private void hideInputMethod() {
        Activity activity = (Activity) this.getContext();
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                View focus = activity.getCurrentFocus();
                if (focus != null) {
                    inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }
}
