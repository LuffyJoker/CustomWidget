package com.peng.customwidget.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.peng.customwidget.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 */
public class CustomChartView extends View {

    private int width;
    private int height;
    private List<Integer> valueListData;
    private List<String> dateListData;
    private List<String> practicalData;
    private int valueMaxHeight;
    private int valueMinHeight;
    float[] mLimitLineSegmentsBuffer = new float[4];
    protected RectF mLimitLineClippingRect = new RectF();

    public void setValueListData(List<Integer> valueListData) {
        this.valueListData = valueListData;
    }

    public void setDateListData(List<String> dateListData) {
        this.dateListData = dateListData;
    }

    public void setPracticalData(List<String> practicalData) {
        this.practicalData = practicalData;
    }

    /**
     * 实现虚线效果
     */
    private DashPathEffect mDashPathEffect = null;
    private List<LimitLine> limitLines;
    private int xIntervalDistance;
    private float[] position;


    public CustomChartView(Context context) {
        super(context);
        initData();
    }

    public CustomChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public CustomChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        valueListData = new ArrayList<>();
        valueListData.add(105);
        valueListData.add(125);
        valueListData.add(145);

        dateListData = new ArrayList<>();
        dateListData.add("12/11");
        dateListData.add("1/20");
        dateListData.add("5/2");
        dateListData.add("5/31");
        dateListData.add("7/24");

        practicalData = new ArrayList<>();
        practicalData.add("111");
        practicalData.add("138");
        practicalData.add("127");
        practicalData.add("141");
        practicalData.add("140");


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        limitLines = new ArrayList<>();

        for (int i = 0; i < practicalData.size(); i++) {
            limitLines.add(new LimitLine(0.2f));
        }

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //坐标轴的最大、最小高度
        valueMaxHeight = height / 8 * 7;
        valueMinHeight = height / 8;

        //减去左边字体的距离，减去第一条线距离y轴的距离，减去右边没有的宽度
        xIntervalDistance = (width - 45 - 40 - 30) / practicalData.size();

        drawXYLine(canvas);

        drawDate(canvas);

        drawUnit(canvas);

        drawValueData(canvas);

        drawRoundRect(canvas);

        drawPractical(canvas);

        drawDashLines(canvas);

        viewRender(canvas);

        drawStandardLine(canvas);

        drawPracticalPoint(canvas);

