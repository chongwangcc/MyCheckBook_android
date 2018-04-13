package com.ccmm.cc.mycheckbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


import com.ccmm.cc.mycheckbook.Adapter.CheckbookAddingFragmentPagerAdapter;
import com.ccmm.cc.mycheckbook.R;

/***
 * 添加记账本的 activity
 */
public class CheckbookAddingFragmentActivity extends FragmentActivity {

    private CheckbookAddingFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_add_checkbook);

        //1.绑定tab标签View控件
        pagerAdapter = new CheckbookAddingFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}