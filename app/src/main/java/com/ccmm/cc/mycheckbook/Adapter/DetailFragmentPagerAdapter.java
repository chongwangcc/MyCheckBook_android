package com.ccmm.cc.mycheckbook.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccmm.cc.mycheckbook.Fragment.DetailFragment;
import com.ccmm.cc.mycheckbook.Fragment.PageFragment;

/**
 * Created by cc on 2018/4/6.
 */

public class DetailFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"明细","类别说明","账户"};
    private Context context;

    public DetailFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: //明细
                return DetailFragment.newInstance();
            case 1: //类别
                break;
            case 2: //账号
                break;

        }
        return PageFragment.newInstance(0);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
