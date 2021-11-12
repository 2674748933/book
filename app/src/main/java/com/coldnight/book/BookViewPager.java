package com.coldnight.book;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class BookViewPager extends ViewPager {
    public BookViewPager(@NonNull Context context) {
        super(context);
    }

    public BookViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
