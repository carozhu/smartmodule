package com.caro.smartmodule.viewComponent.ScrollViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Description:解决在scrollview中只显示第一行数据的问题
 * @author http://blog.csdn.net/finddreams
 */
public class ScrollGridview extends GridView {
    public ScrollGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollGridview(Context context) {
        super(context);
    }

    public ScrollGridview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}

