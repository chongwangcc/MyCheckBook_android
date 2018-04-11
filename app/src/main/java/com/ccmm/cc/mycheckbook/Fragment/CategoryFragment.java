package com.ccmm.cc.mycheckbook.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.MyControl.PieGraphView;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.activity.DetailAddingActivity;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.ZaTools;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener,PieGraphView.ItemChangeListener{

    com.ccmm.cc.mycheckbook.MyControl.PieGraphView pieChart;

    private List<DetailGroupBean> detail_group_income; //明细数据
    private List<DetailGroupBean> detail_group_spend; //明细数据
    private List<DetailGroupBean> detail_group_inner; //明细数据

    protected boolean isCreated = false;
    private int mColors_expend[] ={0xFFE4E1,0xFF4500,0xFFA07A,0xF4A460,0xFA8072,0xCD5C5C,0x8B0000,0x800000};
    private int mColors_income[] ={0x7FFF00,0x006400,0x9ACD32,0x90EE90,0x2E8B57,0x00FF7F,0x32CD32,0xADFF2F};
    private int mColors_inner[] = {0x0000FF,0x4169E1,0x1E90FF,0x00BFFF,0x0000CD,0x8A2BE2,0x800080,0x8A2BE2};
    private ImageView iv_select_category;
    private TextView tv_select_category_description;
    private TextView tv_select_category_money;

    double total_income=1.0;
    double total_spent=1.0;

    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        return new CategoryFragment();
    }

    public CategoryFragment(){
        int temp[] =new int[mColors_expend.length];
        for(int i=0;i<temp.length;i++){
            String color_str="#"+ ZaTools.toHexString(mColors_expend[i],6);
            temp[i]= Color.parseColor(color_str);
        }
        mColors_expend=temp;

        temp =new int[mColors_income.length];
        for(int i=0;i<temp.length;i++){
            String color_str="#"+ ZaTools.toHexString(mColors_income[i],6);
            temp[i]= Color.parseColor(color_str);
        }
        mColors_income=temp;

        temp =new int[mColors_inner.length];
        for(int i=0;i<temp.length;i++){
            String color_str="#"+ ZaTools.toHexString(mColors_inner[i],6);
            temp[i]= Color.parseColor(color_str);
        }
        mColors_inner=temp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 标记
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view ;

        view = inflater.inflate(R.layout.fragment_category_report, container, false);
        pieChart=view.findViewById(R.id.pieChart);
        //1.获得数据
        List<CheckDetailBean> llBan =CheckDetailsTools.getAllDetailsInMonth(CheckbookTools.getSelectedCheckbook().getCheckbookID(),
                                               CheckDetailsTools.getDetails_year(),
                                               CheckDetailsTools.getDetals_month());
        detail_group_income =  CheckDetailsTools.detailsGroupByCategory(llBan, BalanceName.Income);
        detail_group_spend =  CheckDetailsTools.detailsGroupByCategory(llBan, BalanceName.Expend);
        detail_group_inner=  CheckDetailsTools.detailsGroupByCategory(llBan, BalanceName.Inner);
        iv_select_category = view.findViewById(R.id.main1).findViewById(R.id.detail_pic);
        tv_select_category_description = view.findViewById(R.id.main1).findViewById(R.id.detail_content);
        tv_select_category_money = view.findViewById(R.id.main1).findViewById(R.id.detail_money);
        setPieViewData();

        //2.TODO 设置处理点击处理事件
        pieChart.setItemChangeListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.add_detail_button:
                //跳转
                Intent intent = new Intent();
                intent.setClass(this.getActivity(), DetailAddingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isCreated) {
            return;
        }

        if (isVisibleToUser) {
            updateData();
        }
    }

    /***
     * 更新数据
     */
    public void updateData(){
        if(isCreated){

        }
    }

    public PieGraphView.ItemGroup genItemGroup(List<DetailGroupBean> groupBeans,String balanceName){
        if(groupBeans.size()>0){
            PieGraphView.ItemGroup itemGroup = new PieGraphView.ItemGroup();
            itemGroup.id=balanceName; //设置id
            List<PieGraphView.Item> items = new LinkedList<>();
            int mColors_temp[] = null;



            //TODO 设置title
            String title="";
            switch (balanceName){
                case BalanceName.Expend:
                    title+="总支出";
                    mColors_temp=mColors_expend;
                    break;
                case BalanceName.Income:
                    title+="总收入";
                    mColors_temp=mColors_income;
                    break;
                case BalanceName.Inner:
                    title+="内部转账";
                    mColors_temp=mColors_inner;
            }
            float totalMoney=0;
            for(int i=0;i<8 && i<groupBeans.size();i++){
                PieGraphView.Item item = new PieGraphView.Item();
                item.id = groupBeans.get(i).getCategoryType();
                item.value = groupBeans.get(i).getMoney();
                item.color = mColors_temp[i]; //设置颜色
                item.icon_id = CategoriesIconTool.getDrawableIndex(item.id);
                items.add(item);
                totalMoney+=groupBeans.get(i).getMoney();
            }

            switch (balanceName){
                case BalanceName.Expend:
                    total_spent=totalMoney;
                    break;
                case BalanceName.Income:
                    total_income=totalMoney;
                    break;
                case BalanceName.Inner:
            }

            itemGroup.items = items.toArray(new PieGraphView.Item[items.size()]);
            title+="\n"+totalMoney+"\n元";
            itemGroup.title=title;
            return itemGroup;
        }
        return null;
    }

    public void setPieViewData(){
        List<PieGraphView.ItemGroup> itemGroups = new LinkedList<>();
        //设置支出组别
        PieGraphView.ItemGroup itemGourp = genItemGroup(detail_group_spend,BalanceName.Expend);
        if(itemGourp!=null){
            itemGroups.add(itemGourp);
        }
        //设置收入组别
         itemGourp = genItemGroup(detail_group_income,BalanceName.Income);
        if(itemGourp!=null){
            itemGroups.add(itemGourp);
        }
        //设置内部转账组别
        itemGourp = genItemGroup(detail_group_inner,BalanceName.Inner);
        if(itemGourp!=null){
            itemGroups.add(itemGourp);
        }
        pieChart.setData(itemGroups.toArray(new PieGraphView.ItemGroup[itemGroups.size()]));
        pieChart.setRingWidthFactor(100);
    }

    /***
     * 选中了指定的类别
     * @param balanceType
     * @param categoryType
     */
    public void chooseCategory(String balanceType,String categoryType){
        //TODO 设置表格


    }

    @Override
    public void onItemSelected(PieGraphView.ItemGroup group, PieGraphView.Item item) {
        chooseCategory(group.id,item.id);
        Toast.makeText(this.getContext(),group.id+"...."+item.id+"...."+item.value,Toast.LENGTH_SHORT).show();
        String category=item.id;
        String balance=group.id;
        double money = item.value;
        //1.设置图标
        Drawable drawable = this.getContext().getDrawable(CategoriesIconTool.getDrawableIndex(category));
        drawable = CategoriesIconTool.changeDrawableByBalanceType(drawable, balance);
        iv_select_category.setImageDrawable(drawable);

        //2.设置金钱
        String money_str=String.format("%.2f", money);
        tv_select_category_money.setText(money_str);

        //3.设置详解
        double percent = 0;
        switch (balance){
            case BalanceName.Income:
                percent = money/total_income;
                break;
            case BalanceName.Expend:
                percent = money/total_spent;
                break;

        }
        String percent_str=String.format("%.2f", percent*100);
        String description = category +"   " +(percent_str)+"%";
        tv_select_category_description.setText(description);
    }
}