        drawBrokenLine(canvas);

    }

    //画虚线
    public void drawDash(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#AFAFAF"));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new DashPathEffect(new float[]{0.2f, 0.4f}, 0));
        //计算日期之间的间距
        int xIntervalDistance = (width - 45 - 40 - 30) / dateListData.size();
        //画虚线
        for (int i = 0; i < dateListData.size(); i++) {
            canvas.drawLine(
                    85 + xIntervalDistance * i,
                    valueMaxHeight,
                    85 + xIntervalDistance * i,
                    valueMaxHeight / 8,
                    paint
            );
        }
    }

    //画纵坐标
    public void drawValueData(Canvas canvas) {
        int maxPracticalValue = 0;
        int maxXValue = 0;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#3E3E3E"));
        paint.setTextSize(15);

        for (int i = 0; i < practicalData.size(); i++) {
            if (parseInt(practicalData.get(i)) > maxPracticalValue) {
                maxPracticalValue = parseInt(practicalData.get(i));
            }
        }

        for (int i = 0; i < valueListData.size(); i++) {
            if (valueListData.get(i) > maxXValue) {
                maxXValue = valueListData.get(i);
            }
        }

        for (int i = 0; i < valueListData.size(); i++) {
            Rect mValueBound = new Rect();
            paint.getTextBounds(String.valueOf(valueListData.get(0)), 0, String.valueOf(valueListData.get(0)).length(), mValueBound);
            if (i == 1) {
                paint.setColor(Color.parseColor("#D4CCED"));
            } else {
                paint.setColor(Color.parseColor("#3E3E3E"));
            }
            if (maxXValue > maxPracticalValue) {
                canvas.drawText(
                        String.valueOf(valueListData.get(i)),
                        getPaddingLeft(),
                        (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxXValue * valueListData.get(i)) - 5,
                        paint);
            } else {
                canvas.drawText(
                        String.valueOf(valueListData.get(i)),
                        getPaddingLeft(),
                        (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxPracticalValue * valueListData.get(i)) - 5,
                        paint);
            }
        }
    }

    //画坐标轴
    public void drawXYLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#AFAFAF"));
        //x轴
        canvas.drawLine(45, valueMaxHeight, width, valueMaxHeight, paint);
        //y轴
        canvas.drawLine(45, 0, 45, valueMaxHeight, paint);
    }

    //画日期
    public void drawDate(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#333333"));
        paint.setTextSize(18);
        //计算日期之间的间距
        //减去左边字体的距离，减去第一条线距离y轴的距离，减去右边没有的宽度
        int xIntervalDistance = (width - 45 - 40 - 30) / dateListData.size();
        //画日期
        for (int i = 0; i < dateListData.size(); i++) {
            Rect mDateBound = new Rect();
            paint.getTextBounds(dateListData.get(i), 0, dateListData.get(i).length(), mDateBound);
            int textWidth = mDateBound.width();
            int textHeight = mDateBound.height();
            canvas.drawText(
                    dateListData.get(i),
                    (85 - textWidth / 2) + xIntervalDistance * i,
                    height - textHeight,
                    paint
            );
        }
    }

    //画实际值
    public void drawPractical(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#525252"));
        paint.setTextSize(15);
        //计算日期之间的间距
        //减去左边字体的距离，减去第一条线距离y轴的距离，减去右边没有的宽度
        int xIntervalDistance = (width - 45 - 40 - 30) / practicalData.size();
        //画实际值
        for (int i = 0; i < practicalData.size(); i++) {

            //超标的字体颜色
            if (parseInt(practicalData.get(i)) > valueListData.get(2)) {
                paint.setColor(Color.RED);
                //低于标准的字体颜色
            } else if (parseInt(practicalData.get(i)) < valueListData.get(0)) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(Color.parseColor("#525252"));
            }


            Rect mDateBound = new Rect();
            paint.getTextBounds(practicalData.get(i), 0, practicalData.get(i).length(), mDateBound);
            int textWidth = mDateBound.width();
            int textHeight = mDateBound.height();
            canvas.drawText(
                    practicalData.get(i),
                    (85 - textWidth / 2) + xIntervalDistance * i,
                    height - valueMaxHeight - textHeight - 5, //最后一个减去15是为了让实际值被矩形框包住
                    paint
            );
        }
    }

    //画圆角矩形框
    public void drawRoundRect(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setTextSize(15);
        paint.setColor(Color.parseColor("#525252"));
        int xIntervalDistance = (width - 45 - 40 - 30) / practicalData.size();
        for (int i = 0; i < practicalData.size(); i++) {

            if (parseInt(practicalData.get(i)) > valueListData.get(2)) {
                paint.setColor(Color.RED);
                //低于标准的字体颜色
            } else if (parseInt(practicalData.get(i)) < valueListData.get(0)) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(Color.parseColor("#525252"));
            }

            Rect mDateBound = new Rect();
            if (practicalData.get(i).length() < 4) {
                //为了限制圆角矩形的最小宽度为4个字符的长度
                paint.getTextBounds("1234", 0, 4, mDateBound);
            } else {
                paint.getTextBounds(practicalData.get(i), 0, practicalData.get(i).length(), mDateBound);
            }
            //paint.getTextBounds(practicalData.get(i), 0, practicalData.get(i).length(), mDateBound);
            int textWidth = mDateBound.width();
            int textHeight = mDateBound.height();
            RectF oval3 = new RectF(
                    (85 - textWidth / 2) + xIntervalDistance * i - 5, //左上右下
                    height - valueMaxHeight - textHeight - 15 - 7,
                    (85 + textWidth / 2) + xIntervalDistance * i + 5,
                    height - valueMaxHeight - 15 + 5);// 设置个新的长方形
            paint.setStyle(Paint.Style.STROKE);
            //第二个参数是x半径，第三个参数是y半径
//            canvas.drawRoundRect(oval3, 5, 5, paint);
            canvas.drawRoundRect(oval3, textHeight / 2 + 5, textHeight / 2 + 5, paint);
        }
    }

    /**
     * 画单位
     *
     * @param canvas
     */
    public void drawUnit(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setTextSize(15);
        paint.setColor(Color.parseColor("#3E3E3E"));
        Rect mTextBound = new Rect();
        paint.getTextBounds("g/L", 0, String.valueOf(valueListData.get(0)).length(), mTextBound);
        canvas.drawText("g/L", getPaddingLeft(), valueMaxHeight - mTextBound.height(), paint);
    }

    //画具体值在坐标系中的点
    public void drawPracticalPoint(Canvas canvas) {

        int maxPracticalValue = 0;
        int maxXValue = 0;

        for (int i = 0; i < practicalData.size(); i++) {
            if (parseInt(practicalData.get(i)) > maxPracticalValue) {
                maxPracticalValue = parseInt(practicalData.get(i));
            }
        }

        for (int i = 0; i < valueListData.size(); i++) {
            if (valueListData.get(i) > maxXValue) {
                maxXValue = valueListData.get(i);
            }
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#32DA9B"));

        //画点
        for (int i = 0; i < practicalData.size(); i++) {

            if (maxXValue > maxPracticalValue) {

                canvas.drawCircle(
                        getPaddingLeft() + 85 + xIntervalDistance * i,
                        (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxXValue * parseInt(practicalData.get(i))) - 10,
                        5,
                        paint
                );

            } else {

                canvas.drawCircle(
                        getPaddingLeft() + 85 + xIntervalDistance * i,
                        (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxPracticalValue * parseInt(practicalData.get(i))) - 10,
                        5,
                        paint
                );
            }
        }
    }


    /**
     * 画一条虚线
     *
     * @param canvas
     * @param limitLine
     * @param position
     */
    public void renderLimitLine(Canvas canvas, LimitLine limitLine, float[] position) {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(limitLine.getLineColor());
        paint.setStrokeWidth(limitLine.getLineWidth());
        paint.setPathEffect(limitLine.getDashPathEffect());

        mLimitLineSegmentsBuffer[0] = position[0];
        mLimitLineSegmentsBuffer[1] = position[1];
        mLimitLineSegmentsBuffer[2] = position[2];
        mLimitLineSegmentsBuffer[3] = position[3];

        Path path = new Path();
        path.reset();
        path.moveTo(mLimitLineSegmentsBuffer[0], mLimitLineSegmentsBuffer[1]);
        path.lineTo(mLimitLineSegmentsBuffer[2], mLimitLineSegmentsBuffer[3]);

        canvas.drawPath(path, paint);
    }

    /**
     * 画出多条虚线，方法中 85 为第一条虚线的位置。距离View最最左边的位置
     *
     * @param canvas
     */
    public void drawDashLines(Canvas canvas) {

        if (limitLines == null || limitLines.size() <= 0)
            return;

        position = new float[4];

        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine limitLine = limitLines.get(i);
            limitLine.enableDashedLine(3f, 8f, 0);
            limitLine.setLineColor(getResources().getColor(R.color.color_32DA9B));

            position[0] = 85 + xIntervalDistance * i;
            position[1] = valueMinHeight;
            position[2] = 85 + xIntervalDistance * i;
            position[3] = valueMaxHeight;

            renderLimitLine(canvas, limitLine, position);

        }
    }

    //画折线，连接各个点
    public void drawBrokenLine(Canvas canvas) {

        int maxPracticalValue = 0;
        int maxXValue = 0;

        for (int i = 0; i < practicalData.size(); i++) {
            if (parseInt(practicalData.get(i)) > maxPracticalValue) {
                maxPracticalValue = parseInt(practicalData.get(i));
            }
        }

        for (int i = 0; i < valueListData.size(); i++) {
            if (valueListData.get(i) > maxXValue) {
                maxXValue = valueListData.get(i);
            }
        }

        if (limitLines == null || limitLines.size() <= 0) {
            return;
        }
        position = new float[4];

        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine limitLine = limitLines.get(i);
            limitLine.disableDashedLine();
            limitLine.setLineColor(getResources().getColor(R.color.colorPrimary));
            if (i != limitLines.size() - 1 && i < limitLines.size() - 1) {

                if (maxXValue > maxPracticalValue) {

                    position[0] = 85 + xIntervalDistance * i;
                    position[1] = (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxXValue * parseInt(practicalData.get(i))) - 10;
                    position[2] = 85 + xIntervalDistance * (i + 1);
                    position[3] = (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxXValue * parseInt(practicalData.get(i + 1))) - 10;


                } else {

                    position[0] = 85 + xIntervalDistance * i;
                    position[1] = (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxPracticalValue * parseInt(practicalData.get(i))) - 10;
                    position[2] = 85 + xIntervalDistance * (i + 1);
                    position[3] = (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxPracticalValue * parseInt(practicalData.get(i + 1))) - 10;
                }
            }
            renderLimitLine(canvas, limitLine, position);
        }
    }

    /**
     * 颜色渲染
     */
    public void viewRender(Canvas canvas) {

        int maxPracticalValue = 0;
        int maxXValue = 0;

        for (int i = 0; i < practicalData.size(); i++) {
            if (parseInt(practicalData.get(i)) > maxPracticalValue) {
                maxPracticalValue = parseInt(practicalData.get(i));
            }
        }

        for (int i = 0; i < valueListData.size(); i++) {
            if (valueListData.get(i) > maxXValue) {
                maxXValue = valueListData.get(i);
            }
        }

        Path path = new Path();
        path.reset();
        path.moveTo(85, valueMaxHeight);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.colorPrimary));

        if (maxXValue > maxPracticalValue) {
            for (int i = 0; i < practicalData.size(); i++) {
                path.lineTo(
                        85 + xIntervalDistance * i,
                        (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxXValue * parseInt(practicalData.get(i))) - 10);
            }
            path.lineTo(85 + (practicalData.size() - 1) * xIntervalDistance, valueMaxHeight);

        } else {
            for (int i = 0; i < practicalData.size(); i++) {
                path.lineTo(
                        85 + xIntervalDistance * i,
                        (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxPracticalValue * parseInt(practicalData.get(i))) - 10);
            }
            path.lineTo(85 + (practicalData.size() - 1) * xIntervalDistance, valueMaxHeight);

        }


        //垂直线性渲染
        LinearGradient linearGradient = new LinearGradient(
                0,
                (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxPracticalValue * valueListData.get(valueListData.size() - 1)) - 10,
                0,
                height / 8 * 5,
                new int[]{Color.parseColor("#E7FAF3"), Color.TRANSPARENT}, null,
                Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        paint.setAntiAlias(true);
        canvas.drawPath(path, paint);
    }

    //画水平标识线
    public void drawStandardLine(Canvas canvas) {

        int maxPracticalValue = 0;
        int maxXValue = 0;

        for (int i = 0; i < practicalData.size(); i++) {
            if (parseInt(practicalData.get(i)) > maxPracticalValue) {
                maxPracticalValue = parseInt(practicalData.get(i));
            }
        }

        for (int i = 0; i < valueListData.size(); i++) {
            if (valueListData.get(i) > maxXValue) {
                maxXValue = valueListData.get(i);
            }
        }


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#D4CCED"));


        if (maxXValue > maxPracticalValue) {

            canvas.drawLine(
                    45,
                    (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxXValue * valueListData.get(1)) - 10,
                    width,
                    (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxXValue * valueListData.get(1)) - 10,
                    paint);
        } else {

            canvas.drawLine(
                    45,
                    (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxPracticalValue * valueListData.get(1)) - 10,
                    width,
                    (valueMaxHeight - (valueMaxHeight - valueMinHeight) / maxPracticalValue * valueListData.get(1)) - 10,
                    paint);

        }

    }

}


