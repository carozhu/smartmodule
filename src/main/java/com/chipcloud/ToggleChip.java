package com.chipcloud;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

public class ToggleChip extends CheckedTextView {


  public ToggleChip(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public ToggleChip(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ToggleChip(Context context) {
    super(context);
  }

  public void setLabel(String label){
    setText(label);
  }
}
