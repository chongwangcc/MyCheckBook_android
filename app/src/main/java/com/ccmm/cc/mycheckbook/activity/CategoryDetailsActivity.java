package com.ccmm.cc.mycheckbook.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Adapter.CategoryDetailsListAdapter;
import com.ccmm.cc.mycheckbook.Adapter.CategoryLeaderBoadAdapter;
import com.ccmm.cc.mycheckbook.Adapter.DetailsInnerListAdapter;
import com.ccmm.cc.mycheckbook.MyControl.ChooseMonthDialog;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.LoginTools;

public class CategoryDetailsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar toolbar;
    private ImageView iv_icon;
    private TextView tv_description;
    private TextView tv_money;
    private TextView tv_month;
    private ListView lv_details;
    private String title;
    DetailGroupBean bean;
    private double total_income_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        //1.获得数据
        Intent intent = getIntent();
        title = (String)intent.getStringExtra("title");
        if(title ==null) title = "详情";
        bean = (DetailGroupBean)intent.getSerializableExtra("detailGroup");
        total_income_money = (double) intent.getDoubleExtra("total",1.0);
        //2.设置toolbar
        toolbar =  findViewById(R.id.toolbar4);
        settingToolBar();

        //3.设置类别汇总数据
        iv_icon = findViewById(R.id.main1).findViewById(R.id.detail_pic);
        tv_description = findViewById(R.id.main1).findViewById(R.id.detail_content);
        tv_money = findViewById(R.id.main1).findViewById(R.id.detail_money);
        tv_month = findViewById(R.id.textView15);
        settingCategorySum();
        //4.设置类别明细数据
        lv_details = findViewById(R.id.dd_dynamic);
        settingCategoryDetails();


    }
    /***
     * 设置工具栏
     * @param
     */
    private void settingToolBar() {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);//设置导航图标要在setSupportActionBar方法之后
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /***
     *设置类别汇总数据
     */
    private void settingCategorySum(){
        //1.设置图标
        Drawable drawable = getDrawable(CategoriesIconTool.getDrawableIndex(bean.getCategoryType()));
        drawable = CategoriesIconTool.changeDrawableByBalanceType(drawable, bean.getBalanceType());
        iv_icon.setImageDrawable(drawable);

        //3.设置money
        double money = bean.getMoney();
        tv_money.setText(String.format("%.2f", money));

        //2.设置类别 百分比
        String decrption=bean.getCategoryType();
        double percent = 0;
        percent = money/ total_income_money;
        String percent_str=String.format("%.2f", percent*100);
        decrption+="    "+percent_str +"%";
        tv_description.setText(decrption);

        //4.设置年月
        String date_month="";
        date_month=bean.getYear()+"-"+bean.getMonth();
        tv_month.setText(date_month);
    }

    /***
     * 设置类别明细数据
     */
    private void settingCategoryDetails(){
        //设置适配器
        //lv_details
        CategoryDetailsListAdapter daAdapter  = new CategoryDetailsListAdapter(this);
        daAdapter.addAll(bean.getData());
        lv_details.setAdapter(daAdapter);
        lv_details.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //1.获得明细数据
        CategoryDetailsListAdapter myAdapter = (CategoryDetailsListAdapter)adapterView.getAdapter();
        CheckDetailBean bean = myAdapter.getData_list().get(i);

        //2.打开界面展示明细数据
        Intent intent = new Intent();
        intent.setClass(this, DetailInfoActivity.class);
        intent.putExtra("detailBean",bean);
        intent.putExtra("title","详情");
        this.startActivity(intent);
    }
}
