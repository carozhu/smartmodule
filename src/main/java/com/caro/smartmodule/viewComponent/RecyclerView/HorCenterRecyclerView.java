package com.caro.smartmodule.viewComponent.RecyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 水平居中的RecyclerView
 * Created by george.yang on 2015/12/30.
 * RecyclerView横向居中显示
 * <p>
 * 横向的RecyclerView默认从最左边开始，如果你的项目要求item数量不足以铺满最大宽度时需要居中显示，
 * 此时需要自定义RecyclerView达到这种效果
 */
public class HorCenterRecyclerView extends RecyclerView {
    public HorCenterRecyclerView(Context context) {
        super(context);
    }

    public HorCenterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorCenterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int lastWidth;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (getChildCount() > 0) {
            int newWidth = 0;
            for (int i = 0; i < getChildCount(); i++) {
                newWidth += getChildAt(i).getMeasuredWidth();
            }
            if (lastWidth != newWidth) {
                lastWidth = newWidth;

                int empty = getMeasuredWidth() - newWidth;
                if (empty > 0) {
                    if (getPaddingLeft() == empty / 2) {
                        return;
                    }

                    setPadding(empty / 2, 0, empty / 2, 0);
                    //如果不再一次onLayout，子view就不会有padding
                    super.onLayout(changed, l, t, r, b);
                }
            }
        }
    }

    //    //固定宽度的居中方式
    //    protected void onMeasure(int widthSpec, int heightSpec) {
    //        if (getAdapter()==null) {
    //            super.onMeasure(widthSpec, heightSpec);
    //        } else {
    //            int specHeight = MeasureSpec.getSize(heightSpec);
    //            int maxHeight = MeasureSpec.makeMeasureSpec(specHeight, MeasureSpec.AT_MOST);
    //            //item_data weidth:60dp
    //            setMeasuredDimension(DensityUtil.dip2px(getContext(),60*getAdapter().getItemCount()), maxHeight);
    //        }
    //    }

}