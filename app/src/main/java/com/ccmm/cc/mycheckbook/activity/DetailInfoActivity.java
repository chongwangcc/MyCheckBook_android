package com.ccmm.cc.mycheckbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.DateTools;

public class DetailInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tv_date;
    TextView tv_category;
    TextView tv_money;
    EditText ev_description;
    ImageView iv_iorn;
    CheckDetailBean bean;
    ConstraintLayout layout;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_info);
        //1.获得数据
        Intent intent = getIntent();
        title = (String)intent.getStringExtra("title");
        if(title ==null) title = "详情";
        bean = (CheckDetailBean)intent.getSerializableExtra("detailBean");
        if(bean ==null){
            finish();
        }
        //1.初始化view
        toolbar = findViewById(R.id.toolbar2);
        tv_date = findViewById(R.id.textView12);
        tv_category = findViewById(R.id.textView13);
        tv_money = findViewById(R.id.textView14);
        ev_description = findViewById(R.id.editText);
        iv_iorn = findViewById(R.id.imageView3);
        layout = findViewById(R.id.constraintLayout);

        setSupportActionBar(toolbar);//设置导航图标要在setSupportActionBar方法之后
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获得修改后数据
                bean.setDescription(ev_description.getText().toString());

                //2.保存到数据库中
                CheckDetailsTools.modifyOneCheckDetails(bean);

                //3.关闭界面
                finish();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转activity
                Intent intent = new Intent();
                intent.setClass(DetailInfoActivity.this, DetailAddingActivity.class);
                intent.putExtra("detailBean",bean);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
        update();

    }

    public void update(){

        //2.设置要显示的数据
        String date_Str=bean.getYear()+"-"+bean.getMonth()+"-"+bean.getDay();
        tv_date.setText("  "+date_Str+"-"+ DateTools.getDateStrWeek(date_Str));
        tv_category.setText(bean.getCategory());
        tv_money.setText(bean.getMoneyStr());
        ev_description.setText(bean.getDescription());
        Drawable drawable = getDrawable(CategoriesIconTool.getDrawableIndex(bean.getCategory()));
        drawable = CategoriesIconTool.changeDrawableByBalanceType(drawable, bean.getBalanceType());
        iv_iorn.setImageDrawable(drawable);;
        //3.设置处理事件
        toolbar.setTitle(title);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (resultCode) {
            case RESULT_OK:
                title = (String)intent.getStringExtra("title");
                if(title ==null) title = "详情";
                bean = (CheckDetailBean)intent.getSerializableExtra("detailBean");
                update();
                break;

            default:
                break;
        }
    }
}
