package com.caro.smartmodule.zxing.decode;


import android.app.Activity;
import android.content.DialogInterface;

/**
 * 监听摄像头是否已被使用
 * 
 * @author Sean Owen
 */
public final class FinishListener implements DialogInterface.OnClickListener,
		DialogInterface.OnCancelListener {

	private final Activity activityToFinish;

	public FinishListener(Activity activityToFinish) {
		this.activityToFinish = activityToFinish;
	}

	@Override
	public void onCancel(DialogInterface dialogInterface) {
		run();
	}

	@Override
	public void onClick(DialogInterface dialogInterface, int i) {
		run();
	}

	private void run() {
		activityToFinish.finish();
	}

}
