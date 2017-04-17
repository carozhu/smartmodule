package com.caro.smartmodule.viewComponent.viewpagers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.caro.smartmodule.R;
import com.caro.smartmodule.viewComponent.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by caro on 16/6/23.
 * <p/>
 * 封装autoScrollViewPager 与 circlePageIndicator一体的显示广告轮播图片的ViewGroup
 * 的结构体
 * 若需要定制其他广告提示,请定制该结构体中的布局文件
 * 广告轮播图默认高度:150dp
 * 默认滚动.滚动间隔时间--
 */
public class SmartADautoScrollViewpager extends LinearLayout {
    private View rootView;
    private Context context;
    private AutoScrollViewPager autoScrollViewPager;
    private CirclePageIndicator circlePageIndicator;

    private boolean showIndicator;
    private int fillColor;
    private int strokeColor;
    private int radius;
    private int interval;


    public SmartADautoScrollViewpager(Context context) {
        super(context);
        this.context = context;
        rootView = View.inflate(context, R.layout.structure_smart_adviewpager, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ADPagerAtrrs);
        initView(typedArray);
    }

    public SmartADautoScrollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        rootView = View.inflate(context, R.layout.structure_smart_adviewpager, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ADPagerAtrrs);
        initView(typedArray);
    }


    private void initView(TypedArray typedArray) {
        autoScrollViewPager = (AutoScrollViewPager) rootView.findViewById(R.id.autopager);
        circlePageIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);

        showIndicator = typedArray.getBoolean(R.styleable.ADPagerAtrrs_showIndicator, false);
        if (showIndicator) {
            circlePageIndicator.setVisibility(VISIBLE);
        } else {
            circlePageIndicator.setVisibility(GONE);
        }

        fillColor = typedArray.getColor(R.styleable.ADPagerAtrrs_FillColor, Color.parseColor("#FFFFFF"));
        circlePageIndicator.setFillColor(fillColor);

        strokeColor = typedArray.getColor(R.styleable.ADPagerAtrrs_StrokeColor, Color.parseColor("#FFFFFF"));
        circlePageIndicator.setStrokeColor(strokeColor);

        radius = typedArray.getInteger(R.styleable.ADPagerAtrrs_radius, 10);
        circlePageIndicator.setRadius(radius);

        interval = typedArray.getInteger(R.styleable.ADPagerAtrrs_interval, 4000);
        autoScrollViewPager.setInterval(interval);
    }


    /**
     * set indicator radius
     *
     * @param radius
     * @return
     */
    public SmartADautoScrollViewpager setCirclePageIndicatorRadius(float radius) {

        circlePageIndicator.setRadius(radius);
        return this;
    }


    /**
     * @param imageUrlList
     * @return
     */
    public SmartADautoScrollViewpager loadDataAndShowADPager(ArrayList<String> imageUrlList) {
        if (imageUrlList == null) {
            return this;
        }

        if (imageUrlList.size() == 0) {
            return this;
        }

        ADImagePagerAdapter APA = new ADImagePagerAdapter(context, imageUrlList);
        autoScrollViewPager.setAdapter(APA);

        //autoScrollViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //circlePageIndicator.setFillColor(context.getResources().getColor(R.color.orange_yellow));
        //circlePageIndicator.setStrokeColor(context.getResources().getColor(R.color.white));
        //circlePageIndicator.setRadius(10);
        //autoScrollViewPager.setInterval(Interval);

        circlePageIndicator.setViewPager(autoScrollViewPager);
        if (imageUrlList.size() > 1) {
            circlePageIndicator.setVisibility(View.VISIBLE);
        } else {
            circlePageIndicator.setVisibility(View.GONE);
        }
        autoScrollViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
        autoScrollViewPager.startAutoScroll();

        return this;
    }
    public SmartADautoScrollViewpager loadDataAndShowADPager(ArrayList<String> imageUrlList,ADImagePagerAdapter.OnViewClickListener mOnViewClickListener) {
        if (imageUrlList == null) {
            return this;
        }

        if (imageUrlList.size() == 0) {
            return this;
        }

        ADImagePagerAdapter APA = new ADImagePagerAdapter(context, imageUrlList);
        autoScrollViewPager.setAdapter(APA);

        //autoScrollViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //circlePageIndicator.setFillColor(context.getResources().getColor(R.color.orange_yellow));
        //circlePageIndicator.setStrokeColor(context.getResources().getColor(R.color.white));
        //circlePageIndicator.setRadius(10);
        //autoScrollViewPager.setInterval(Interval);

        circlePageIndicator.setViewPager(autoScrollViewPager);
        if (imageUrlList.size() > 1) {
            circlePageIndicator.setVisibility(View.VISIBLE);
        } else {
            circlePageIndicator.setVisibility(View.GONE);
        }
        autoScrollViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
        autoScrollViewPager.startAutoScroll();

        APA.setOnViewClickListener(mOnViewClickListener);
        return this;
    }
    /**
     * stop scroll
     *
     * @return
     */
    public SmartADautoScrollViewpager stopAutoSroll() {

        autoScrollViewPager.stopAutoScroll();
        return this;
    }


    public AutoScrollViewPager getAutoViewpager() {
        if (autoScrollViewPager != null) {
            return autoScrollViewPager;
        }

        return null;

    }


}
