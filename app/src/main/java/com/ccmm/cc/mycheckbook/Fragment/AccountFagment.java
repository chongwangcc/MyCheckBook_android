package com.ccmm.cc.mycheckbook.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ccmm.cc.mycheckbook.Adapter.AccountDescriptionListAdapter;
import com.ccmm.cc.mycheckbook.MyControl.AddAccountDialog;
import com.ccmm.cc.mycheckbook.MyControl.ChooseMonthDialog;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.activity.DetailAddingActivity;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;

import java.util.List;

public class AccountFagment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    protected boolean isCreated = false;
    ListView lv_account_description ;
    Button bt_add_account;
    private AddAccountDialog selfDialog;


    public static AccountFagment newInstance() {
        Bundle args = new Bundle();
        return new AccountFagment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 标记
        context = this.getContext();
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view ;
        //1.获得View 显示控件
        view = inflater.inflate(R.layout.fragment_account_report, container, false);
        lv_account_description=view.findViewById(R.id.account_list_description);
        bt_add_account = view.findViewById(R.id.button2);
        //2.设置处理点击处理事件
        lv_account_description.setOnItemClickListener(this);
        bt_add_account.setOnClickListener(this);
        //3.更新数据
        updateData();
        return view;
    }

    private void updateData() {
        //1.读数据库,获得最新明细数据
        List<CheckDetailBean> llBan = CheckDetailsTools.getAllDetailsInMonth(CheckbookTools.getSelectedCheckbook().getCheckbookID(),
                CheckDetailsTools.getDetails_year(),
                CheckDetailsTools.getDetals_month());
        List<DetailGroupBean> groupBeans= CheckDetailsTools.detailsGroupByAccount(llBan);
        AccountDescriptionListAdapter adtapter = new AccountDescriptionListAdapter(context);
        //TODO 添加要处理的数据
        adtapter.addAll(groupBeans);
        lv_account_description.setAdapter(adtapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO 处理account点击事件
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.button2:
                //打开新增账户对话框
                selfDialog = new AddAccountDialog(view.getContext());
                selfDialog.show();
                break;
        }
    }
}
