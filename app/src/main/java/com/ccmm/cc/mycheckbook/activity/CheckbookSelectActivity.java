package com.ccmm.cc.mycheckbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Adapter.DetailsInnerListAdapter;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.LoginTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/4/5.
 */

public class CheckbookSelectActivity extends Activity {
    private List<Map<String, Object>> mList = new ArrayList<>();
    private Button addCheckbookButton =null;
    private Button returnloginButton =null;
    private ListView listView = null;
    private SimpleAdapter adapter=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook_choose);

        //1.绑定listView 的data和view
        adapter = new SimpleAdapter(this,
                mList,
                R.layout.listitem_checkbook,      // 自定义布局格式
                new String[] { "PIC", "TITLE", "CONTENT" },
                new int[] { R.id.detail_pic, R.id.detail_title_1, R.id.listitem_content }
        );
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);


        //2.定义“添加记账本”按钮的单击事件
        addCheckbookButton=this.findViewById(R.id.add_checkbook_button);
        View.OnClickListener addCheckbookButtonHandler = new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                //1.弹出对话框
                Intent intent = new Intent();
                intent.setClass(CheckbookSelectActivity.this, CheckbookAddingFragmentActivity.class);
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

                //1.获得记账本信息
                CheckbookEntity checkbook =  CheckbookTools.getCheckbookByIndex(LoginTools.getLoginUser(),position);
                Toast.makeText(CheckbookSelectActivity.this,"打开记账本...."+checkbook.getTitle()+"",Toast.LENGTH_SHORT).show();
                //view.findViewById()
                //2.跳转
                Intent intent = new Intent();
                intent.setClass(CheckbookSelectActivity.this, CheckbookMainActivity.class);
                intent.putExtra("checkbook",checkbook);
                CheckbookTools.setSelectedCheckbook(checkbook);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                //1.获得明细数据,缓存
                CheckbookEntity checkbook =  CheckbookTools.getCheckbookByIndex(LoginTools.getLoginUser(),position);
                CheckbookTools.setDeleteCheckbook_cacher(checkbook);
                return false;
            }
        });

        //5.设置返回登录界面按钮
        returnloginButton=this.findViewById(R.id.button21);
        View.OnClickListener returnlonginButtonHandler = new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                //1.弹出对话框
                Intent intent = new Intent();
                intent.setClass(CheckbookSelectActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        returnloginButton.setOnClickListener(returnlonginButtonHandler );
        this.registerForContextMenu(listView);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("你想干啥？");
        menu.add(0, 0, Menu.NONE, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getMenuInfo() instanceof AdapterView.AdapterContextMenuInfo) {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //处理菜单的点击事件
            switch (item.getItemId()) {
                case 0: //删除对话框
                    //1.删除数据
                    CheckbookEntity bean = CheckbookTools.getDeleteCheckbook_cacher();
                    CheckbookTools.deleteCheckbookByID(LoginTools.getLoginUser(),bean);
                    Toast.makeText(this.getApplicationContext(),"删除记账本...."+bean.getCheckbookID()+"",Toast.LENGTH_SHORT).show();
                    //mTextView.setText(item.getTitle().toString() + menuInfo.position);
                    updateCheckbookData();
                    break;
                default :
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCheckbookData();
    }

    /***
     *更新记账本数据
     */
    public void updateCheckbookData(){
        //1.更新listView中的数据
        List<CheckbookEntity> checkbookList=CheckbookTools.fetchAllCheckbook(LoginTools.getLoginUser());
        mList.clear();
        if(checkbookList!=null && checkbookList.size()>0){
            //2.添加数据
            for (CheckbookEntity checkbook : checkbookList) {
                if(checkbook!=null) {
                    Map<String, Object> map = new HashMap<>();
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