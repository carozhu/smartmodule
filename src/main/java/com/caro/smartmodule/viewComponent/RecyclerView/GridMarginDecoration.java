package com.caro.smartmodule.viewComponent.RecyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.caro.smartmodule.utils.DisplayMetricsUtil;

/**
 * Created by caro on 2017/3/8.
 * 为 Grid recyclerView设置间距
 */

public class GridMarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public GridMarginDecoration(int dp,Context context) {
        margin =  DisplayMetricsUtil.dip2px(context,dp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}
