package com.caro.smartmodule.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author caro
 * 判断程序是否是在该版本
 * 第一次运行或是升级后第一次运行
 */
public class RunGuideUtil {
	/**
	 * @param context
	 * @return true 第一次运行，false。反之
	 */
	public static boolean initRunProgram(Context context) {
		boolean blnFirstRun = false;
		int getvercode = AppInfoUtil.getVerCode(context);
		SharedPreferences preferences = context.getSharedPreferences("runhelp", 0);//xml name
		Editor editor = preferences.edit();
		int saveVercode = preferences.getInt("vercode", 0);
		if (getvercode > saveVercode) {
			// 清除缓存 count
			editor.putInt("count", 0);
			//保存vercode
			editor.putInt("vercode", getvercode);
			// 提交修改
			editor.commit();
		}
		int count = preferences.getInt("count", 0);
		if (count == 0) {// 判断程序与第几次运行，如果是第一次运行则跳转到引导页面
			blnFirstRun = true;
		} else { // 开启主界面（或 你想要进入的界面Spalsh
			blnFirstRun = false;
		}
		// 存入数据
		editor.putInt("count", ++count);
		// 提交修改
		editor.commit();

		return blnFirstRun;
	}

}
