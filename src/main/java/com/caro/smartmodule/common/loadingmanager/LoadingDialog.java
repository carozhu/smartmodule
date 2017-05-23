package com.caro.smartmodule.common.loadingmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caro.smartmodule.R;
import com.caro.smartmodule.viewComponent.LoadingViewAnim.LVCircularSmile;


/**
 * 
 * 简单的透明正在加载Dialog
 * 
 * @author caro
 */
public class LoadingDialog {

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 *            正在加载TextView显示内容
	 * @return Dialog 使用方法:createLoadingDialog Dialog.show(); Dialog.dimiss();
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		// 首先得到整个View
		View view = LayoutInflater.from(context).inflate(R.layout.loading__dialog_view, null);
		// 获取整个布局
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_view);

		final TextView tipsTV = (TextView)view.findViewById(R.id.loading_tips);
		final LVCircularSmile smaile  = (LVCircularSmile)view.findViewById(R.id.lv_circularSmile);
		smaile.startAnim();

		//加载动画，动画用户使img图片不停的旋转
		//Animation animation = AnimationUtils.loadAnimation(context,R.anim.dialog_load_animation);
		//显示动画
		//img.startAnimation(animation);

		if (!TextUtils.isEmpty(msg)){
			tipsTV.setText(msg);
		}

		// 创建自定义样式的Dialog
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
		// 设置返回键无效
		loadingDialog.setCancelable(false);
		loadingDialog.setCanceledOnTouchOutside(true);
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				// TODO: 取消网络请求
			}
		});
		return loadingDialog;
	}


}
