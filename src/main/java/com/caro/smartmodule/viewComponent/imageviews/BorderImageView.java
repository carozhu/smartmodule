package com.caro.smartmodule.viewComponent.imageviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 带边框ImageView
 * @author caro
 *
 */
public class BorderImageView extends ImageView {
	public BorderImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BorderImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 这里就是重写的方法了，想画什么形状自己动手
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// 画边框

		//获取控件需要重新绘制的区域  
        Rect rect=canvas.getClipBounds();  
        rect.bottom-=4;  
        rect.right-=4; 
        rect.left+=4;
        rect.top+=4;
        Paint paint=new Paint();  
        paint.setColor(Color.WHITE);  
        paint.setStyle(Paint.Style.STROKE);  
        paint.setStrokeWidth(4);  
        canvas.drawRect(rect, paint);
	}
}