package com.ccmm.cc.mycheckbook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.MyControl.MyListView;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.activity.CheckbookSelectActivity;
import com.ccmm.cc.mycheckbook.activity.DetailInfoActivity;
import com.ccmm.cc.mycheckbook.activity.LoginActivity;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;

import java.util.List;

/**
 * 明细，外层列表适配器
 */
public class DetailsOuterListAdapter extends BaseAdapter implements ListAdapter {
    private List<DetailGroupBean> list;
    private Context context;
    private LayoutInflater inflater;


    public DetailsOuterListAdapter(List<DetailGroupBean> list, Context context) {
        super();
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChildListViewItem childListViewItem ;
        //1.获得控件
        if (convertView == null) {
            childListViewItem = new ChildListViewItem();
            convertView = inflater.inflate(R.layout.parentitem, null, false);
            childListViewItem.text_date =  convertView.findViewById(R.id.date);
            childListViewItem.text_sumMoney =  convertView.findViewById(R.id.sum_money);
            childListViewItem.inner_lv =  convertView.findViewById(R.id.detail_statement);
            convertView.setTag(childListViewItem);
        } else {
            childListViewItem = (ChildListViewItem) convertView.getTag();
        }

        //2.显示数据
        childListViewItem.text_date.setText("  "+list.get(position).getDay()+"-"+list.get(position).getWeek());
        childListViewItem.text_sumMoney.setText("支出："+list.get(position).getTotal_spent()+" 收入："+list.get(position).getTotal_income()+"  ");
        DetailsInnerListAdapter daAdapter  = new DetailsInnerListAdapter(context);
        int z = (list.get(position).getData()).size();
        daAdapter.addAll((list.get(position).getData()));
        childListViewItem.inner_lv.setAdapter(daAdapter);
        childListViewItem.inner_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                //点击一条明细时的操作
                //1.获得明细数据
                DetailsInnerListAdapter myAdapter = (DetailsInnerListAdapter)arg0.getAdapter();
                CheckDetailBean bean = myAdapter.getData_list().get(arg2);

                //2.打开界面展示明细数据
                Intent intent = new Intent();
                intent.setClass(context, DetailInfoActivity.class);
                intent.putExtra("detailBean",bean);
                intent.putExtra("title","详情");
                context.startActivity(intent);
            }
        });
        childListViewItem.inner_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1.获得明细数据,缓存
                DetailsInnerListAdapter myAdapter = (DetailsInnerListAdapter)adapterView.getAdapter();
                CheckDetailBean bean = myAdapter.getData_list().get(i);
                CheckDetailsTools.setDeleteDetails_cacher(bean);

                return false;
            }
        });
        return convertView;
    }

    public class ChildListViewItem {
        TextView text_date;
        TextView text_sumMoney;
        MyListView inner_lv;
    }

}