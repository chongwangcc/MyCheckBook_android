package com.ccmm.cc.mycheckbook.MyControl;

/**
 * Created by cc on 2018/4/7.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.AccountBean;
import com.ccmm.cc.mycheckbook.utils.AccountTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.ZaTools;

import java.util.LinkedList;
import java.util.List;

/**
 * 选择日期的对话框
 */
public class AddAccountDialog extends Dialog implements  AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private EditText ev_account_name;
    private EditText ev_assets_money;
    private EditText ev_liabilities_money;
    private TextView tv_title;
    private Button yesButton;
    private Button noButton;

    Context context;
    private AccountBean accountbean;
    List<AccountBean> allAccounts;
    List<AccountBean> topAccounts = new LinkedList<>();
    AccountBean selectedTopAccount;

    public AddAccountDialog(Context context) {
        super(context, R.style.Theme_AppCompat_DayNight);
        this.context=context;
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获得数据,保存到数据库
                AccountBean topAccount=selectedTopAccount;
                String name = ev_account_name.getText().toString();
                String assets_str = ev_assets_money.getText().toString();
                String liablities_str = ev_liabilities_money.getText().toString();
                if(name ==null || name.isEmpty()){
                    String errorInfo="账户名称不能为空!!!";
                    ZaTools.showErrorMessage(context,errorInfo);
                    return;
                }else if (AccountTools.isNamesUsed(name,allAccounts) || name.equals("未分类")){
                    String errorInfo="账户名称已被使用!!!";
                    ZaTools.showErrorMessage(context,errorInfo);
                    return;
                }
                if(assets_str==null || assets_str.isEmpty()){
                    assets_str="0";
                }
                if(liablities_str==null || liablities_str.isEmpty()){
                    liablities_str="0";
                }
                //1.新建账户类,保存到数据库
                AccountBean account = new AccountBean();
                account.setName(name);
                account.setAssets_nums(Double.valueOf(assets_str.toString()));
                account.setLiablities_num(Double.valueOf(liablities_str.toString()));
                if(topAccount==null || topAccount.getAccount_id()<0 || topAccount.getName().equals("未分类")){
                    account.setParent_id(-1);
                    account.setLevel(1);
                }else{
                    account.setParent_id(topAccount.getAccount_id());
                    account.setLevel(topAccount.getLevel()+1);
                    account.setKey(topAccount.getParent_id()+"-");
                }
                AccountTools.addOneAccount(CheckbookTools.getSelectedCheckbook().getCheckbookID(),account);
                //退出
                cancel();
            }
        });

        spinner.setOnItemSelectedListener (this);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if(accountbean==null || accountbean.getAccount_id()<0){
            //新增账户信息
            tv_title.setText("新增账户");
        }else{
            tv_title.setText("修改账户");
        }
        //1.设置子账户列表
       allAccounts =  AccountTools.getAccountList(CheckbookTools.getSelectedCheckbook().getCheckbookID());

        for(AccountBean account : allAccounts){
            if(account.getLevel()==1){
                topAccounts.add(account);
            }
        }

        ArrayAdapter<AccountBean> myAdapter=new ArrayAdapter<AccountBean>(this.getContext(),
                android.R.layout.simple_spinner_item, topAccounts);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        spinner = findViewById(R.id.spinner);
        ev_account_name = findViewById(R.id.editText2);
        ev_assets_money = findViewById(R.id.editText3);
        ev_liabilities_money = findViewById(R.id.editText4);

        yesButton = findViewById(R.id.button20);
        noButton = findViewById(R.id.button2);
        tv_title = findViewById(R.id.textView16);

    }
    ////////////////OVERRITE 方法/////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_account);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayAdapter<AccountBean> myAdapter =(ArrayAdapter<AccountBean>) adapterView.getAdapter();
        selectedTopAccount=topAccounts.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        ArrayAdapter<AccountBean> myAdapter =(ArrayAdapter<AccountBean>) adapterView.getAdapter();
        selectedTopAccount=topAccounts.get(0);
    }

    ////////////////////////// SET GET 方法///////////////////////////////////////////////
    public AccountBean getAccountbean() {
        return accountbean;
    }

    public void setAccountbean(AccountBean accountbean) {
        this.accountbean = accountbean;
    }



}