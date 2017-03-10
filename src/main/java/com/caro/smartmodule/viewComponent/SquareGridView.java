package com.caro.smartmodule.viewComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 用于嵌套在SrcollView中显示不全的listView extends
 * com.caro.commonWidget.GridViewWithHeaderAndFooter
 * 
 * @author caro extends com.caro.commonWidget.GridViewWithHeaderAndFooter or
 *         GridView
 */
public class SquareGridView extends GridViewWithHeaderAndFooter {

	public SquareGridView(Context context, AttributeSet attrs) {

		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SquareGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);// AT_MOST
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
