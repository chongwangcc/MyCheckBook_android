package com.ccmm.cc.mycheckbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ccmm.cc.mycheckbook.MyControl.ChildListView;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;

import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class ParentAdapter extends BaseAdapter implements ListAdapter {
    private List<DetailGroupBean> list;
    private Context context;
    private LayoutInflater inflater;


    public ParentAdapter(List<DetailGroupBean> list, Context context) {
        super();
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChildListViewItem childListViewItem = null;
        if (convertView == null) {
            childListViewItem = new ChildListViewItem();
            convertView = inflater.inflate(R.layout.parentitem, null, false);
            childListViewItem.text_date = (TextView) convertView.findViewById(R.id.date);
            childListViewItem.text_sumMoney = (TextView) convertView.findViewById(R.id.sum_money);
            childListViewItem.parent_lv = (ChildListView) convertView.findViewById(R.id.detail_statement);
            convertView.setTag(childListViewItem);
        } else {
            childListViewItem = (ChildListViewItem) convertView.getTag();
        }
        childListViewItem.text_date.setText("  "+list.get(position).getDay()+"-"+list.get(position).getWeek());
        childListViewItem.text_sumMoney.setText("支出："+list.get(position).getTotal_spent()+" 收入："+list.get(position).getTotal_income()+"  ");

        //2.显示数据
        ChildAdapter daAdapter  = new ChildAdapter(context);;
        int z = ((List<CheckDetailBean>)list.get(position).getData()).size();
        daAdapter.addAll(((List<CheckDetailBean>)list.get(position).getData()));
        childListViewItem.parent_lv.setAdapter(daAdapter);
        childListViewItem.parent_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                //TODO 点击一条明细时的操作

            }
        });
        return convertView;
    }

    public class ChildListViewItem {
        TextView text_date;
        TextView text_sumMoney;
        ChildListView parent_lv;
    }

    public void setDetail_data(List<DetailGroupBean> list) {
        this.list = list;
    }

}