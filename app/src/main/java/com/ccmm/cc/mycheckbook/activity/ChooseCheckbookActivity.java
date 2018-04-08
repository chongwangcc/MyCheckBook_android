package com.ccmm.cc.mycheckbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.LoginTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/4/5.
 */

public class ChooseCheckbookActivity extends Activity {
    List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
    Button addCheckbookButton =null;
    ListView listView = null;
    SimpleAdapter adapter=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook_choose);
        //1.绑定listView 的data和view
        adapter = new SimpleAdapter(this,
                mList,
                R.layout.checkbook_listitem,      // 自定义布局格式
                new String[] { "PIC", "TITLE", "CONTENT" },
                new int[] { R.id.detail_pic, R.id.detail_content, R.id.listitem_content }
        );
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);


        //2.定义“添加记账本”按钮的单击事件
        addCheckbookButton=(Button)this.findViewById(R.id.add_checkbook_button);
        View.OnClickListener addCheckbookButtonHandler = new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                //1.弹出对话框
                Intent intent = new Intent();
                intent.setClass(ChooseCheckbookActivity.this, AddCheckbookFragmentActivity.class);
                startActivity(intent);
            }
        };
        addCheckbookButton.setOnClickListener(addCheckbookButtonHandler );

        //3.设定adapter的setViewBinder函数，使其能处理本地图片
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(
                    View view,
                    Object data,
                    String textRepresentation) {
                if(view !=null && view instanceof ImageView) {

                    if (data!=null && data instanceof Bitmap) {
                        ImageView imageView = (ImageView) view;
                        imageView.setImageDrawable(null);
                        imageView.setImageBitmap(null);
                        imageView.setImageBitmap((Bitmap) data);
                        return true;
                    }
                }
                return false;
            }
        });

        //4.设定listview的单击函数
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChooseCheckbookActivity.this,"你单击的是第"+(position+1)+"条数据",Toast.LENGTH_SHORT).show();
                //1.获得记账本信息
                CheckbookEntity checkbook =  CheckbookTools.getChecckbookByIndex(LoginTools.getLoginUser(),position);
                //view.findViewById()
                //2.跳转
                Intent intent = new Intent();
                intent.setClass(ChooseCheckbookActivity.this, CheckbookMainActivity.class);
                intent.putExtra("checkbook",checkbook);
                CheckbookTools.setSelectedCheckbook(checkbook);
                startActivity(intent);
            }
        });
        //5.新建一个默认记账本
        CheckbookEntity checkbook = CheckbookTools.checkInvitation(LoginTools.getLoginUser(),"test");
        CheckbookTools.addOneCheckbook(LoginTools.getLoginUser(),checkbook);
    }

    @Override
    public void onResume() {
        super.onResume();
        //1.更新listView中的数据
        List<CheckbookEntity> checkbookList=CheckbookTools.fetchAllCheckbook(LoginTools.getLoginUser());
        mList.clear();
        if(checkbookList!=null && checkbookList.size()>0){
            //2.添加数据
            for (CheckbookEntity checkbook : checkbookList) {
                if(checkbook!=null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("PIC", CheckbookTools.getBitmapFromCacher(checkbook));     // 加载图片资源
                    //map.put("PIC", R.drawable.ic_gf_camera);
                    map.put("TITLE", checkbook.getTitle());
                    map.put("CONTENT", checkbook.getDescription());
                    mList.add(map);
                }
            }
        }

        //通知更新
        adapter.notifyDataSetChanged();
        listView.invalidate();
    }
}