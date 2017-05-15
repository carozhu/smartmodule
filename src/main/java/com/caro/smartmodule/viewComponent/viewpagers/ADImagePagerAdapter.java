package com.caro.smartmodule.viewComponent.viewpagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.caro.smartmodule.R;

import java.util.List;

/**
 * Created by caro on 9/1/15.
 */

public class ADImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<String> imageList;
    private int           size;
    private boolean       isInfiniteLoop;
    public OnViewClickListener onViewClickListener;


    public ADImagePagerAdapter(Context context, List<String> stringList){
        this.context=context;
        this.imageList=stringList;
        this.size = getSize(imageList);
        isInfiniteLoop = false;

    }
    public void setOnViewClickListener(OnViewClickListener mOnViewClickListener) {
        this.onViewClickListener = mOnViewClickListener;
    }

    public interface OnViewClickListener {
        public void ViewClick(int position);
    }
    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : getSize(imageList);
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }



    @Override
    public View getView(final int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_simple_image, null);
            holder.imageView=(ImageView)view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        if (imageList!=null && imageList.size()>0){
            holder.imageView.setTag(imageList.get(position));
            Glide.with(context)
                    .load(imageList.get(position))
                    .centerCrop()
                    .crossFade()
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onViewClickListener !=null){
                        onViewClickListener.ViewClick(position);

                    }
                }
            });
        }


        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ADImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }


    /**
     * for android common utils frame
     * get size of list
     *
     * <pre>
     * getSize(null)   =   0;
     * getSize({})     =   0;
     * getSize({1})    =   1;
     * </pre>
     *
     * @param <V>
     * @param sourceList
     * @return if list is null or empty, return 0, else return {@link List#size()}.
     */
    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null ? 0 : sourceList.size();
    }
}
