package com.caro.smartmodule.viewComponent;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.caro.smartmodule.R;
import com.caro.smartmodule.utils.AnimationController;
import com.caro.smartmodule.viewComponent.viewpagers.SmartADautoScrollViewpager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17.
 */

public class AdvertView  extends RelativeLayout{
    private SmartADautoScrollViewpager banner;
    LayoutParams params;
    Context mContext;
    AnimationController animationController;
    public AdvertView(Context context) {
        super(context);
        initView(context);
    }

    public AdvertView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AdvertView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        animationController = new AnimationController();
    }
    ImageView imageView;

    public void showBanner(ArrayList<String> imgeList){

        banner = new SmartADautoScrollViewpager(mContext);
        banner.setLayoutParams(params);
        banner.loadDataAndShowADPager(imgeList);
        this.addView(banner);
        showView(this);
//        imageView = new ImageView(mContext);
//        imageView.setBackgroundColor(mContext.getResources().getColor(R.color.actionbar_bg));
//        imageView.setLayoutParams(params);
//        this.addView(imageView);
    }

    public void showGifBanner(ArrayList<ImageBase> imgeList){
        banner = new SmartADautoScrollViewpager(mContext);
        banner.setLayoutParams(params);
        banner.loadDataAndShowADPager(imgeList,0);
        this.addView(banner);
        showView(this);
    }


    public void hindeView(){
        hindeView(this);
    }


    long durationMillis = 2000, delayMillis = 0;

    public void showView(View viewIn){

        animationController.fadeIn(viewIn, durationMillis, delayMillis);
    }

    public void hindeView(View viewOut){

        animationController.fadeOut(viewOut, durationMillis, delayMillis);
    }




}
