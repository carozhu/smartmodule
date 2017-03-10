package com.caro.smartmodule.viewComponent.textViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.caro.smartmodule.R;


/**
 * Created by drakeet(http://drakeet.me)
 * Date: 15/10/25 19:25
 */
public class LabelView extends TextView {

    private CharSequence mLeftText, mTopText, mRightText, mBottomText;
    private int mLeftTextAppearance, mTopTextAppearance, mRightTextAppearance,
            mBottomTextAppearance;
    private CharSequence mText;


    public LabelView(Context context) {
        this(context, null);
    }


    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.LabelView);
        mLeftText = a.getText(R.styleable.LabelView_leftText);
        mTopText = a.getText(R.styleable.LabelView_topText);
        mRightText = a.getText(R.styleable.LabelView_rightText);
        mBottomText = a.getText(R.styleable.LabelView_bottomText);

        mLeftTextAppearance = a.getResourceId(
                R.styleable.LabelView_leftTextAppearance, 0);
        mTopTextAppearance = a.getResourceId(
                R.styleable.LabelView_topTextAppearance, 0);
        mRightTextAppearance = a.getResourceId(
                R.styleable.LabelView_rightTextAppearance, 0);
        mBottomTextAppearance = a.getResourceId(
                R.styleable.LabelView_bottomTextAppearance, 0);
        int gravity = a.getInt(R.styleable.LabelView_android_gravity,
                Gravity.CENTER);

        a.recycle();

        setGravity(gravity);
        setText(super.getText());
    }


    @Override public void setText(CharSequence mainText, BufferType type) {
        super.setText(mainText, type);
        mText = mainText;
        CharSequence text = mainText;
        if (notNullOrEmpty(mLeftText)) {
            text = buildTextLeft(mLeftText.toString(), text,
                    mLeftTextAppearance);
        }
        if (notNullOrEmpty(mRightText)) {
            text = buildTextRight(text, mRightText.toString(),
                    mRightTextAppearance);
        }
        if (notNullOrEmpty(mTopText)) {
            text = new SpannableStringBuilder("\n").append(text);
            text = buildTextLeft(mTopText.toString(), text, mTopTextAppearance);
        }
        if (notNullOrEmpty(mBottomText)) {
            text = new SpannableStringBuilder(text).append("\n");
            text = buildTextRight(text, mBottomText.toString(),
                    mBottomTextAppearance);
        }
        if (notNullOrEmpty(text)) {
            super.setText(text, type);
        }
    }


    private CharSequence buildTextLeft(CharSequence head, CharSequence foot, int style) {
        SpannableString leftText = format(getContext(), head, style);
        SpannableStringBuilder builder = new SpannableStringBuilder(
                leftText).append(foot);
        return builder.subSequence(0, builder.length());
    }


    //todo
    private CharSequence buildTextRight(CharSequence head, CharSequence foot, int style) {
        SpannableString rightText = format(getContext(), foot, style);
        SpannableStringBuilder builder = new SpannableStringBuilder(
                head).append(rightText);
        return builder.subSequence(0, builder.length());
    }


    public SpannableString format(Context context, CharSequence text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0,
                text.length(), 0);
        return spannableString;
    }


    private boolean notNullOrEmpty(CharSequence leftText) {
        return leftText != null && leftText.length() > 0;
    }


    @Override public CharSequence getText() {
        return mText;
    }
}