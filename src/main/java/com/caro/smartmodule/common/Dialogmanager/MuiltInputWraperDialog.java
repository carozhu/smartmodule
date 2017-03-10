package com.caro.smartmodule.common.Dialogmanager;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.caro.smartmodule.R;


public class MuiltInputWraperDialog extends Dialog implements View.OnClickListener {
	private View dialog_header;
	private TextView titleView;
	private EditText editViewone;
	private EditText editViewtwo;
	private ImageView exit;
	private boolean mShowCancel;
	private boolean mShowTitle;
	private TextInputLayout oneTextWrapper ;
	private TextInputLayout twoTextWrapper ;


	private String oneTextWrapperHint ;
	private String twoTextWrapperHint ;

	private String title;
	private String editOnetext;
	private String editOnehint;

	private String mTitleText;
	private String mButtonText;
	private String murl;
	private String text="";

	// private Button mConfirmButton;
	private View mCancelButton;
	private Button mConfirmButton;

	private ImageStyleDialogOnClickListener mCancelClickListener;
	private ImageStyleDialogOnClickListener mConfirmClickListener;

	private int  color=0xffffff;
	private int  titlecolor=0xffffff;
	public static interface ImageStyleDialogOnClickListener {
		public void onClick(MuiltInputWraperDialog Dialog);
	}

	public MuiltInputWraperDialog(Context context) {
		super(context, R.style.alert_dialog_style);
		setCancelable(true);
		setCanceledOnTouchOutside(false);

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.muilt_editable_dialog);

		// contain view
		dialog_header = (View) findViewById(R.id.header);
		titleView= (TextView) findViewById(R.id.title);
		exit= (ImageView) findViewById(R.id.exit);

		oneTextWrapper = (TextInputLayout) findViewById(R.id.onetextWrapper);
		twoTextWrapper = (TextInputLayout) findViewById(R.id.twotextWrapper);
		setoneTextWrapperHint(oneTextWrapperHint);
		settwoTextWrapperHint(twoTextWrapperHint);

		editViewone = (EditText) findViewById(R.id.editone);
		editViewtwo = (EditText) findViewById(R.id.edittwo);
		setEditOneText(editOnetext);
		setEditOneHint(editOnehint);

		setBackGroudColor(color);
		setTitle(title);
		setTitleColor(titlecolor);
		mConfirmButton = (Button) findViewById(R.id.makesure);
		mCancelButton = (View) findViewById(R.id.cancel);
		mCancelButton.setOnClickListener(this);
		mConfirmButton.setOnClickListener(this);
		exit.setOnClickListener(this);

	}


	public void setoneTextWrapperHint(String moneTextWrapperHint) {
		this.oneTextWrapperHint=moneTextWrapperHint;
		if (oneTextWrapper != null && oneTextWrapperHint != null) {
			oneTextWrapper.setHint(oneTextWrapperHint);
		}
	}

	public void settwoTextWrapperHint(String mtwoTextWrapperHint) {
		this.twoTextWrapperHint=mtwoTextWrapperHint;
		if (twoTextWrapper != null && twoTextWrapperHint != null) {
			twoTextWrapper.setHint(twoTextWrapperHint);
		}
	}
	/**
	 * 文本监听
	 * @author Administrator
	 */
	private class DivisionTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {

		}
	}

	public String getEditOneText() {
		if (editViewone != null ) {
			return editViewone.getText().toString();
		}
		return null;
	}

	public String getEditTwoText() {
		if (editViewtwo != null ) {
			return editViewtwo.getText().toString();
		}
		return null;
	}

	public EditText getOneEdit() {
		if (editViewone != null) {
			return editViewone;
		}
		return null;
	}

	public TextInputLayout getoneTextWrapper() {
		if (oneTextWrapper != null) {
			return oneTextWrapper;
		}
		return null;
	}

	public TextInputLayout getTwoTextWrapper() {
		if (twoTextWrapper != null) {
			return twoTextWrapper;
		}
		return null;
	}




	public void setEditOneText(String text) {
        this.editOnetext=text;
		if (editViewone != null && editOnetext != null) {
			editViewone.setText(editOnetext);
		}
	}

	public void setEditOneHint(String medithint) {
		this.editOnehint=medithint;
		if (editViewone != null && editOnehint != null) {
			editViewone.setHint(editOnehint);
		}
	}

	public void setTitle(String text) {
		this.title=text;
		if (titleView != null && title != null) {
			titleView.setText(title);
			titleView.setVisibility(View.VISIBLE);
		}
	}

	public String getTitleText() {
		return mTitleText;
	}



	public MuiltInputWraperDialog setButtonText(String text) {
		mButtonText = text;
		if (mConfirmButton != null && mConfirmButton != null) {
			mConfirmButton.setText(mButtonText);
		}
		return this;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public MuiltInputWraperDialog setBackGroudColor(int  mcolor) {
		this.color = mcolor;
		if (dialog_header != null && color != 0) {
			dialog_header.setBackgroundColor(color);
		}
		return this;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public MuiltInputWraperDialog setTitleColor(int  mtitlecolor) {
		this.titlecolor = mtitlecolor;
		if (titleView != null && titlecolor != 0) {
			titleView.setTextColor(titlecolor);
		}
		return this;
	}

	public boolean isShowCancelButton() {
		return mShowCancel;
	}

	public MuiltInputWraperDialog showCancelButton(boolean isShow) {
		mShowCancel = isShow;
		if (mCancelButton != null) {
			mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	public boolean isShowContentText() {
		return mShowTitle;
	}

	public MuiltInputWraperDialog setCancelClickListener(
			ImageStyleDialogOnClickListener listener) {
		mCancelClickListener = listener;
		return this;
	}

	public MuiltInputWraperDialog setConfirmClickListener(
			ImageStyleDialogOnClickListener listener) {
		mConfirmClickListener = listener;
		return this;
	}

	/**
	 * The real Dialog.cancel() will be invoked async-ly after the animation
	 * finishes.
	 */
	@Override
	public void cancel() {
		dismiss();

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancel) {
			if (mCancelClickListener != null) {
				mCancelClickListener.onClick(MuiltInputWraperDialog.this);
			} else {
				dismiss();
			}
		} else if (v.getId() == R.id.makesure) {
			if (mConfirmClickListener != null) {
				mConfirmClickListener.onClick(MuiltInputWraperDialog.this);
			} else {
				//dismiss();
			}
		}else if (v.getId() == R.id.exit){
			dismiss();
		}
	}

}