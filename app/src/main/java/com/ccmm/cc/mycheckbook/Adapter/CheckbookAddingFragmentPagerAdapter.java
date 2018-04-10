package com.ccmm.cc.mycheckbook.Adapter;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.ccmm.cc.mycheckbook.Fragment.CheckbookAddingFragment;

/***
 * 添加记账本Fragment的Page 适配器
 *
 */
public class CheckbookAddingFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int  PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"加入","新建"};
    private Context context;

    public CheckbookAddingFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return CheckbookAddingFragment.newInstance(position + 1);
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