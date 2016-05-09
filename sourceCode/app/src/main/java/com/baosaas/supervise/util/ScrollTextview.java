package com.baosaas.supervise.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/15.
 */
public class ScrollTextview extends TextView {
    public ScrollTextview(Context context) {
        super(context);
    }

    public ScrollTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
