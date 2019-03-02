package com.peng.customwidget.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.peng.customwidget.R;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 *      显示带有图片的 Toast
 */
public class ToastUtil {

    private static Toast toast;

    /**
     * 显示有 image 的 toast
     *
     * @param tvStr
     * @param imageResource
     * @return
     */
    public static Toast showToastWithImg(Context context, final String tvStr, final int imageResource) {
        if (toast == null) {
            toast = new Toast(context);
        }

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundResource(R.drawable.shape_rounded_corners_4);
        root.setAlpha(0.7f);
        root.setGravity(Gravity.CENTER);
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();


        ImageView iv = new ImageView(context);
        int ivPadding = (int) (52.0f / 1275 * screenHeight);
        iv.setPadding(0, ivPadding, 0, ivPadding);
        root.addView(iv);

        TextView tv = new TextView(context);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(0xffffffff);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.gravity = Gravity.CENTER;
        if (imageResource > 0) {
            int width = (int) (266.0f / 720 * screenWidth);
            root.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
            root.setMinimumWidth(width);
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(imageResource);

            int bottom = (int) (36.0f / 1275 * screenHeight);
            tvParams.setMargins(0, 0, 0, bottom);

        } else {
            int tvPadding = (int) (30.0f / 1275 * screenHeight);
            tvParams.setMargins(0, tvPadding, 0, tvPadding);
            iv.setVisibility(View.GONE);

        }
        int padding = (int) (25.0f / 720 * screenWidth);
        root.setPadding(padding, 0, padding, 0);

        root.addView(tv, tvParams);
        tv.setText(TextUtils.isEmpty(tvStr) ? "" : tvStr);

        toast.setView(root);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }
}
