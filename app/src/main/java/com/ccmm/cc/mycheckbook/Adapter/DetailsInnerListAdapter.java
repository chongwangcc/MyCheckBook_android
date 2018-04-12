package com.ccmm.cc.mycheckbook.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;

import java.util.List;

/**
 * 明细，里层列表适配器
 */
public class DetailsInnerListAdapter extends BaseAdapter {

    private List<CheckDetailBean> data_list;
    private Context context;
    private LayoutInflater inflater;

    public DetailsInnerListAdapter(Context context) {
        super();
        this.context = context;
    }
    public void addAll(List<CheckDetailBean> list) {
        this.data_list =list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data_list.size();
    }

    @Override
    public Object getItem(int position) {
        return data_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InnerListItem parentListItem;
        if (convertView == null) {
            parentListItem = new InnerListItem();
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.detail_listitem, null, false);
            parentListItem.item_icon =  convertView .findViewById(R.id.detail_pic);
            parentListItem.text_description = convertView .findViewById(R.id.detail_content);
            parentListItem.text_money = convertView .findViewById(R.id.detail_money);
            convertView.setTag(parentListItem);
        } else {
            parentListItem = (InnerListItem) convertView.getTag();
        }
        //2.设置控件文本内容
        String desciption=data_list.get(position).getDescription();
        if(desciption==null || desciption.isEmpty()){
            desciption=data_list.get(position).getCategory();
        }
        parentListItem.text_description.setText(desciption);
        parentListItem.text_money.setText(data_list.get(position).getMoney()+"");


        //3.设置图标颜色
        Drawable drawable = context.getDrawable(CategoriesIconTool.getDrawableIndex(data_list.get(position).getCategory()));
        drawable = CategoriesIconTool.changeDrawableByBalanceType(drawable, data_list.get(position).getBalanceType());
        parentListItem.item_icon.setImageDrawable(drawable);
        return convertView;
    }

    /**
     * Inner列表类中的控件
     */
    public class InnerListItem {
        ImageView item_icon;
        TextView text_description, text_money;
    }

    public List<CheckDetailBean> getData_list() {
        return data_list;
    }

}