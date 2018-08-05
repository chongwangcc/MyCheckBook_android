package com.ccmm.cc.mycheckbook.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ccmm.cc.mycheckbook.Adapter.AccountDescriptionListAdapter;
import com.ccmm.cc.mycheckbook.Adapter.CategoryDetailsListAdapter;
import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.AccountBean;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.AccountTools;
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.ZaTools;

public class AccountDetailsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar toolbar;
    AccountDescriptionListAdapter.InnerListItem parentListItem;
    private String title;
    DetailGroupBean m_bean;
    private ListView lv_details;
    private TextView tv_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        //1.获得数据
        Intent intent = getIntent();
        title = (String)intent.getStringExtra("title");
        if(title ==null) title = "详情";
        m_bean = (DetailGroupBean)intent.getSerializableExtra("detailGroup");

        //2.设置toolbar
        toolbar =  findViewById(R.id.toolbar4);
        parentListItem = new AccountDescriptionListAdapter.InnerListItem();

        //3.获得控件
        parentListItem.item_icon =  findViewById(R.id.detail_pic);
        parentListItem.text_title = findViewById(R.id.detail_title_1);
        parentListItem.text_subtitle = findViewById(R.id.subtitle);
        parentListItem.text_money_in = findViewById(R.id.detail_money);
        parentListItem.text_money_out = findViewById(R.id.money_out);

        //4.设置类别明细数据
        tv_month = findViewById(R.id.textView15);
        lv_details = findViewById(R.id.dd_dynamic);
    }

    @Override
    protected void onResume() {
        //1.遍历bean更新数据

        updateGroupBean();
        //2.设置控件上值
        settingToolBar();
        settingCategorySum();
        settingCategoryDetails();
        super.onResume();
    }

    private void updateGroupBean(){
        //1.更新数据
        DetailGroupBean new_bean_group = new DetailGroupBean();
        for(CheckDetailBean detailBean: this.m_bean.getData()){
            CheckDetailBean beanDetail=CheckDetailsTools.queryCheckDetail(CheckbookTools.getSelectedCheckbook().getCheckbookID(),detailBean);
            new_bean_group.addOneDetailBean(beanDetail);
        }

        //2.更新totoal_Income
        double money_old=this.m_bean.getMoney();
        double money_new=new_bean_group.getMoney();
        this.m_bean=new_bean_group;

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
     *设置账户汇总数据
     */
    private void settingCategorySum(){
        //2.设置控件文本内容
        parentListItem.text_title.setText(m_bean.getAccountName());
        AccountBean bean = AccountTools.getAccountByID(m_bean.getAccount_id());
        double assets_num = bean.getAssets_nums()+m_bean.getAssets_diff();
        double liablity_num = bean.getLiablities_num()+m_bean.getLiabilities_diff();
        String money_in_str= ZaTools.formatMoneyStr(assets_num, BalanceName.Income);
        String money_out_str= ZaTools.formatMoneyStr(liablity_num, BalanceName.Expend);
        String assets_str="资产:"+money_in_str+"     负债:"+money_out_str;
        parentListItem.text_money_in.setText(assets_str);

        money_in_str= ZaTools.formatMoneyStr(m_bean.getTotal_income(), BalanceName.Income);
        money_out_str= ZaTools.formatMoneyStr(m_bean.getTotal_spent(), BalanceName.Expend);
        String profitSum_str="收入:"+money_in_str+"     支出:"+money_out_str;
        parentListItem.text_money_out.setText(profitSum_str);

        money_in_str= ZaTools.formatMoneyStr(m_bean.getCashflow_in(), BalanceName.Income);
        money_out_str= ZaTools.formatMoneyStr(m_bean.getCashflow_out(), BalanceName.Expend);
        String cacherflowm_str="流入:"+money_in_str+"     流出:"+money_out_str;
        parentListItem.text_subtitle.setText(cacherflowm_str);

        //4.设置年月
        String date_month="";
        date_month=m_bean.getYear()+"-"+m_bean.getMonth();
        tv_month.setText(date_month);
    }

    /***
     * 设置类别明细数据
     */
    private void settingCategoryDetails(){
        //设置适配器
        //lv_details
        CategoryDetailsListAdapter daAdapter  = new CategoryDetailsListAdapter(this);
        daAdapter.addAll(m_bean.getData());
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
