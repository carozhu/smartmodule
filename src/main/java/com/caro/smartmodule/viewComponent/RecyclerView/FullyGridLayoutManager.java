package com.caro.smartmodule.viewComponent.RecyclerView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * http://www.jianshu.com/p/49bf92baa98b
 * <p>
 * 解决scroll中嵌套recycview grid时显示不全的问题
 * 并禁止滑动
 * <p>
 * 解决方法，在使用到recycleview的布局外用一个RelativeLayout，关键属性android:descendantFocusability="blocksDescendants"
 * <RelativeLayout
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:descendantFocusability="blocksDescendants">
 * <android.support.v7.widget.RecyclerView
 * android:background="@color/white"
 * android:id="@+id/recyclerView_single"
 * android:layout_width="match_parent"
 * android:layout_height="match_parent" />
 * </RelativeLayout>
 */

public class FullyGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public FullyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FullyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FullyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}