package com.peng.customwidget.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.peng.customwidget.R;


/**
 * create by Mr.Q on 2019/3/1.
 * 类介绍：
 *  用于 XML 布局中，一个配置Item，具体效果看 XML 布局预览图
 */
public class ConfigItemView extends FrameLayout {

    private TextView mTvName;
    private EditText mTvValue;
    private ImageView mIvIcon;
    private String mValue;
    private boolean enableEdit;

    public ConfigItemView(Context context) {
        this(context, null);
    }

    public ConfigItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConfigItemView);
        initView(context, typedArray);
        typedArray.recycle();
    }

    /**
     * @param context
     * @param typedArray
     */
    private void initView(Context context, TypedArray typedArray) {
        String name = typedArray.getString(R.styleable.ConfigItemView_configName);
        String value = typedArray.getString(R.styleable.ConfigItemView_configValue);
        String valueHint = typedArray.getString(R.styleable.ConfigItemView_configValueHint);
        int iconRes = typedArray.getResourceId(R.styleable.ConfigItemView_rightDrawable, -1);
        int nameColor = typedArray.getColor(R.styleable.ConfigItemView_configNameColor, context.getResources().getColor(R.color.color_1E1E1E));
        int valueColor = typedArray.getColor(R.styleable.ConfigItemView_configValueColor, context.getResources().getColor(R.color.color_1E1E1E));

        View root = LayoutInflater.from(context).inflate(R.layout.layout_config_item_view, null);
        addView(root, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mTvName = (TextView) root.findViewById(R.id.tv_name);
        mTvValue = (EditText) root.findViewById(R.id.tv_value);
        setValueEnableEdit(false);
        mTvValue.setHint(valueHint);

        mIvIcon = (ImageView) root.findViewById(R.id.iv_icon);


        mTvName.setText(name);
        mTvValue.setText(value);
        mTvName.setTextColor(nameColor);
        mTvValue.setTextColor(valueColor);

        if (iconRes == -1) {
            mIvIcon.setVisibility(INVISIBLE);
        } else {
            mIvIcon.setImageResource(iconRes);
            mIvIcon.setVisibility(VISIBLE);
        }
    }

    public String getName() {
        return mTvName.getText().toString().trim();
    }

    public String getValue() {
        return mTvValue.getText().toString().trim();
    }

    public TextView getNameTextView() {
        return mTvName;
    }

    public EditText getValueEditTextView() {
        return mTvValue;
    }

    /**
     * 设置value是否可以编辑
     *
     * @param enable
     */
    public void setValueEnableEdit(boolean enable) {
        if (!enable) {
            mTvValue.setCursorVisible(false);
            mTvValue.setFocusable(false);
            mTvValue.setFocusableInTouchMode(false);
        } else {
            mTvValue.setCursorVisible(true);
            mTvValue.setFocusable(true);
            mTvValue.setFocusableInTouchMode(true);
        }
        enableEdit = enable;
    }

    /**
     * 设置value提示值
     *
     * @param hint
     */
    public void setVauleHint(String hint) {
        mTvValue.setHint(hint);
    }

    public ImageView getRightImageView() {
        return mIvIcon;
    }

    public void setValue(String value) {
        mValue = value;
        mTvValue.setText(mValue);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!enableEdit) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
