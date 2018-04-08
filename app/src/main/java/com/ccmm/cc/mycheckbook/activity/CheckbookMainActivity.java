package com.ccmm.cc.mycheckbook.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Adapter.DetailFragmentPagerAdapter;
import com.ccmm.cc.mycheckbook.MyControl.SelfDialog;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;

import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class CheckbookMainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView year_TextView ;
    private Button month_button ;
    DetailFragmentPagerAdapter pagerAdapter;
    SelfDialog selfDialog;
    List<DetailGroupBean> list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook_main);

        //1.获得传递过来的checkbook数据
        CheckbookEntity checkbook = CheckbookTools.getSelectedCheckbook();

        //2.设置ToolBar
        settingToolBar(checkbook);

        //3.设置概况栏

        //4.设置明细Fragment
        pagerAdapter = new DetailFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.checkbook_main_viewpager);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        //5.设置年，月显示功能
        year_TextView = (TextView) findViewById(R.id.textView6);
        month_button = (Button) findViewById(R.id.select_date);
        year_TextView.setText(CheckDetailsTools.getDetails_year());
        month_button.setText(CheckDetailsTools.getDetals_month());

    }

    private void updateData(){
        year_TextView.setText(CheckDetailsTools.getDetails_year());
        month_button.setText(CheckDetailsTools.getDetals_month());
    }

    private void settingToolBar(CheckbookEntity checkbook){
        toolbar = (Toolbar) findViewById(R.id.checkbook_main_toolbar);
        toolbar.setTitle("记账本");
        toolbar.setSubtitle(checkbook.getTitle());
        setSupportActionBar(toolbar);//设置导航图标要在setSupportActionBar方法之后
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView iv = (ImageView)findViewById(R.id.profile_imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckbookMainActivity.this,"profile clicked",Toast.LENGTH_SHORT).show();
            }
        });
        Button  button =(Button)findViewById(R.id.select_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfDialog = new SelfDialog(v.getContext());
                selfDialog.setYearStr(CheckDetailsTools.getDetails_year());
                selfDialog.setMonthStr(CheckDetailsTools.getDetals_month());
                selfDialog.show();
                selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        //1.更新数据
                        CheckDetailsTools.setDetails_year(selfDialog.getYearStr());
                        CheckDetailsTools.setDetals_month(selfDialog.getMonthStr());
                        //2.更新控件上显示
                        updateData();
                        //3.关闭对话框
                        selfDialog.dismiss();

                    }
                });
                selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        ////Toast.makeText(selfDialog.getContext(),"点击了--取消--按钮",Toast.LENGTH_LONG).show();
                        selfDialog.dismiss();
                    }
                });
            }
        });
    }
}
