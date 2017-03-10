package com.caro.smartmodule.viewComponent.ScrollViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 参考：http://www.cnblogs.com/lbangel/p/4335879.html
 * @author caro
 *仿朋友圈背景图片下拉
 *usage：参考链接或最下面注释布局文件代码
 */
public class ScrollDampView extends ScrollView {
	/** 该属性具体参数 怎么控制 未解！！！！ */
	private static final int LEN = 0xc8;
	/** 回弹时所用的时间 */
	private static final int DURATION = 200;
	/** 最大Y坐标 其值一般设定为Scroller对应控件的高度 */
	private static final int MAX_DY = 200;

	private Scroller mScroller;
	/** 阻尼系数 */
	private static final float OFFSET_RADIO = 2.5f;

	private float startY;
	private int imageViewH;

	private ImageView imageView;
	private boolean scrollerType;

	public ScrollDampView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public ScrollDampView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}

	public ScrollDampView(Context context) {
		super(context);

	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	float curY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (!mScroller.isFinished()) {
			return super.onTouchEvent(event);
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:// 变量赋初始值
			imageViewH = imageView.getHeight();
			startY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (imageView.isShown()) {
				float deltaY = (event.getY() - startY ) / OFFSET_RADIO;
				Log.i("syso", "deltaY: "+deltaY+" imageTop: "+imageView.getTop());
				//往下拉
				if (deltaY > 0 && deltaY <= imageView.getBottom() + LEN) {
					android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
					params.height = (int) (imageViewH + deltaY);// 改变高度
					imageView.setLayoutParams(params);
				}
				scrollerType = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			scrollerType = true;
			// 开始一个动画控制，由(startX , startY)在duration时间内前进(dx,dy)个单位
			// ，即到达坐标为(startX+dx , startY+dy)处
			mScroller.startScroll(imageView.getLeft(), imageView.getBottom(),
					0 - imageView.getLeft(),
					imageViewH - imageView.getBottom(), DURATION);
			invalidate();
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	// //该mScroller针对于imageView的变化
	// 被父视图调用，用于必要时候对其子视图的值（mScrollX和mScrollY）
	// 进行更新。典型的情况如：父视图中某个子视图使用一个Scroller对象来实现滚动操作，会使得此方法被调用。
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();// ImageView的当前Y坐标
			imageView.layout(0, 0, x + imageView.getWidth(), y);// 使imageView本身做相应变化
			invalidate();
			// 滑动还未完成时，手指抬起时，当前y坐标大于其实imageView的高度时
			// 设定imageView的布局参数 作用：使除imageView之外的控件做相应变化
			if (!mScroller.isFinished() && scrollerType && y > MAX_DY) {
				android.view.ViewGroup.LayoutParams params = imageView
						.getLayoutParams();
				params.height = y;
				imageView.setLayoutParams(params);
			}
		}
	}
}

//eg:布局文件：
//<com.caro.commonWidget.ScrollDampView xmlns:android="http://schemas.android.com/apk/res/android"
//    android:id="@+id/dampview"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent" >
//
//    <LinearLayout
//        android:layout_width="fill_parent"
//        android:layout_height="fill_parent"
//        android:orientation="vertical" >
//
//        <!--此处必须设置imageview的scaleType为centerCrop,当然在代码中设置也可以-->
//        <ImageView
//            android:id="@+id/img"
//            android:layout_width="match_parent"
//            android:layout_height="160dp"
//            android:scaleType="centerCrop"
//            android:src="@drawable/image" />
//
//        <ImageView
//            android:id="@+id/iv_photo"
//            android:layout_width="64dp"
//            android:layout_height="64dp"
//            android:layout_marginTop="-32dp"
//            android:src="@drawable/ic_launcher" 
//            />
//
//    </LinearLayout>
//
//</com.caro.commonWidget.ScrollDampView>
