package com.peng.customwidget.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.peng.customwidget.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create by Mr.Q on 2019/3/1.
 * 类介绍：
 *      用于底部导航栏切换
 */
public class TabsView extends RadioGroup {

    private Context mContext;
    private int itemMinWidth;

    public TabsView(Context context) {
        this(context, null);
    }

    public TabsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabsView);
        int length = array.length();
        for (int i = 0; i < length; i++) {
            int type = array.getIndex(i);
            if (type == R.styleable.TabsView_tabs) {
                CharSequence[] textArray = array.getTextArray(i);
                mItems = Arrays.asList(textArray);
            } else if (type == R.styleable.TabsView_itemMinWidth) {
                itemMinWidth = array.getDimensionPixelSize(i, -1);
            }
        }
        array.recycle();

        init();
    }

    private List<CharSequence> mItems = new ArrayList<>();

    private void init() {
        setBackgroundResource(R.drawable.tv_selected_bg);
        setOrientation(HORIZONTAL);
        setPadding(1, 1, 1, 1);
        setGravity(Gravity.CENTER);
        addChild();
    }

    private void addChild() {
        for (int i = 0; i < mItems.size(); i++) {

            int resId;
            if (mItems.size() == 1) {
                resId = R.layout.tabs_view_center_child;
            } else {
                if (i == 0) {
                    resId = R.layout.tabs_view_left_child;
                } else if (i == mItems.size() - 1) {
                    resId = R.layout.tabs_view_right_child;
                } else {
                    resId = R.layout.tabs_view_center_child;
                }
            }

            RadioButton child = (RadioButton) inflate(mContext, resId, null);
            child.setId(getId(i));
//            child.setId(View.NO_ID);

            child.setGravity(Gravity.CENTER);
            if (itemMinWidth != -1) {
                child.setMinWidth(itemMinWidth);
            }
            child.setText(mItems.get(i));
            addView(child);
            if (i != 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
                layoutParams.setMargins(1, 0, 0, 0);
            }
        }
    }

    private int getId(int i) {
        return mContext.getResources().getIdentifier("item_" + i, "id", mContext.getPackageName());
    }

    public RadioButton getFirstButton() {
        return (RadioButton) this.getChildAt(0);
    }
}

