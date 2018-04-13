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
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;
import com.ccmm.cc.mycheckbook.utils.ZaTools;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 类别，排行榜列表适配器
 */
public class CategoryLeaderBoadAdapter extends BaseAdapter {

    private List<CheckDetailBean> data_list;
    private Context context;
    private LayoutInflater inflater;

    public CategoryLeaderBoadAdapter(Context context) {
        super();
        this.context = context;
    }
    public void addAll(List<CheckDetailBean> list) {
        //1.排序
        Collections.sort(list, new Comparator<CheckDetailBean>() {
            @Override
            public int compare(CheckDetailBean t1, CheckDetailBean t2) {
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
        //2.取top5
        this.data_list = new LinkedList<>();
        for(int i=0;i<5 && i<list.size();i++){
            this.data_list.add(list.get(i));
        }

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
            convertView = inflater.inflate(R.layout.listitem_detail_image, null, false);
            parentListItem.item_icon =  convertView .findViewById(R.id.detail_pic);
            parentListItem.text_description = convertView .findViewById(R.id.detail_title_1);
            parentListItem.text_money = convertView .findViewById(R.id.detail_money);
            convertView.findViewById(R.id.detail_statement);
            convertView.setTag(parentListItem);
        } else {
            parentListItem = (InnerListItem) convertView.getTag();
        }
        //2.设置控件文本内容
        String desciption=ZaTools.makeDetailDescription(data_list.get(position));
        String balance=data_list.get(position).getBalanceType();
        if(desciption==null || desciption.isEmpty()){
            desciption=data_list.get(position).getCategory();
        }
        parentListItem.text_description.setText(desciption);
        String money_str= ZaTools.formatMoneyStr(data_list.get(position).getMoney(),balance);
        parentListItem.text_money.setText(money_str);

        //3.设置图标颜色
        Drawable drawable = context.getDrawable(CategoryColorEnum.rank_ICON_ID[position]);
        drawable = CategoriesIconTool.changeDrawableByBalanceType(drawable, data_list.get(position).getBalanceType());
        parentListItem.item_icon.setImageDrawable(drawable);

        //4.设置点击响应事件

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