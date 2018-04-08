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

import com.ccmm.cc.mycheckbook.Adapter.ParentAdapter;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.activity.AddOneDetailActivity;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;

import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class DetailFragment extends Fragment implements View.OnClickListener{
    View view_join;

    public static DetailFragment newInstance() {
        Bundle args = new Bundle();
        DetailFragment pageFragment = new DetailFragment();
        return pageFragment;
    }

    public DetailFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        view = inflater.inflate(R.layout.checkbook_main_details_fragment, container, false);
        //2.设置按钮的点击事件处理方法
        Button join_checkbook_button=(Button)view.findViewById(R.id.add_detail_button);
        join_checkbook_button.setOnClickListener(this);
        view_join=view;
        //3.设定List源
        List<DetailGroupBean> ll =  CheckDetailsTools.getDetailGroupByDay();
        ParentAdapter adapter = new ParentAdapter(ll,this.getContext());
        ListView listView = (ListView) view.findViewById(R.id.details_listview);
        listView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.add_detail_button:
                //跳转
                Intent intent = new Intent();
                intent.setClass(this.getActivity(), AddOneDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}


