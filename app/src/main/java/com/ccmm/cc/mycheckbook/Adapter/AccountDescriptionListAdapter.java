package com.ccmm.cc.mycheckbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.ZaTools;

import java.util.List;

/**
 * 类别，排行榜列表适配器
 */
public class AccountDescriptionListAdapter extends BaseAdapter {

    private List<DetailGroupBean> data_list;
    private Context context;
    private LayoutInflater inflater;

    public AccountDescriptionListAdapter(Context context) {
        super();
        this.context = context;
    }
    public void addAll(List<DetailGroupBean> list) {
        //1.排序

        data_list=list;
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
            convertView = inflater.inflate(R.layout.listitem_account_image, null, false);
            parentListItem.item_icon =  convertView .findViewById(R.id.detail_pic);
            parentListItem.text_title = convertView .findViewById(R.id.detail_title_1);
            parentListItem.text_subtitle = convertView .findViewById(R.id.subtitle);
            parentListItem.text_money_in = convertView .findViewById(R.id.detail_money);
            parentListItem.text_money_out = convertView .findViewById(R.id.money_out);

            convertView.setTag(parentListItem);
        } else {
            parentListItem = (InnerListItem) convertView.getTag();
        }
        //2.设置控件文本内容
        //TODO 设置文字
        parentListItem.text_title.setText(data_list.get(position).getAccountName());
        parentListItem.text_subtitle.setText(data_list.get(position).getAccountName());
        String money_in_str= ZaTools.formatMoneyStr(data_list.get(position).getTotal_income(), BalanceName.Income);
        String money_out_str= ZaTools.formatMoneyStr(data_list.get(position).getTotal_spent(), BalanceName.Expend);
        parentListItem.text_money_in.setText(money_in_str);
        parentListItem.text_money_out.setText(money_out_str);
//        String desciption=data_list.get(position).getDescription();
//        if(desciption==null || desciption.isEmpty()){
//            desciption=data_list.get(position).getCategory();
//        }
//        parentListItem.text_description.setText(desciption);
//        parentListItem.text_money.setText(data_list.get(position).getMoney()+"");

        //3.TODO 设置图标
//        Drawable drawable = context.getDrawable(CategoryColorEnum.rank_ICON_ID[position]);
//        drawable = CategoriesIconTool.changeDrawableByBalanceType(drawable, data_list.get(position).getBalanceType());
//        parentListItem.item_icon.setImageDrawable(drawable);

        //4.设置点击响应事件

        return convertView;
    }

    /**
     * Inner列表类中的控件
     */
    public class InnerListItem {
        ImageView item_icon;
        TextView text_title, text_subtitle;
        TextView text_money_in, text_money_out;
    }

    public List<DetailGroupBean> getData_list() {
        return data_list;
    }

}