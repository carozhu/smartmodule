package com.caro.smartmodule.viewComponent.Labviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caro.smartmodule.ConfigureManager;
import com.caro.smartmodule.R;

public class TitleBarView extends RelativeLayout {

    private View mRootView;
    private String title;
    private String leftBtnText;
    private String rightBtnText;
    private int titleTextColor = 0;
    private int titleBgColor = 0;
    private Drawable leftBtnBackground;
    private Drawable rightBtnBackground;
    private boolean showback;

    private float leftBtnTextSize;
    private float titleTextSize;
    private float rightBtnTextSize;
    private ImageView backIV;
    private ImageView rightImg;
    private TextView titleTv;
    private TextView rightText;
    private RelativeLayout titlebar;
    public TitleBarClickListener listener;
    public TitleBarRightTextClickListener rightTextClickListener;
    private LinearLayout right_ll;


    public TitleBarView(Context context) {
        super(context);


    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView = View.inflate(context, R.layout.titlebar_header, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Title_bar);
        init(context, typedArray);
    }


    /**
     * @param context
     * @param ta
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(final Context context, TypedArray ta) {
        title = ta.getString(R.styleable.Title_bar_titleText);
        titleTextColor = ta.getColor(R.styleable.Title_bar_titleColor, 0);
        titleBgColor = ta.getColor(R.styleable.Title_bar_titleBarBgColor, 0);
        titleTextSize = ta.getDimension(R.styleable.Title_bar_titleTextSize, 10);

        leftBtnText = ta.getString(R.styleable.Title_bar_leftBtnText);
        leftBtnBackground = ta.getDrawable(R.styleable.Title_bar_leftBtnBackground);
        leftBtnTextSize = ta.getDimension(R.styleable.Title_bar_leftBtnTextSize, 8);

        rightBtnText = ta.getString(R.styleable.Title_bar_rightBtnText);
        rightBtnBackground = ta.getDrawable(R.styleable.Title_bar_rightImg);
        rightBtnTextSize = ta.getDimension(R.styleable.Title_bar_rightBtnTextSize, 8);

        showback = ta.getBoolean(R.styleable.Title_bar_showback, false);

        titlebar = (RelativeLayout) mRootView.findViewById(R.id.titlebar);
        titleTv = (TextView) mRootView.findViewById(R.id.titlebar_title);
        backIV = (ImageView) mRootView.findViewById(R.id.back);
        rightImg = (ImageView) mRootView.findViewById(R.id.right_img);
        rightText = (TextView) mRootView.findViewById(R.id.right_text);
        right_ll = (LinearLayout) mRootView.findViewById(R.id.right_ll);

        titleTv.setText(title);
        titleTv.setTextColor(titleTextColor);
        //titleTv.setTextSize(titleTextSize);

        if (titleBgColor != 0) {
            titlebar.setBackgroundColor(titleBgColor);
        }else {
            titlebar.setBackgroundColor( ConfigureManager.getConfigureManager().getAppThemeColor());
        }

        if(rightBtnBackground == null){
            right_ll.setVisibility(GONE);
        }else {
            rightImg.setImageDrawable(rightBtnBackground);
        }


        if (rightBtnText == null){
            rightText.setVisibility(GONE);
        }else {
            rightText.setText(rightBtnText);
            rightText.setVisibility(VISIBLE);
            //rightText.setTextSize(rightBtnTextSize);
        }




        if (!showback) {
            backIV.setVisibility(GONE);
        } else {
            backIV.setVisibility(VISIBLE);
        }

        backIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.leftClick();


            }
        });


        rightImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)

                    listener.rightClick();
            }
        });

        rightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightTextClickListener != null)
                    rightTextClickListener.rightTextClick();


            }
        });




        ta.recycle();


    }


    /**
     *
     */
    public interface TitleBarClickListener {
        void leftClick();
        void rightClick();
    }

    public interface TitleBarRightTextClickListener {
        void rightTextClick();
    }
    /**
     * @param listener
     */
    public void setOnTitleBarClickListener(TitleBarClickListener listener) {
        this.listener = listener;
    }

    public void setOnTitleBarRightTextClickListener(TitleBarRightTextClickListener listener) {
        this.rightTextClickListener = listener;
    }

    /**
     * @param flag
     */
    public void showBack(boolean flag) {
        if (flag) {
            backIV.setVisibility(VISIBLE);
        } else {
            backIV.setVisibility(GONE);
        }
    }

    /**
     * @param resid
     */
    public void showBackImage(int resid) {
        backIV.setImageResource(resid);
    }

    /**
     * @param flag
     */
    public void setRightBtnVisable(boolean flag) {
        if (flag) {
            rightImg.setVisibility(VISIBLE);
        } else {
            rightImg.setVisibility(GONE);
        }
    }

    public void setRightImgBackground(int resid) {
        rightImg.setVisibility(VISIBLE);
        rightText.setVisibility(GONE);
        rightImg.setImageResource(resid);
    }


    /**
     * @param
     * @param title
     * @IdRes titlebar_title
     */
    public void setTitle(String title) {
        if (titleTv != null) {
            if (title != null) {
                titleTv.setText(title);
            }

        }
    }


    /**
     * 设置titlebar color
     * @param titleBgColor
     */
    public void setTitleBarBackgroud(String titleBgColor) {
        if (titlebar != null) {
            titlebar.setBackgroundColor(Color.parseColor(titleBgColor));

        }
    }

    public void setTitleBarBackgroud(int titleBgColor) {
        if (titlebar != null) {
            titlebar.setBackgroundColor(titleBgColor);

        }
    }
}
