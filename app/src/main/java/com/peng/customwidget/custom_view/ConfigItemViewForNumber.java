package com.peng.customwidget.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.peng.customwidget.R;
import com.peng.customwidget.util.ArithmeticUtil;

import java.text.DecimalFormat;

/**
 * create by Mr.Q on 2019/3/1.
 * 类介绍：
 *
 * 用于加减本条目的数量，具体用法看 XML 图
 */
public class ConfigItemViewForNumber extends FrameLayout implements View.OnClickListener {

    private TextView mTvName;
    private TextView mTvValue;
    private ImageView mIvIcon;
    private ImageView mIvPlus;
    private ImageView mIvReduce;
    private double mValue;
    private String mName;
    private float mValueStep; //步长
    private DecimalFormat mDecimalFormat;
    private int mScale;
    private float mMinValue = Float.MIN_VALUE;
    private float mMaxValue = Float.MAX_VALUE;

    public ConfigItemViewForNumber(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ConfigItemViewForNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConfigItemViewForNumber);
        initView(context, typedArray);
        typedArray.recycle();
    }

    /**
     * @param context
     * @param typedArray
     */
    private void initView(Context context, TypedArray typedArray) {
        mName = typedArray.getString(R.styleable.ConfigItemViewForNumber_configName);
        mValue = typedArray.getFloat(R.styleable.ConfigItemViewForNumber_configNumberValue, 0.0f);
        mValueStep = typedArray.getFloat(R.styleable.ConfigItemViewForNumber_configValueStep, 1.0f);
        mMinValue = typedArray.getFloat(R.styleable.ConfigItemViewForNumber_configMinValue, 0.0f);
        mMaxValue = typedArray.getFloat(R.styleable.ConfigItemViewForNumber_configMaxValue, 0.0f);

        int nameColor = typedArray.getColor(R.styleable.ConfigItemViewForNumber_configNameColor, context.getResources().getColor(R.color.color_1E1E1E));
        int valueColor = typedArray.getColor(R.styleable.ConfigItemViewForNumber_configValueColor, context.getResources().getColor(R.color.color_1E1E1E));

        View root = LayoutInflater.from(context).inflate(R.layout.layout_config_item_view_for_number, null);
        addView(root, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mTvName = root.findViewById(R.id.tv_name);
        mTvValue = root.findViewById(R.id.tv_value);
        mIvPlus = root.findViewById(R.id.iv_plus);
        mIvReduce = root.findViewById(R.id.iv_reduce);
        mIvIcon = root.findViewById(R.id.iv_icon);
        mIvPlus.setOnClickListener(this);
        mIvReduce.setOnClickListener(this);

        mTvName.setText(mName);
        setValue(mValue);

        mTvName.setTextColor(nameColor);
        mTvValue.setTextColor(valueColor);


    }

    /**
     * 点击加减好时，value改变监听
     *
     * @param watcher
     */
    public void setValueChangeListener(TextWatcher watcher) {
        mTvValue.addTextChangedListener(watcher);
    }

    public String getName() {
        return mTvName.getText().toString().trim();
    }

    /**
     * @param scale 保留小叔位数
     * @return
     */
    public double getValue(int scale) {
        return ArithmeticUtil.round(mValue, scale);
    }

    public TextView getNameTextView() {
        return mTvName;
    }

    public TextView getValueTextView() {
        return mTvValue;
    }

    public ImageView getRightImageView() {
        return mIvIcon;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_plus:
                changeValue(true);
                break;
            case R.id.iv_reduce:
                changeValue(false);
                break;

        }

    }

    private void changeValue(boolean isPlus) {
        if (isPlus) {
            mValue = ArithmeticUtil.add(mValue, mValueStep);
        } else {
            mValue = ArithmeticUtil.sub(mValue, mValueStep);
        }
        if (mValue > mMaxValue) {
            mValue = mMaxValue;
        }
        if (mValue < mMinValue) {
            mValue = mMinValue;
        }
        setValue(mValue);

    }

    public void setValue(double value) {
        mValue = value;
        mTvValue.setText(String.valueOf(ArithmeticUtil.setScale(mValue + "", mScale)));
    }

    /**
     * 设置小数保留的位数
     *
     * @param scale
     */
    public void setScale(int scale) {
        mScale = scale;
        setValue(mValue);//refresh
    }

}

