package com.peng.customwidget.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 */
public class EditBg extends View {
    public EditBg(Context context) {
        super(context);
    }

    public EditBg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditBg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setColor(Color.parseColor("#32DA9B"));
        canvas.drawLine(0,0,0,height,paint);
        canvas.drawLine(0,height,width,height,paint);
        canvas.drawLine(width,height,width,0,paint);
    }
}

