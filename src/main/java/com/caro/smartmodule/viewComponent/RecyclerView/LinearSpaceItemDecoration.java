package com.caro.smartmodule.viewComponent.RecyclerView;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by caro on 2017/3/8.
 * 为线性recyclerView设置间距
 */

public class LinearSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public LinearSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        //getChildPosition
        if(parent.getChildLayoutPosition(view) != 0)
            outRect.top = space;
    }
}