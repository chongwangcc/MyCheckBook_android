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
import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.MyControl.ChooseMonthDialog;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;

import java.util.List;

/**
 * 打卡一个记账本后的主页
 */
public class CheckbookMainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView year_TextView ;
    private TextView tv_totalIncome ;
    private TextView tv_totalSpend ;
    private Button month_button ;
    private DetailFragmentPagerAdapter pagerAdapter;
    private ChooseMonthDialog selfDialog;
    CheckbookEntity checkbook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook_main);

        //1.获得传递过来的checkbook数据
        checkbook = CheckbookTools.getSelectedCheckbook();

        //2.设置ToolBar
        settingToolBar(checkbook);

        //3.设置概况栏
        tv_totalIncome=findViewById(R.id.textView9);
        tv_totalSpend=findViewById(R.id.textView10);

        //4.设置明细Fragment
        pagerAdapter = new DetailFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager =  findViewById(R.id.checkbook_main_viewpager);
        viewPager.setAdapter(pagerAdapter);

        tabLayout =  findViewById(R.id.tabLayout2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        //5.设置年，月显示功能
        year_TextView =  findViewById(R.id.textView6);
        month_button =  findViewById(R.id.select_date);
        year_TextView.setText(CheckDetailsTools.getDetails_year());
        month_button.setText(CheckDetailsTools.getDetals_month());



    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    /***
     * 更新数据
     */
    private void updateData(){
        year_TextView.setText(CheckDetailsTools.getDetails_year());
        month_button.setText(CheckDetailsTools.getDetals_month());

        pagerAdapter.notifyDataSetChanged();

        List<CheckDetailBean> goupbean = CheckDetailsTools.getAllDetailsInMonth(checkbook.getCheckbookID(),
                                                                              year_TextView.getText().toString(),
                                                                              month_button.getText().toString());
        float total_income=0;
        float total_spent=0;
        for(CheckDetailBean group:goupbean){
            switch (group.getBalanceType()){
                case BalanceName.Income:
                    total_income+=group.getMoney();
                    break;
                case BalanceName.Expend:
                    total_spent+=group.getMoney();
                    break;
            }
        }
        tv_totalIncome.setText("+"+String.format("%.2f", total_income));
        tv_totalSpend.setText("-"+String.format("%.2f", total_spent));
    }

    /***
     * 设置工具栏
     * @param checkbook
     */
    private void settingToolBar(CheckbookEntity checkbook){
        toolbar =  findViewById(R.id.checkbook_main_toolbar);
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
        ImageView iv = findViewById(R.id.profile_imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 点击profile弹出配置界面
                Toast.makeText(CheckbookMainActivity.this,"profile clicked",Toast.LENGTH_SHORT).show();
            }
        });
        Button  button =findViewById(R.id.select_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfDialog = new ChooseMonthDialog(v.getContext());
                selfDialog.setYearStr(CheckDetailsTools.getDetails_year());
                selfDialog.setMonthStr(CheckDetailsTools.getDetals_month());
                selfDialog.show();
                selfDialog.setYesOnclickListener("确定", new ChooseMonthDialog.onYesOnclickListener() {
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
                selfDialog.setNoOnclickListener("取消", new ChooseMonthDialog.onNoOnclickListener() {
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
