package com.ccmm.cc.mycheckbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;

import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */
public class ChildAdapter extends BaseAdapter {
    private List<CheckDetailBean> list;
    private Context context;
    private LayoutInflater inflater;

    public ChildAdapter(Context context) {
        super();
        this.context = context;
    }
    public void addAll(List<CheckDetailBean> list) {
        this.list=list;
        notifyDataSetChanged();
    }

    public void clearAll() {
        this.list.clear();
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ParentListItem parentListItem = null;
        if (convertView == null) {
            parentListItem = new ParentListItem();
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.detail_listitem, null, false);
            parentListItem.item_icon = (ImageView) convertView .findViewById(R.id.detail_pic);
            parentListItem.text_description = (TextView) convertView .findViewById(R.id.detail_content);
            parentListItem.text_money = (TextView) convertView .findViewById(R.id.detail_money);
            convertView.setTag(parentListItem);
        } else {
            parentListItem = (ParentListItem) convertView.getTag();
        }
        parentListItem.text_description.setText(list.get(position).getDescription());
        parentListItem.text_money.setText(list.get(position).getMoney()+"");
        return convertView;
    }

    public class ParentListItem {
        ImageView item_icon;
        TextView text_description, text_money;
    }

}