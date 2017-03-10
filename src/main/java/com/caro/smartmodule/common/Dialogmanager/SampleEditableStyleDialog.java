package com.caro.smartmodule.common.Dialogmanager;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.caro.smartmodule.R;


public class SampleEditableStyleDialog extends Dialog implements View.OnClickListener {
	private View dialog_header;
	private TextView titleView;
	private EditText editText;
	private ImageView exit;
	private boolean mShowCancel;
	private boolean mShowTitle;

	private String title;
	private String editext;
	private String edithint;
	private String mTitleText;
	private String mButtonText;
	private String murl;
	private String text="";

	// private Button mConfirmButton;
	private View mCancelButton;
	private Button mConfirmButton;

	private ImageStyleDialogOnClickListener mCancelClickListener;
	private ImageStyleDialogOnClickListener mConfirmClickListener;
	private ImageStyleDialogOnClickListener mImageClickListener;

	private int  color=0xffffff;

	public static interface ImageStyleDialogOnClickListener {
		public void onClick(SampleEditableStyleDialog Dialog);
	}

	public SampleEditableStyleDialog(Context context) {
		super(context, R.style.alert_dialog_style);
		setCancelable(true);
		setCanceledOnTouchOutside(false);

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_editable_dialog);

		// contain view
		dialog_header = (View) findViewById(R.id.header);
		titleView= (TextView) findViewById(R.id.title);
		exit= (ImageView) findViewById(R.id.exit);
		editText= (EditText) findViewById(R.id.edit_yanzheng);
		setBackGroudColor(color);
		setTitle(title);
		mConfirmButton = (Button) findViewById(R.id.makesure);
	      setConfirmButtonText(mButtonText);
		mCancelButton = (View) findViewById(R.id.cancel);
	      setEditText(editext);
		setEditHint(edithint);
		mCancelButton.setOnClickListener(this);
		mConfirmButton.setOnClickListener(this);
		exit.setOnClickListener(this);
		editText.addTextChangedListener(new DivisionTextWatcher());

	}
	/**
	 * 文本监听
	 *
	 * @author Administrator
	 *
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

	public String getEditText() {

		if (editText != null && editText != null) {
			return editText.getText().toString();
		}
		return "";
	}
	public EditText getEdit() {

		if (editText != null) {
			return editText;
		}
		return null;
	}


	public SampleEditableStyleDialog setEditText(String text) {
        this.editext=text;
		if (editText != null && editext != null) {
			 editText.setText(editext);
		}

		return this;
	}
	public SampleEditableStyleDialog setTitle(String text) {
		this.title=text;
		if (titleView != null && title != null) {
			titleView.setText(title);
			titleView.setVisibility(View.VISIBLE);
		}

		return this;
	}

	public SampleEditableStyleDialog setEditHint(String medithint) {
		this.edithint=medithint;
		if (editText != null && edithint != null) {
			editText.setHint(edithint);
		}

		return this;
	}
	public String getTitleText() {
		return mTitleText;
	}



	public SampleEditableStyleDialog setConfirmButtonText(String text) {
		mButtonText = text;
		if (mConfirmButton != null && mConfirmButton != null) {
			mConfirmButton.setText(mButtonText);
		}

		return this;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public SampleEditableStyleDialog setBackGroudColor(int  mcolor) {
		this.color = mcolor;
		if (dialog_header != null && color != 0) {
			dialog_header.setBackgroundColor(color);
		}
		return this;
	}

	public boolean isShowCancelButton() {
		return mShowCancel;
	}

	public SampleEditableStyleDialog showCancelButton(boolean isShow) {
		mShowCancel = isShow;
		if (mCancelButton != null) {
			mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	public boolean isShowContentText() {
		return mShowTitle;
	}

	public SampleEditableStyleDialog setCancelClickListener(
			ImageStyleDialogOnClickListener listener) {
		mCancelClickListener = listener;
		return this;
	}

	public SampleEditableStyleDialog setConfirmClickListener(
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
				mCancelClickListener.onClick(SampleEditableStyleDialog.this);
			} else {
				dismiss();
			}
		} else if (v.getId() == R.id.makesure) {
			if (mConfirmClickListener != null) {
				mConfirmClickListener.onClick(SampleEditableStyleDialog.this);
			} else {
				//dismiss();
			}
		}else if (v.getId() == R.id.exit){
			dismiss();
		}
	}

}