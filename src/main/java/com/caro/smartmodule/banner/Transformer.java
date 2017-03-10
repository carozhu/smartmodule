package com.caro.smartmodule.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.caro.smartmodule.banner.transformer.AccordionTransformer;
import com.caro.smartmodule.banner.transformer.BackgroundToForegroundTransformer;
import com.caro.smartmodule.banner.transformer.CubeInTransformer;
import com.caro.smartmodule.banner.transformer.CubeOutTransformer;
import com.caro.smartmodule.banner.transformer.DefaultTransformer;
import com.caro.smartmodule.banner.transformer.DepthPageTransformer;
import com.caro.smartmodule.banner.transformer.FlipHorizontalTransformer;
import com.caro.smartmodule.banner.transformer.FlipVerticalTransformer;
import com.caro.smartmodule.banner.transformer.ForegroundToBackgroundTransformer;
import com.caro.smartmodule.banner.transformer.RotateDownTransformer;
import com.caro.smartmodule.banner.transformer.RotateUpTransformer;
import com.caro.smartmodule.banner.transformer.ScaleInOutTransformer;
import com.caro.smartmodule.banner.transformer.StackTransformer;
import com.caro.smartmodule.banner.transformer.TabletTransformer;
import com.caro.smartmodule.banner.transformer.ZoomInTransformer;
import com.caro.smartmodule.banner.transformer.ZoomOutSlideTransformer;
import com.caro.smartmodule.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
