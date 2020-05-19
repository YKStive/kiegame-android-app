package com.kiegame.mobile.ui.views;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;

/**
 * Created by: var.
 * Created date: 2020/5/19.
 * Description: 金额输入过滤器
 */
public class MoneyFilter extends DigitsKeyListener {

    private int digits = 2;

    public MoneyFilter() {
        super(false, true);
    }

    public MoneyFilter setDigits(int d) {
        digits = d;
        return this;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }
        int len = end - start;
        if (len == 0) {
            return source;
        }
        if (source.toString().equals(".") && dstart == 0) {
            return "0.";
        }
        if (!source.toString().equals(".") && dest.toString().equals("0")) {
            return "0";
        }
        int dlen = dest.length();
        for (int i = 0; i < dstart; i++) {
            if (dest.charAt(i) == '.') {
                return (dlen - (i + 1) + len > digits) ? "" : new SpannableStringBuilder(source, start, end);
            }
        }
        for (int i = start; i < end; ++i) {
            if (source.charAt(i) == '.') {
                if ((dlen - dend) + (end - (i + 1)) > digits)
                    return "";
                else
                    break;
            }
        }
        return new SpannableStringBuilder(source, start, end);
    }
}
