package com.peng.customwidget.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * create by Mr.Q on 2019/3/2.
 * 类介绍：
 *      ViewPager 的 adapter，不带 title
 */
public class CommonPagerAdapterNoTitle extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    public CommonPagerAdapterNoTitle(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
