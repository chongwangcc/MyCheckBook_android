package com.ccmm.cc.mycheckbook.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Adapter.CategoryLeaderBoadAdapter;
import com.ccmm.cc.mycheckbook.Adapter.CategoryMultiGroupAdapter;
import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.Enum.CategoryColorEnum;
import com.ccmm.cc.mycheckbook.MyControl.MyListView;
import com.ccmm.cc.mycheckbook.MyControl.PieGraphView;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.activity.CategoryDetailsActivity;
import com.ccmm.cc.mycheckbook.activity.DetailAddingActivity;
import com.ccmm.cc.mycheckbook.activity.DetailInfoActivity;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener,PieGraphView.ItemChangeListener{
    private final int MAX_ITEM_NUM=8;

    com.ccmm.cc.mycheckbook.MyControl.PieGraphView pieChart;
    private ImageView iv_select_category;
    private TextView tv_select_category_description;
    private TextView tv_select_category_money;
    private TextView tv_Leaderboard;
    private MyListView mlv_Leaderboard_details;
    private View layout_title;
    private Context context;

    protected boolean isCreated = false;

    private List<DetailGroupBean> detail_group_income; //收入-类别-明细数据
    private List<DetailGroupBean> detail_group_spend; //支出-类别-明细数据
    private List<DetailGroupBean> detail_group_inner; //内部转账-类别明细数据

    double total_income_money =1.0;
    double total_spent_money =1.0;
    double total_inner_money =1.0;

    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        return new CategoryFragment();
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
        view = inflater.inflate(R.layout.fragment_category_report, container, false);
        pieChart=view.findViewById(R.id.pieChart);
        iv_select_category = view.findViewById(R.id.main1).findViewById(R.id.detail_pic);
        tv_select_category_description = view.findViewById(R.id.main1).findViewById(R.id.detail_title_1);
        tv_select_category_money = view.findViewById(R.id.main1).findViewById(R.id.detail_money);
        tv_Leaderboard = view.findViewById(R.id.textView17);
        mlv_Leaderboard_details = view.findViewById(R.id.detail_statement);
        layout_title = view.findViewById(R.id.main1);
        //2.设置处理点击处理事件
        pieChart.setItemChangeListener(this);

        //3.更新数据
        updateData();
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
        //1.读数据库,获得最新明细数据
        List<CheckDetailBean> llBan =CheckDetailsTools.getAllDetailsInMonth(CheckbookTools.getSelectedCheckbook().getCheckbookID(),
                CheckDetailsTools.getDetails_year(),
                CheckDetailsTools.getDetals_month());
        detail_group_income =  CheckDetailsTools.detailsGroupByCategory(llBan, BalanceName.Income);
        detail_group_spend =  CheckDetailsTools.detailsGroupByCategory(llBan, BalanceName.Expend);
        detail_group_inner=  CheckDetailsTools.detailsGroupByCategory(llBan, BalanceName.Inner);
        //2.设置pieVie中的数据
        setPieViewData();
    }

    /***
     * 生成绘制饼状图用的ItemGroup
     * @param groupBeans
     * @param balanceName
     * @return
     */
    public PieGraphView.ItemGroup genItemGroup(List<DetailGroupBean> groupBeans,String balanceName){
        //1.初始化group中公共字段
        PieGraphView.ItemGroup itemGroup = new PieGraphView.ItemGroup();
        String title="";
        float totalMoney=0;
        int mColors_temp[] = null;
        switch (balanceName){
            case BalanceName.Expend:
                title+="总支出";
                mColors_temp=CategoryColorEnum.mColors_expend;
                break;
            case BalanceName.Income:
                title+="总收入";
                mColors_temp= CategoryColorEnum.mColors_income;
                break;
            case BalanceName.Inner:
                title+="内部转账";
                mColors_temp=CategoryColorEnum.mColors_inner;
        }
        if(groupBeans!=null && groupBeans.size()>0){
            itemGroup.id=balanceName; //设置id
            List<PieGraphView.Item> items = new LinkedList<>();
            for(int i=0;i<MAX_ITEM_NUM && i<groupBeans.size();i++){
                PieGraphView.Item item = new PieGraphView.Item();
                List<DetailGroupBean> groupAll = new LinkedList<>();
                item.id = groupBeans.get(i).getCategoryType();
                item.value = groupBeans.get(i).getMoney();
                item.color = mColors_temp[i%MAX_ITEM_NUM]; //设置颜色
                item.icon_id = CategoriesIconTool.getDrawableIndex(item.id);
                groupAll.add(groupBeans.get(i));
                item.remark=groupAll;
                items.add(item);
                totalMoney+=groupBeans.get(i).getMoney();
            }
            //8个以上的归到一组里
            if(groupBeans.size()>=MAX_ITEM_NUM){
                PieGraphView.Item item = new PieGraphView.Item();
                List<DetailGroupBean> groupAll = new LinkedList<>();
                float temp_money=0;
                for(int i=MAX_ITEM_NUM-1;i<groupBeans.size();i++){
                    groupAll.add(groupBeans.get(i));
                    temp_money+=groupBeans.get(i).getMoney();
                    totalMoney+=groupBeans.get(i).getMoney();
                }
                item.remark=groupAll;
                item.id = "一般";
                item.value =temp_money;
                item.color = mColors_temp[MAX_ITEM_NUM-1]; //设置颜色
                item.icon_id = CategoriesIconTool.getDrawableIndex(item.id);
                items.set(MAX_ITEM_NUM-1,item);
            }
            switch (balanceName){
                case BalanceName.Expend:
                    total_spent_money =totalMoney;
                    break;
                case BalanceName.Income:
                    total_income_money =totalMoney;
                    break;
                case BalanceName.Inner:
                    total_inner_money =totalMoney;
                    break;
            }

            itemGroup.items = items.toArray(new PieGraphView.Item[items.size()]);
            title+="\n"+totalMoney+"\n元";
            itemGroup.title=title;

            //5.整理itemGroup各项所占百分比
            for(PieGraphView.Item item:itemGroup.items){
                item.value = item.value/totalMoney*100;
            }
            double remainPercent=100;
            double totalAll=100;
            List<PieGraphView.Item> allItem = new ArrayList(Arrays.asList(itemGroup.items.clone()));
            while (true){
                PieGraphView.Item findMin=null;
                //1.查找是否有比例小于5%的
                for(PieGraphView.Item item:allItem){
                    if(item.value<5){
                        findMin=item;
                        break;
                    }
                }
                //2.将最小值比例设置为5,其余值按比例均分剩余的值
                if(findMin==null){
                    break;
                }else{
                    totalAll-=findMin.value;
                    findMin.value=5;
                    remainPercent-=5;

                    allItem.remove(findMin);
                    for(PieGraphView.Item item:allItem){
                        item.value=item.value/totalAll*remainPercent; }
                    }
            }
        }else{
            //对空值进行特殊处理
            itemGroup.id="null";
            title+="\n"+totalMoney+"\n元";
            itemGroup.title=title;
            PieGraphView.Item item = new PieGraphView.Item();
            item.id=null;
            item.value=1.0;
            item.color = mColors_temp[0];
            item.icon_id=CategoriesIconTool.getDrawableIndex("一般");
            itemGroup.items = new PieGraphView.Item[]{item};
        }

        return itemGroup;
    }

    /***
     * 设置饼状图的所有显示数据
     */
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
        //设置pieValue中的数据
        pieChart.setData(itemGroups.toArray(new PieGraphView.ItemGroup[itemGroups.size()]));
        pieChart.setRingWidthFactor(100);
    }

    @Override
    public void onItemSelected(PieGraphView.ItemGroup group, PieGraphView.Item item) {

        //Toast.makeText(this.getContext(),group.id+"...."+item.id+"...."+item.value,Toast.LENGTH_SHORT).show();
        String category=item.id;
        String balance=group.id;
        List<DetailGroupBean >groupAll = (List<DetailGroupBean>)item.remark;
        layout_title.setVisibility(View.INVISIBLE);
        tv_Leaderboard.setVisibility(View.INVISIBLE);
        mlv_Leaderboard_details.setVisibility(View.INVISIBLE);
        if(item.remark==null){
            // 对空值进行特殊处理--隐藏控件
            return;
        }else{
            layout_title.setVisibility(View.VISIBLE);
            tv_Leaderboard.setVisibility(View.VISIBLE);
            mlv_Leaderboard_details.setVisibility(View.VISIBLE);
        }
        if(groupAll.size()==1){
            /*************单一类别**?????*/
            final DetailGroupBean bean = groupAll.get(0);
            double money = bean.getMoney();
            //1.设置图标
            Drawable drawable = this.getContext().getDrawable(CategoriesIconTool.getDrawableIndex(category));
            drawable = CategoriesIconTool.changeDrawableByBalanceType(drawable, balance);
            iv_select_category.setImageDrawable(drawable);

            //2.设置金钱
            String money_str=String.format("%.2f", money);
            tv_select_category_money.setText(money_str);

            //3.设置详解
            double percent = 0;
            String leaderboard_title=category;
            switch (balance){
                case BalanceName.Income:
                    percent = money/ total_income_money;
                    leaderboard_title+="消费排行榜";
                    break;
                case BalanceName.Expend:
                    percent = money/ total_spent_money;
                    leaderboard_title+="收入排行榜";
                    break;
                case BalanceName.Inner:
                    percent = money/ total_inner_money;
                    leaderboard_title+="内部转账排行榜";
            }
            String percent_str=String.format("%.2f", percent*100);
            String description = category +"   " +(percent_str)+"%";
            tv_select_category_description.setText(description);

            //4.设置排行榜
            tv_Leaderboard.setText(leaderboard_title);
            CategoryLeaderBoadAdapter daAdapter  = new CategoryLeaderBoadAdapter(this.getContext());
            daAdapter.addAll(bean.getData());
            mlv_Leaderboard_details.setAdapter(daAdapter);

            //5.设置带点击事件
            mlv_Leaderboard_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //1.获得明细数据
                    CategoryLeaderBoadAdapter myAdapter = (CategoryLeaderBoadAdapter)adapterView.getAdapter();
                    CheckDetailBean bean = myAdapter.getData_list().get(i);

                    //2.打开界面展示明细数据
                    Intent intent = new Intent();
                    intent.setClass(context, DetailInfoActivity.class);
                    intent.putExtra("detailBean",bean);
                    intent.putExtra("title","详情");

                    context.startActivity(intent);
                }
            });
            layout_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double totalMoney=0;
                    switch (bean.getBalanceType()){
                        case BalanceName.Expend:
                            totalMoney= total_spent_money;
                            break;
                        case BalanceName.Income:
                            totalMoney= total_income_money;
                            break;
                        case BalanceName.Inner:
                            totalMoney= total_inner_money;
                            break;
                    }
                    //2.打开界面展示明细数据
                    Intent intent = new Intent();
                    intent.setClass(context, CategoryDetailsActivity.class);
                    intent.putExtra("detailGroup",bean);
                    intent.putExtra("title","类别详情");
                    intent.putExtra("total",totalMoney);
                    context.startActivity(intent);
                }
            });
        }else{
            /************* 多个类别类别**?????*/
            List<DetailGroupBean> detailList = (List<DetailGroupBean>)item.remark;
            //1.隐藏不用的控件
            layout_title.setVisibility(View.INVISIBLE);
            tv_Leaderboard.setVisibility(View.INVISIBLE);
            mlv_Leaderboard_details.setVisibility(View.VISIBLE);

            CategoryMultiGroupAdapter daAdapter  = new CategoryMultiGroupAdapter(this.getContext());
            daAdapter.addAll(detailList);
            double totalMoney=0;
            switch (balance){
                case BalanceName.Expend:
                    totalMoney= total_spent_money;
                    break;
                case BalanceName.Income:
                    totalMoney= total_income_money;
                    break;
                case BalanceName.Inner:
                    totalMoney= total_inner_money;
                    break;
            }
            daAdapter.setTotalMoney(totalMoney);
            mlv_Leaderboard_details.setAdapter(daAdapter);

            //5.设置带点击事件
            mlv_Leaderboard_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //设置点击事件处理,跳到新的Activity中
                    //1.获得明细数据
                    CategoryMultiGroupAdapter myAdapter = (CategoryMultiGroupAdapter)adapterView.getAdapter();
                    DetailGroupBean bean = myAdapter.getData_list().get(i);

                    //2.打开界面展示明细数据
                    Intent intent = new Intent();
                    double totalMoney=0;
                    switch (bean.getBalanceType()){
                        case BalanceName.Expend:
                            totalMoney= total_spent_money;
                            break;
                        case BalanceName.Income:
                            totalMoney= total_income_money;
                            break;
                        case BalanceName.Inner:
                            totalMoney= total_inner_money;
                            break;
                    }
                    intent.setClass(context, CategoryDetailsActivity.class);
                    intent.putExtra("detailGroup",bean);
                    intent.putExtra("title","类别详情");
                    intent.putExtra("total",totalMoney);
                    context.startActivity(intent);
                }
            });
        }

    }
}


