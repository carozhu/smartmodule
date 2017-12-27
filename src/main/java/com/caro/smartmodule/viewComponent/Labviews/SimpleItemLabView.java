package com.caro.smartmodule.viewComponent.Labviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caro.smartmodule.R;
import com.caro.smartmodule.viewComponent.imageviews.CircleImageView;
import com.caro.smartmodule.viewComponent.textViews.BadgeView;

/**
 * Created by caro on 16/1/28.
 */
public class SimpleItemLabView extends LinearLayout {

    private View mRootView;
    private ImageView mIv_item_myview;
    private TextView mTv_item_myview;
    private TextView mTv_item_myMessage;
    private ImageView mIv_item_view_arrow;
    private ImageView rightIcon;
    private View line;



    public OnLabViewClickListener onLabViewClickListener;
    public interface OnLabViewClickListener{
       public void OnLabViewClick(View view);
    }

    public void setOnLabViewClickListener(OnLabViewClickListener onLabViewClickListener){
        this.onLabViewClickListener =onLabViewClickListener;
    }
    public SimpleItemLabView(Context context) {
        super(context);
    }



    public SimpleItemLabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView = View.inflate(context, R.layout.simple_item_labview, this);

        initView(context);
        initData(context, attrs);
    }

    private void initView(final  Context context) {
        mIv_item_myview = (ImageView) mRootView.findViewById(R.id.iv_usercenter_itemview);
        mTv_item_myview = (TextView) mRootView.findViewById(R.id.tv_item_myview);
        mTv_item_myMessage= (TextView) mRootView.findViewById(R.id.tv_item_message);
        mIv_item_view_arrow = (ImageView) mRootView.findViewById(R.id.iv_item_view_arrow);
        rightIcon= (CircleImageView) mRootView.findViewById(R.id.rightImg);
        line = (View)mRootView.findViewById(R.id.line);

    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.SimpItemLabView);
        CharSequence text = t.getText(R.styleable.SimpItemLabView_labtext);
        if(text!=null){
            mTv_item_myview.setText(text);
        }

        CharSequence infomessage = t.getText(R.styleable.SimpItemLabView_infomessage);
        if(infomessage!=null){
            mTv_item_myMessage.setText(infomessage);
        }else {
            mTv_item_myMessage.setText("");
        }

        Drawable drawable = t.getDrawable(R.styleable.SimpItemLabView_labicon);
        if(drawable!=null){
            mIv_item_myview.setImageDrawable(drawable);
            mIv_item_myview.setVisibility(VISIBLE);
        }else {
            mIv_item_myview.setVisibility(GONE);
        }

        boolean b = t.getBoolean(R.styleable.SimpItemLabView_isshowarrow, true);
        if(b){
            mIv_item_view_arrow.setVisibility(VISIBLE);
        }else {
            mIv_item_view_arrow.setVisibility(INVISIBLE);
        }

        boolean  showline = t.getBoolean(R.styleable.SimpItemLabView_showline,true);
        if (showline){
            line.setVisibility(VISIBLE);
        }else {
            line.setVisibility(GONE);
        }

        Drawable rightDrawable = t.getDrawable(R.styleable.SimpItemLabView_rightlabicon);
        if (rightDrawable!=null){
            rightIcon.setImageDrawable(rightDrawable);
            rightIcon.setVisibility(VISIBLE);
        }else {
            rightIcon.setVisibility(GONE);
        }

        CharSequence urlheader = t.getText(R.styleable.SimpItemLabView_rightlabiconurl);
        if (urlheader!=null){
          Glide.with(context).load(urlheader).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.icon_share).into(this.rightIcon);
        }



        t.recycle();
        mTv_item_myview.setTextColor(getResources().getColor(R.color.black));//default


    }

    public void setInfoMessge(String infoMessge){

        if (mTv_item_myMessage!=null){
            mTv_item_myMessage.setHint("");
            mTv_item_myMessage.setText(infoMessge);
        }
    }

    public void setInfoHintMessge(String hintMessge){

        if (mTv_item_myMessage!=null){
            mTv_item_myMessage.setHint(hintMessge);
        }
    }



    public String getInfomessageText(){

        return mTv_item_myMessage.getText().toString();
    }





    private BadgeView bvText;
    public void  setBageView(int number){
        if(bvText!=null){
            bvText.setText(String.valueOf(number));
        }else {
            bvText = new BadgeView(this.getContext(), mTv_item_myview);
            bvText.setText(String.valueOf(number));
            bvText.setTextColor(Color.WHITE);
            bvText.setTextSize(dip2px(this.getContext(), 4));
            bvText.setBadgeMargin(0,0);
            bvText.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认值
            bvText.show();
        }
    }



    public void hideTextBageView(){
        if(bvText!=null){
            if(bvText.isShown()){
                bvText.hide();
            }
        }
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public ImageView getRightIcon(){

        return rightIcon;
    }


    public void showarrow(boolean showArrow){
        if(showArrow){
            mIv_item_view_arrow.setVisibility(VISIBLE);
        }else {
            mIv_item_view_arrow.setVisibility(INVISIBLE);
        }
    }

    public void setLabText(String labText){
        mTv_item_myview.setText(labText);
    }

}
