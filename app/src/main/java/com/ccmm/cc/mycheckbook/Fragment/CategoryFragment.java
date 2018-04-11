package com.ccmm.cc.mycheckbook.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.MyControl.PieGraphView;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.activity.DetailAddingActivity;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener{

    com.ccmm.cc.mycheckbook.MyControl.PieGraphView pieChart;

    private List<DetailGroupBean> detail_group_income; //明细数据
    private List<DetailGroupBean> detail_group_spend; //明细数据
    private List<DetailGroupBean> detail_group_inner; //明细数据

    protected boolean isCreated = false;
    private Integer mColors[] ={Color.RED,Color.GREEN,Color.BLACK,Color.BLUE,Color.CYAN };

    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        return new CategoryFragment();
    }

    public CategoryFragment(){

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
        setPieViewData();

        //2.TODO 设置处理点击处理事件

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

    public void setPieViewData(){
        //TODO 将类别值设置放到一个函数
        List<PieGraphView.ItemGroup> itemGroups = new LinkedList<>();
        if(detail_group_income.size()>0){
            PieGraphView.ItemGroup itemGroup = new PieGraphView.ItemGroup();
            itemGroup.id=BalanceName.Income;

            List<DetailGroupBean>  groupBeans=detail_group_income;
            List<PieGraphView.Item> items = new LinkedList<>();
            float totalMoney=0;
            for(int i=0;i<8 && i<groupBeans.size();i++){
                PieGraphView.Item item = new PieGraphView.Item();
                item.id = groupBeans.get(i).getCategoryType();
                item.value = groupBeans.get(i).getMoney();
                item.drawable = mColors[i];
                items.add(item);
                totalMoney+=groupBeans.get(i).getMoney();
            }
            itemGroup.items = items.toArray(new PieGraphView.Item[items.size()]);
            //TODO 设置title
            itemGroup.title="总收入\n"+totalMoney+"\n元";
            itemGroups.add(itemGroup);
        }

        if(detail_group_spend.size()>0){
            PieGraphView.ItemGroup itemGroup = new PieGraphView.ItemGroup();
            itemGroup.id=BalanceName.Expend;
            List<DetailGroupBean>  groupBeans=detail_group_spend;
            List<PieGraphView.Item> items = new LinkedList<>();
            float totalMoney=0;
            for(int i=0;i<8 && i<groupBeans.size();i++){
                PieGraphView.Item item = new PieGraphView.Item();
                item.id = groupBeans.get(i).getCategoryType();
                item.value = groupBeans.get(i).getMoney();
                item.drawable = mColors[i];
                items.add(item);
                totalMoney+=groupBeans.get(i).getMoney();
            }
            itemGroup.items = items.toArray(new PieGraphView.Item[items.size()]);
            //TODO 设置title
            itemGroup.title="总支出\n"+totalMoney+"\n元";
            itemGroups.add(itemGroup);
        }

        pieChart.setData(itemGroups.toArray(new PieGraphView.ItemGroup[itemGroups.size()]));
        pieChart.setRingWidthFactor(100);
    }

}


