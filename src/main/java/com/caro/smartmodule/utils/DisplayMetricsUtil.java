package com.caro.smartmodule.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayMetricsUtil {
	//public static DisplayMetrics metric = new DisplayMetrics();

	 /**
	  * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	  */

	 public static int dip2px(Context context, float dpValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (dpValue * scale + 0.5f);
	 }

	 /**
	  * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	  */
	 public static int px2dip(Context context, float pxValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (pxValue / scale + 0.5f);
	 }

	/**
	 * 得到屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenHieght(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		int cyScreen = outMetrics.heightPixels;
		return cyScreen;
	}

	/**
	 * 获得屏幕宽度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		int cxScreen = outMetrics.widthPixels;
		return cxScreen;
	}


	/**
	 * 获取屏幕尺寸与密度.
	 *
	 * @param context the context
	 * @return mDisplayMetrics
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		Resources mResources;
		if (context == null) {
			mResources = Resources.getSystem();

		} else {
			mResources = context.getResources();
		}
		DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
		int densityDpi =   mDisplayMetrics.densityDpi;
		return mDisplayMetrics;
	}
}
