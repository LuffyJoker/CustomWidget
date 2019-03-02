package com.peng.customwidget.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.peng.customwidget.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 */
public class DatePickerView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "DatePickerView";
    private static final int DEFAULT_MIN_YEAR = 1900;
    public LoopView yearLoopView;
    public LoopView monthLoopView;
    public LoopView dayLoopView;
    public View contentView;//root view

    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;
    private Context mContext;


    private int minYear = DEFAULT_MIN_YEAR;
    private int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
    private String dateChose = getStrDate();
    private int colorCenterLine = Color.parseColor("#32DA9B");
    private boolean textTips = true;


    List<String> yearList = new ArrayList();
    List<String> monthList = new ArrayList();
    List<String> dayList = new ArrayList();

    public DatePickerView(Context context) {
        super(context);
        init(context, null);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePickerView);
        colorCenterLine = a.getColor(R.styleable.DatePickerView_centerLineColor, colorCenterLine);
        textTips = a.getBoolean(R.styleable.DatePickerView_textTips, textTips);


        a.recycle();
        initView();

    }

    private void initView() {
        contentView = inflate(mContext, R.layout.date_picker_view, this);
        yearLoopView = (LoopView) contentView.findViewById(R.id.picker_year);
        monthLoopView = (LoopView) contentView.findViewById(R.id.picker_month);
        dayLoopView = (LoopView) contentView.findViewById(R.id.picker_day);

        yearLoopView.setCenterLineColor(colorCenterLine);
        monthLoopView.setCenterLineColor(colorCenterLine);
        dayLoopView.setCenterLineColor(colorCenterLine);

        contentView.findViewById(R.id.tips_layer).setVisibility(textTips ? View.VISIBLE : View.GONE);
        setDefaultDate(dateChose);

        //set checked listen
        yearLoopView.setLoopListener(new LoopView.LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                yearPos = item;
                initDayPickerView();
            }
        });
        monthLoopView.setLoopListener(new LoopView.LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                monthPos = item;
                initDayPickerView();
            }
        });
        dayLoopView.setLoopListener(new LoopView.LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                dayPos = item;
                Log.d(TAG, "onItemSelect dayPos item:" + item);
            }
        });

        initPickerViews(); // init year and month loop view
        initDayPickerView(); //init day loop view
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerViews() {

        int yearCount = maxYear - minYear;

        for (int i = 0; i < yearCount; i++) {
            yearList.add(format2LenStr(minYear + i));
        }

        for (int j = 0; j < 12; j++) {
            monthList.add(format2LenStr(j + 1));
        }

        yearLoopView.setDataList((ArrayList) yearList);
        yearLoopView.scrollToPosition(yearPos);

        monthLoopView.setDataList((ArrayList) monthList);
        monthLoopView.scrollToPosition(monthPos);
    }

    /**
     * Init day item
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        Calendar calendar = Calendar.getInstance();
        dayList = new ArrayList<String>();

        calendar.set(Calendar.YEAR, minYear + yearPos);
        calendar.set(Calendar.MONTH, monthPos);

        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }
        dayLoopView.setDataList((ArrayList) dayList);
        if (dayPos > dayList.size() - 1) {
            dayPos = dayList.size() - 1;
        }
        dayLoopView.scrollToPosition(dayPos);

        notifyDataChange();
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     *
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {
        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    /**
     * 设置选择的日期
     *
     * @param milliseconds
     */
    public void setSelectedDate(long milliseconds) {
        if (milliseconds != -1) {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTimeInMillis(milliseconds);
            yearPos = calendar.get(Calendar.YEAR) - minYear;
            monthPos = calendar.get(Calendar.MONTH);
            dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            invalidate();
        }
    }


    /**
     * set selected date position value when initView.
     *
     * @param dateStr
     */
    private void setDefaultDate(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {

            long milliseconds = getLongFromyyyyMMdd(dateStr);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);

            if (milliseconds != -1) {

                calendar.setTimeInMillis(milliseconds);
                yearPos = calendar.get(Calendar.YEAR) - minYear;
                monthPos = calendar.get(Calendar.MONTH);
                dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            }
        }
    }

    @Override
    public void onClick(View v) {


    }

    private void notifyDataChange() {
        if (null != mOnDatePickedListener) {

            int year = minYear + yearPos;
            int month = monthPos + 1;
            int day = dayPos + 1;
            StringBuffer sb = new StringBuffer();

            sb.append(String.valueOf(year));
            sb.append("-");
            sb.append(format2LenStr(month));
            sb.append("-");
            sb.append(format2LenStr(day));
            mOnDatePickedListener.onDatePickChange(year, month, day, sb.toString());
        }
    }

    public int getYear() {
        int year = minYear + yearPos;
        return year;
    }

    public int getMonth() {
        int month = monthPos + 1;
        return month;
    }

    public int getDayOfMonth() {
        int day = dayPos + 1;
        return day;
    }

    public void setOnDatePickedListener(OnDatePickedListener onDatePickedListener) {
        mOnDatePickedListener = onDatePickedListener;
    }

    private OnDatePickedListener mOnDatePickedListener;

    public interface OnDatePickedListener {

        /**
         * Listener when date has been checked
         *
         * @param year
         * @param month
         * @param day
         * @param dateDesc yyyy-MM-dd
         */
        void onDatePickChange(int year, int month, int day,
                              String dateDesc);
    }

    /**
     * get long from yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static long getLongFromyyyyMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    public static String getStrDate() {
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dd.format(new Date());
    }

}

