package com.caro.smartmodule.common.Dialogmanager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.caro.smartmodule.R;


/**
 * 
 * 简单的透明正在加载Dialog
 * 
 * @author caro
 */
public class NetWorkExceptionTipDialog extends Dialog implements
View.OnClickListener{

	private OnNetActionListener mSetClickListener;
	private ImageView networkSetting;



	public NetWorkExceptionTipDialog(Context context) {
		super(context, R.style.loading_dialog);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_network_exception_tip);// layoutResID
		// 获取整个布局
		//LinearLayout layout = (LinearLayout)findViewById(R.id.parent);
		networkSetting = (ImageView)findViewById(R.id.network_setting);
		networkSetting.setOnClickListener(this);
	}
	
	public static interface OnNetActionListener {
		public void onClick(NetWorkExceptionTipDialog netWorkExceptionTips);
	}
	
	public NetWorkExceptionTipDialog setNEClickListener(
			OnNetActionListener listener) {
		mSetClickListener = listener;
		return this;
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.network_setting){
		     if(mSetClickListener !=null){
		    	 mSetClickListener.onClick(NetWorkExceptionTipDialog.this);
		     }else{
		    	 /************************/
		    	 this.dismiss();
		     }
			
		}
				
	}

	

}
