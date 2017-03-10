package com.caro.smartmodule.viewComponent.EditText;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.caro.smartmodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 参考来自：https://github.com/wangshaolei/AutoFillEmailEditText
 * 自动补全 + dropdown gaizhao
 * modify:caro
 * use:etc:
 * List<String> filterList = new ArrayList<>();
 * for (int index=0;index<30;index++){
 * filterList.add("index "+index);
 * }
 * mAutoFillEmailEditText.configDOMAINS(filterList);
 */
public class FilterDropDownEditText extends AutoCompleteTextView {

    private static int DEFAULT_DROP_DOWN_KEY_COLOR = Color.parseColor("#7281a3");
    private static int DEFAULT_DROP_DOWN_BG = 0;
    private static boolean DEFAULT_DROP_DOWN_DIVIDER = true;
    private Drawable arrow_up, arrow_down;
    private boolean isShown = false;

    private static List<String> listData = new ArrayList<>();
    private DropDownAutoCompleteAdapter adapter;

    public FilterDropDownEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStyle(context, attrs);
        initWidget(context);
    }

    public FilterDropDownEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initStyle(context, attrs);
        initWidget(context);
    }

    private void initStyle(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FilterDropDownEditText, 0, 0);
        DEFAULT_DROP_DOWN_KEY_COLOR = typedArray.getColor(R.styleable.FilterDropDownEditText_AutoFillDropdownEditText_default_drop_down_key_color, DEFAULT_DROP_DOWN_KEY_COLOR);
        DEFAULT_DROP_DOWN_BG = typedArray.getResourceId(R.styleable.FilterDropDownEditText_AutoFillDropdownEditText_default_drop_down_bg, 0);
        DEFAULT_DROP_DOWN_DIVIDER = typedArray.getBoolean(R.styleable.FilterDropDownEditText_AutoFillDropdownEditText_default_drop_down_divider, true);
        arrow_up = typedArray.getDrawable(R.styleable.FilterDropDownEditText_AutoFillDropdownEditText_arrow_up);
        arrow_down = typedArray.getDrawable(R.styleable.FilterDropDownEditText_AutoFillDropdownEditText_arrow_down);
        typedArray.recycle();
    }

    public void configDOMAINS(List<String> filterList) {
        if (filterList == null || filterList.size() == 0) {
            return;
        }
        listData.clear();
        listData.addAll(filterList);
        adapter = new DropDownAutoCompleteAdapter(getContext(), R.layout.item_dropdown, listData);
        setAdapter(adapter);

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setText(listData.get(i));
            }
        });
        setThreshold(1);
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (v.getWidth() - ((EditText) v).getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (isShown) {
                            dismissDropDown();
                            isShown = false;
                            if (arrow_down != null) {
                                // 设置arrow 箭头
                                Drawable drawable = arrow_down;
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                setCompoundDrawables(null, null, drawable, null);
                            }

                        } else {
                            showDropDown();
                            isShown = true;
                            if (arrow_up != null) {
                                // 设置arrow箭头
                                Drawable drawable = arrow_up;
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                setCompoundDrawables(null, null, drawable, null);
                            }
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initWidget(final Context context) {
        if (arrow_up == null || arrow_down == null) {
            // throw new RuntimeException("please config your up and down arrow drawable");
        }
        DropDownAutoCompleteAdapter adapter = new DropDownAutoCompleteAdapter(context, R.layout.item_dropdown, listData);
        setAdapter(adapter);
        if (DEFAULT_DROP_DOWN_BG != 0) {
            setDropDownBackgroundResource(DEFAULT_DROP_DOWN_BG);
        }
        //设置默认arrow_down箭头
        if (arrow_down != null) {
            Drawable drawable = arrow_down;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            setCompoundDrawables(null, null, drawable, null);
        }


    }


    @Override
    protected void replaceText(CharSequence text) {

    }

    // TODO: 2017/2/18 过滤器。可设置你的过滤器
    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        String t = text.toString();
    }

    private class DropDownAutoCompleteAdapter extends ArrayAdapter<String> {
        public DropDownAutoCompleteAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dropdown, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_email);
            View divider = convertView.findViewById(R.id.divider);
            if (DEFAULT_DROP_DOWN_DIVIDER) {
                divider.setVisibility(VISIBLE);
            } else {
                divider.setVisibility(GONE);
            }
            if (DEFAULT_DROP_DOWN_KEY_COLOR != 0) {
                tv.setTextColor(DEFAULT_DROP_DOWN_KEY_COLOR);
            }

            tv.setText(getItem(position));
            return convertView;
        }
    }
}