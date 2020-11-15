package com.kiegame.mobile.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by: var.
 * Created date: 2020/3/31.
 * Description: 针对软键盘遮挡小屏幕适配
 */
public class SoftInputAdapter {

    private static boolean fullScreen;

    public static void assistActivity(Activity activity, boolean fullScreen) {
        SoftInputAdapter.fullScreen = fullScreen;
        new SoftInputAdapter(activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private SoftInputAdapter(Activity activity) {
        FrameLayout content = activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(this::possiblyResizeChildOfContent);
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return SoftInputAdapter.fullScreen ? r.bottom : r.bottom - r.top;
    }
}
