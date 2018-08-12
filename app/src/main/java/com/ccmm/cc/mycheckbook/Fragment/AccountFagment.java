package com.ccmm.cc.mycheckbook.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Adapter.AccountDescriptionListAdapter;
import com.ccmm.cc.mycheckbook.MyControl.AddAccountDialog;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.activity.AccountDetailsActivity;
import com.ccmm.cc.mycheckbook.models.AccountBean;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.AccountTools;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.LoginTools;

import java.util.List;

public class AccountFagment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private Context context;
    protected boolean isCreated = false;
    ListView lv_account_description ;
    Button bt_add_account;
    List<DetailGroupBean> groupBeans;
    List<CheckDetailBean> llBan;
    private AddAccountDialog selfDialog;
    static private DetailGroupBean selected_item;


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
        this.registerForContextMenu(lv_account_description);
        //2.设置处理点击处理事件
        lv_account_description.setOnItemClickListener(this);
        lv_account_description.setOnItemLongClickListener(this);
        bt_add_account.setOnClickListener(this);
        //3.更新数据
        updateData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        //1.读数据库,获得最新明细数据
        llBan = CheckDetailsTools.getAllDetailsInMonth(CheckbookTools.getSelectedCheckbook().getCheckbookID(),
                CheckDetailsTools.getDetails_year(),
                CheckDetailsTools.getDetals_month());
       groupBeans= CheckDetailsTools.detailsGroupByAccount(llBan);
        AccountDescriptionListAdapter adtapter = new AccountDescriptionListAdapter(context);
        // 添加要处理的数据
        adtapter.addAll(groupBeans);
        lv_account_description.setAdapter(adtapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO 处理account点击事件
        //查看此账户下的所有明细
        //1.点击的是哪一个账户
        DetailGroupBean groupBean = (DetailGroupBean)adapterView.getItemAtPosition(i);
        Toast.makeText(this.getContext(),"查看账户...."+groupBean.getAccountName(),Toast.LENGTH_SHORT).show();
        //2.获得此账户下，当月的所有明细操作
        //2.打开界面展示明细数据
        Intent intent = new Intent();
        intent.setClass(context, AccountDetailsActivity.class);
        intent.putExtra("detailGroup",groupBean);
        intent.putExtra("title","账户明细详情");
        intent.putExtra("total",1);
        context.startActivity(intent);

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
        updateData();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("你想干啥？");
        //menu.add(0, 0, Menu.NONE, "查看");
        menu.add(0, 1, Menu.NONE, "编辑");
        menu.add(0, 2, Menu.NONE, "删除");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getMenuInfo() instanceof AdapterView.AdapterContextMenuInfo) {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //处理菜单的点击事件
            DetailGroupBean selected_group = AccountFagment.getSelected_item();
            AccountBean accountBean = AccountTools.getAccountByID(selected_group.getAccount_id());
            switch (item.getItemId()) {
                case 0: //查看账户
                    Toast.makeText(this.getContext(),"查看账户....",Toast.LENGTH_SHORT).show();
                    break;
                case 1: //编辑账户

                    AddAccountDialog dialog = new AddAccountDialog(this.getContext());
                    dialog.setAccountbean(accountBean);
                    dialog.setSelected_group(selected_group);
                    dialog.show();
                    //Toast.makeText(this.getContext(),"编辑明细....",Toast.LENGTH_SHORT).show();
                    break;
                case 2: //删除账户
                    //获得默认的未分类账户
                    if(accountBean.getName().equals("未分类")){
                        Toast.makeText(this.getContext(),"无法删除“未分类”账户....",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    AccountBean defaultBean = AccountTools.getDefaultAccount(CheckbookTools.getSelectedCheckbook().getCheckbookID());
                    AccountTools.deleteAccount(accountBean,defaultBean);
                    Toast.makeText(this.getContext(),"删除账户....",Toast.LENGTH_SHORT).show();
                    break;
                default :

            }

        }
        updateData();
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        // 1.保存选中的账户
        AccountFagment.setSelected_item(groupBeans.get(position));
        return false;
    }

    public static DetailGroupBean getSelected_item() {
        return selected_item;
    }

    public static void setSelected_item(DetailGroupBean selected_item) {
        AccountFagment.selected_item = selected_item;
    }
}
