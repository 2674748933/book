package com.coldnight.book.controls;


import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;

/**
 * Author: duanlei
 * Date: 2015-12-09
 */
public class ReadView extends androidx.appcompat.widget.AppCompatTextView {

    public ReadView(Context context) {
        super(context);
        init();
    }

    public ReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    /**
     * 获取当前页总字数
     */
    public int getCharNum() {
        if (getLayout() != null)
            return getLayout().getLineEnd(getLineNum());
        return 0;
    }

    /**
     * 获取当前页总行数
     */
    private int getLineNum() {
        Layout layout = getLayout();
        int topOfLastLine = getHeight() - getPaddingTop() - getPaddingBottom() - getLineHeight();
        if (layout != null)
            return layout.getLineForVertical(topOfLastLine);
        return 0;
    }


}