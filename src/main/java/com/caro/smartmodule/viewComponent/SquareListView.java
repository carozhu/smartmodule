package com.caro.smartmodule.viewComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 用于嵌套在SrcollView中显示不全的listView
 * @author caro
 *
 */
public class SquareListView extends ListView {

	public SquareListView(Context context, AttributeSet attrs) {

		super(context, attrs);
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
