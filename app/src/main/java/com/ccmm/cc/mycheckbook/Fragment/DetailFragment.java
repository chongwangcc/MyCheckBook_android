package com.ccmm.cc.mycheckbook.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.ccmm.cc.mycheckbook.Adapter.DetailsOuterListAdapter;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.activity.DetailAddingActivity;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;

import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class DetailFragment extends Fragment implements View.OnClickListener{
    View view_detail; //明细的视图
    ListView detail_listView ; //明细列表
    DetailsOuterListAdapter detail_adapter; //明细数据适配器
    List<DetailGroupBean> detail_data; //明细数据
    protected boolean isCreated = false;

    public static DetailFragment newInstance() {
        Bundle args = new Bundle();
        return new DetailFragment();
    }

    public DetailFragment(){

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

        view = inflater.inflate(R.layout.checkbook_main_details_fragment, container, false);
        //2.设置按钮的点击事件处理方法
        Button join_checkbook_button= view.findViewById(R.id.add_detail_button);
        join_checkbook_button.setOnClickListener(this);
        view_detail=view;
        //3.设定List源
        List<CheckDetailBean> llBan =CheckDetailsTools.getAllDetailsInMonth(CheckbookTools.getSelectedCheckbook().getCheckbookID(),
                                               CheckDetailsTools.getDetails_year(),
                                               CheckDetailsTools.getDetals_month());
        detail_data =  CheckDetailsTools.detailsGroupByDay(llBan);
        detail_adapter = new DetailsOuterListAdapter(detail_data,this.getContext());
        detail_listView = view.findViewById(R.id.details_listview);
        detail_listView.setAdapter(detail_adapter);

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
            List<CheckDetailBean> llBan = CheckDetailsTools.getAllDetailsInMonth(CheckbookTools.getSelectedCheckbook().getCheckbookID(),
                    CheckDetailsTools.getDetails_year(),
                    CheckDetailsTools.getDetals_month());
            detail_data = CheckDetailsTools.detailsGroupByDay(llBan);
            detail_adapter = new DetailsOuterListAdapter(detail_data,getContext());
            detail_listView.setAdapter(detail_adapter);
            detail_adapter.notifyDataSetChanged();
            detail_listView.invalidate();
        }
    }
}


