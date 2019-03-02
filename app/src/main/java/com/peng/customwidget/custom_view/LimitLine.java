package com.peng.customwidget.custom_view;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 */
public class LimitLine {

    /**
     * limit / maximum (the y-value or xIndex)
     */
    private float mLimit = 0f;

    /**
     * 虚线的宽度
     */
    private float mLineWidth = 2f;

    /**
     * 虚线的颜色
     */
    private int mLineColor = Color.rgb(237, 91, 91);

    /**
     * 虚线的样式
     */
    private Paint.Style mTextStyle = Paint.Style.FILL_AND_STROKE;

    /**
     * 画在虚线旁边的标签字符串
     */
    private String mLabel = "";

    /**
     * 实现虚线效果的对象
     */
    private DashPathEffect mDashPathEffect = null;

    /**
     * 申明虚线标签的位子，即标签相对虚线的位置
     */
    private LimitLabelPosition mLabelPosition = LimitLabelPosition.RIGHT_TOP;

    /**
     * 枚举类型，标示标签的位置
     */
    public enum LimitLabelPosition {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * 构造方法，带位置参数
     *
     * @param limit 参数标示虚线在 X轴 或 Y轴的那个地方显示
     */
    public LimitLine(float limit) {
        mLimit = limit;
    }

    /**
     * 构造方法，带位置参数、标签
     */
    public LimitLine(float limit, String label) {
        mLimit = limit;
        mLabel = label;
    }

    /**
     * 返回虚线在 X轴 或 Y轴 的位置
     *
     * @return
     */
    public float getLimit() {
        return mLimit;
    }

    /**
     * set the line width of the chart (min = 0.2f, max = 12f); default 2f NOTE:
     * thinner line == better performance, thicker line == worse performance
     * <p>
     * 设置虚线的宽度，最小值为0.2f，最大值为12f。
     * 线越细，展示效果越好
     *
     * @param width
     */
    public void setLineWidth(float width) {

        if (width < 0.2f)
            width = 0.2f;
        if (width > 12.0f)
            width = 12.0f;
    }

    /**
     * 获取线的宽度
     *
     * @return
     */
    public float getLineWidth() {
        return mLineWidth;
    }

    /**
     * 设置线的颜色，需要使用 getResources().getColor(...) 获取颜色值
     *
     * @param color
     */
    public void setLineColor(int color) {
        mLineColor = color;
    }

    /**
     * 获取线的颜色
     *
     * @return
     */
    public int getLineColor() {
        return mLineColor;
    }

    /**
     * 允许线以虚线的形式画出, 例如 "- - - - - -"
     *
     * @param lineLength  每一个虚线点的长度
     * @param spaceLength 两个虚线点之间的间距
     * @param phase       偏移量 (一般使用0)
     */
    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        mDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    /**
     * 不能以虚线的形式画
     */
    public void disableDashedLine() {
        mDashPathEffect = null;
    }

    /**
     * 判断是否可以画虚线
     *
     * @return
     */
    public boolean isDashedLineEnabled() {
        return mDashPathEffect == null ? false : true;
    }

    /**
     * 返回虚线效果对象
     *
     * @return
     */
    public DashPathEffect getDashPathEffect() {
        return mDashPathEffect;
    }

    /**
     * 设置画在线旁边的字体样式
     * 默认为: Paint.Style.FILL_AND_STROKE
     *
     * @param style
     */
    public void setTextStyle(Paint.Style style) {
        this.mTextStyle = style;
    }

    /**
     * 返回当前字体样式
     *
     * @return
     */
    public Paint.Style getTextStyle() {
        return mTextStyle;
    }

    /**
     * 设置标签的位置
     *
     * @param pos
     */
    public void setLabelPosition(LimitLabelPosition pos) {
        mLabelPosition = pos;
    }

    /**
     * 返回标签的位置
     *
     * @return
     */
    public LimitLabelPosition getLabelPosition() {
        return mLabelPosition;
    }

    /**
     * 设置标签的内容
     *
     * @param label
     */
    public void setLabel(String label) {
        mLabel = label;
    }

    /**
     * 获取标签的内容
     *
     * @return
     */
    public String getLabel() {
        return mLabel;
    }
}


