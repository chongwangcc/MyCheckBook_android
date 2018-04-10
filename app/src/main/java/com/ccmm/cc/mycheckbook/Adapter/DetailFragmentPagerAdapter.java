package com.ccmm.cc.mycheckbook.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;

import com.ccmm.cc.mycheckbook.Fragment.DetailFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/***
 * 明细展示的Fragment
 */
public class DetailFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"明细","类别报表","账户"};
    private Context context;
    private List<Fragment> fragmentList=new LinkedList<>();
    private FragmentManager fm;
    public DetailFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f=null;
        switch(position){
            case 0: //明细
                f=DetailFragment.newInstance();
                fragmentList.add(f);

            case 1: //类别
                f=DetailFragment.newInstance();
                fragmentList.add(f);
                break;
            case 2: //账号
                f=DetailFragment.newInstance();
                fragmentList.add(f);
                break;

        }
        return f;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        if(this.fragmentList != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragmentList){
                ft.remove(f);
            }
            ft.commit();
            fm.executePendingTransactions();
        }
        this.fragmentList = fragments;
        notifyDataSetChanged();
    }
}
