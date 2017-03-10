package com.caro.smartmodule.common;

import android.util.SparseArray;
import android.view.View;

/**
 * @author http://blog.csdn.net/finddreams
 *         <p>
 *         simple usage:
 *         if (convertView == null) {
 *         convertView = LayoutInflater.from(mContext).inflate(
 *         R.layout.grid_item, parent, false);
 *         }
 *         TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
 * @Description:万能的viewHolder
 */
public class PowerfulViewHolder {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);//创建集合和根View关联
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}
