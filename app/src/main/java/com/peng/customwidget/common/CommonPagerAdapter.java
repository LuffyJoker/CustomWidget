package com.peng.customwidget.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 *      ViewPager 的 adapter，带 title
 */
public class CommonPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private String[] mPagerTitle;

    public CommonPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] pagerTitle) {
        super(fm);
        mFragments = fragments;
        mPagerTitle = pagerTitle;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPagerTitle[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
