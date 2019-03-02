package com.peng.customwidget.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.peng.customwidget.R;


/**
 * create by Mr.Q on 2019/3/1.
 * 类介绍：
 */
public class NotifiCationItemView extends FrameLayout {

    ImageView image_icon;
    ImageView image_point;
    TextView textview_name;
    TextView textview_content;
    TextView textview_time;

    public NotifiCationItemView(Context context) {
        this(context, null);
    }

    public NotifiCationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NotifiCationItemView);
        initView(context, typedArray);
        typedArray.recycle();
    }

    private void initView(Context context, TypedArray typedArray) {
        String name = typedArray.getString(R.styleable.NotifiCationItemView_notificationName);
        String content = typedArray.getString(R.styleable.NotifiCationItemView_notificationContent);
        String time = typedArray.getString(R.styleable.NotifiCationItemView_notificationDate);
        int icon = typedArray.getResourceId(R.styleable.NotifiCationItemView_iconDrawable, -1);

        int nameColor = typedArray.getColor(R.styleable.NotifiCationItemView_notificationNameColor, context.getResources().getColor(R.color.color_1E1E1E));
        int contentColor = typedArray.getColor(R.styleable.NotifiCationItemView_notificationContentColor, context.getResources().getColor(R.color.color_949494));

        Boolean pointshow = typedArray.getBoolean(R.styleable.NotifiCationItemView_is_new, false);

        View root = LayoutInflater.from(context).inflate(R.layout.layout_contification_item, null);
        addView(root, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        image_icon = (ImageView) root.findViewById(R.id.image_icon);
        image_point = (ImageView) root.findViewById(R.id.image_point);
        textview_name = (TextView) root.findViewById(R.id.textview_name);
        textview_content = (TextView) root.findViewById(R.id.textview_content);
        textview_time = (TextView) root.findViewById(R.id.textview_time);


        textview_name.setText(name);
        textview_content.setText(content);
        textview_time.setText(time);

        textview_name.setTextColor(nameColor);
        textview_content.setTextColor(contentColor);
        textview_time.setTextColor(contentColor);

        if (icon == -1) {
            image_icon.setVisibility(INVISIBLE);
        } else {
            image_icon.setImageResource(icon);
            image_icon.setVisibility(VISIBLE);
        }

        if (pointshow) {
            image_point.setVisibility(VISIBLE);
        } else {
            image_point.setVisibility(GONE);
        }
    }

    public String getName() {
        return textview_name.getText().toString().trim();
    }

    public String getContent() {
        return textview_content.getText().toString().trim();
    }

    public TextView getNameTextView() {
        return textview_name;
    }

    public TextView getContentTextView() {
        return textview_content;
    }

    public TextView getTimeView() {
        return textview_time;
    }

    public ImageView getImagePoint() {
        return image_point;
    }

    public ImageView geticonImageView() {
        return image_icon;
    }

    public void setShow(Boolean isshow) {
        if (isshow) {
            image_point.setVisibility(VISIBLE);
        } else {
            image_point.setVisibility(GONE);
        }

    }
}

