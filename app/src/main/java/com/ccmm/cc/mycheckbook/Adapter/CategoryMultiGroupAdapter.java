package com.ccmm.cc.mycheckbook.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccmm.cc.mycheckbook.Enum.CategoryColorEnum;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 类别，排行榜列表适配器
 */
public class CategoryMultiGroupAdapter extends BaseAdapter {

    private List<DetailGroupBean> data_list;
    private Context context;
    private LayoutInflater inflater;
    private double totalMoney=0;

    public CategoryMultiGroupAdapter(Context context) {
        super();
        this.context = context;
    }
    public void addAll(List<DetailGroupBean> list) {
        //1.排序
        Collections.sort(list, new Comparator<DetailGroupBean>() {
            @Override
            public int compare(DetailGroupBean t1, DetailGroupBean t2) {
                float n1=0,n2=0;
                n1=t1.getMoney();
                n2=t2.getMoney();
                if(n2>n1){
                    return 1;
                }else if(n2<n1){
                    return -1;
                }
                return 0;
            }
        });
        this.data_list = list;

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
        // 1.获得控件
        InnerListItem parentListItem;
        if (convertView == null) {
            parentListItem = new InnerListItem();
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.detail_listitem, null, false);
            parentListItem.item_icon =  convertView .findViewById(R.id.detail_pic);
            parentListItem.text_description = convertView .findViewById(R.id.detail_content);
            parentListItem.text_money = convertView .findViewById(R.id.detail_money);
            convertView.findViewById(R.id.detail_statement);
            convertView.setTag(parentListItem);
        } else {
            parentListItem = (InnerListItem) convertView.getTag();
        }
        //2.设置控件文本内容
        float money=data_list.get(position).getMoney();
        String desciption=data_list.get(position).getCategoryType();
        if(desciption==null || desciption.isEmpty()){
            desciption="";
        }
        if(totalMoney>0){
            String percent=String.format("%.2f", (money/totalMoney*100))+"%";
            desciption+="   "+percent;
        }

        parentListItem.text_description.setText(desciption);
        parentListItem.text_money.setText(money+"");

        //3.设置图标颜色
        Drawable drawable = context.getDrawable(CategoriesIconTool.getDrawableIndex(data_list.get(position).getCategoryType()));
        drawable = CategoriesIconTool.changeDrawableByBalanceType(drawable, data_list.get(position).getBalanceType());
        parentListItem.item_icon.setImageDrawable(drawable);

        //4.设置点击响应事件

        return convertView;
    }
    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    /**
     * Inner列表类中的控件
     */
    public class InnerListItem {
        ImageView item_icon;
        TextView text_description, text_money;
    }

}