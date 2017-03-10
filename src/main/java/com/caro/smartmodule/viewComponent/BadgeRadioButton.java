package com.caro.smartmodule.viewComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.caro.smartmodule.R;

/**
 * Created by：hcs on 2016/10/13 15:09
 * e_mail：aaron1539@163.com
 * 数字+红点提示 by carozhu
 */
public class BadgeRadioButton extends RadioButton {

    private Paint mBgPaint;
    private Paint mTextPaint;
    private int mNum;

    /**
     * 需要绘制的数字大小
     * 默认大小为8sp
     */
    private float mTextSize = sp2px(8);

    /**
     * 默认背景颜色
     */
    private int mBgColor = 0xffff0000;

    /**
     * 默认字体颜色
     */
    private int mTextColor = 0xffffffff;

    //圆心坐标
    private int mX = 0;
    private int mY = 0;

    private boolean mTipOn = false;

    private Dot mDot;

    private class Dot {

        int color;

        int radius;

        int marginTop;
        int marginRight;

        Dot() {
            float density = getContext().getResources().getDisplayMetrics().density;
            radius = (int) (5 * density);
            marginTop = (int) (5 * density);
            marginRight = (int) (10 * density);

            color = getContext().getResources().getColor(R.color.md_deep_orange_500);
        }

    }

    public BadgeRadioButton(Context context) {
        super(context,null);
        init();
    }

    public BadgeRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
        init();
    }

    public BadgeRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
        init();
    }

    private void init(Context context, AttributeSet attrs){

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BadgeRadioButton);

        mTextColor = a.getColor(R.styleable.BadgeRadioButton_badge_txt_color,0xffffffff);
        mBgColor = a.getColor(R.styleable.BadgeRadioButton_badge_bg_color,0xfff74c31);//
        mTextSize = a.getDimensionPixelSize(R.styleable.BadgeRadioButton_badge_txt_size,(int)sp2px(8));
        mNum = a.getInteger(R.styleable.BadgeRadioButton_badge_txt_num,-1);

        //画背景圆形
        mBgPaint=new Paint();
        mBgPaint.setAntiAlias(true);

        //绘制数字
        mTextPaint=new Paint();
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setAntiAlias(true);

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mX = getWidth()*2/3+15;
        mY = getHeight()/4 -10;
        mTextPaint.setTextSize(mTextSize);

        if(mNum>0){

            mBgPaint.setColor(mBgColor);
            canvas.drawCircle(mX, mY, dp2px(9), mBgPaint);

            String str = null;
            if(mNum>99){
                str = "99+";
            }else{
                str=new String(mNum+"");
            }
            //绘制的文本
            mTextPaint.setColor(mTextColor);
            canvas.drawText(str,mX- mTextPaint.measureText(str) / 2,mY+mTextPaint.getFontMetrics().bottom*1.2f,mTextPaint);
        }else{
            mBgPaint.setColor(0x00000000);
            canvas.drawCircle(mX, mY, dp2px(9), mBgPaint);
            String str = "";
            //颜色
            mTextPaint.setColor(0x00ffffff);
            canvas.drawText(str,mX- mTextPaint.measureText(str) / 2,mY+mTextPaint.getFontMetrics().bottom*1.2f,mTextPaint);

            //数字优先级最高
            if (mTipOn) {
                float cx = getWidth() - mDot.marginRight - mDot.radius;
                float cy = mDot.marginTop + mDot.radius;

                Drawable drawableTop = getCompoundDrawables()[1];
                if (drawableTop != null) {
                    int drawableTopWidth = drawableTop.getIntrinsicWidth();
                    if (drawableTopWidth > 0) {
                        int dotLeft = getWidth() / 2 + drawableTopWidth / 2;
                        cx = dotLeft + mDot.radius;
                    }
                }


                //this.setText();
                Paint paint = getPaint();
                //save
                int tempColor = paint.getColor();

                paint.setColor(mDot.color);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, mDot.radius, paint);

                //restore
                paint.setColor(tempColor);
            }
        }



    }


    private void init() {
        mDot = new Dot();
    }

    public void setTipOn(boolean tip) {
        this.mTipOn = tip;

        invalidate();
    }

    public boolean isTipOn() {
        return mTipOn;
    }

    /**
     * 设置提醒数字
     * @param mNum
     */
    public void setmNum(int mNum) {
        this.mNum = mNum;
        invalidate();
    }


    public int getmNum(){
        return this.mNum;
    }

    /**
     * 清除提醒数字
     */
    public void clearNum(){
        this.mNum = -1;
        invalidate();
    }

    /**
     * 设置数字颜色
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    /**
     * 设置数字背景
     * @param bgColor
     */
    public void setBgColor(int bgColor) {
        this.mBgColor = bgColor;
    }

    /**
     * 设置提示数字大小，单位sp
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    /**
     * 将dp转为px
     * @param dp
     * @return
     */
    private int dp2px(int dp){
        int ret = 0;
        ret = (int) (dp * getContext().getResources().getDisplayMetrics().density);
        return ret;
    }

    /**
     * 将sp值转换为px值
     * @param sp
     * @return
     */
    private float sp2px(int sp) {
        float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }
}
