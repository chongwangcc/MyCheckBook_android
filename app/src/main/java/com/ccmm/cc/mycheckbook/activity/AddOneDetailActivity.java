package com.ccmm.cc.mycheckbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Adapter.AdapterViewpager;
import com.ccmm.cc.mycheckbook.Enum.SpentTypeEnum;
import com.ccmm.cc.mycheckbook.MyControl.SelfDialog;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cc on 2018/4/7.
 */

public class AddOneDetailActivity extends AppCompatActivity {

    private String spendType_status="支出"; //收入，支出，内部转账
    private String selectDetailsType="";
    private Toolbar toolbar;
    ViewPager vpager_one;
    List<View> AList;
    int m_position=0;

    LinearLayout lltPageIndicator;
    AdapterViewpager mAdapter;
    Button button_selectData;
    TextView spendType_View;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_one_detail);

        //1.设置ViewPage页面
        vpager_one = (ViewPager) findViewById(R.id.viewpager_type);
        lltPageIndicator = (LinearLayout) findViewById(R.id.llt_page_indicator);

        //2.添加选择日期按钮
        button_selectData = (Button) findViewById(R.id.select_date);
        button_selectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //3.设置toolbar
        settingToolBar(CheckbookTools.getSelectedCheckbook());
        spendType_View=(TextView) findViewById(R.id.spend_type_TextView);
    }
    @Override
    public void onResume() {
        super.onResume();
        updateViewPager();
    }

    /**
     * 展示日期选择对话框
     */
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(AddOneDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                button_selectData.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private View getGuidePage(int position) {
        View v = View.inflate(this, R.layout.circle_page, null);
        List<ImageView> listImage = new LinkedList<>();
        List<TextView> listText = new LinkedList<>();
        listImage.add((ImageView)v.findViewById(R.id.imageView1_1));
        listImage.add((ImageView)v.findViewById(R.id.imageView1_2));
        listImage.add((ImageView)v.findViewById(R.id.imageView1_3));
        listImage.add((ImageView)v.findViewById(R.id.imageView1_4));
        listImage.add((ImageView)v.findViewById(R.id.imageView1_5));
        listImage.add((ImageView)v.findViewById(R.id.imageView2_1));
        listImage.add((ImageView)v.findViewById(R.id.imageView2_2));
        listImage.add((ImageView)v.findViewById(R.id.imageView2_3));
        listImage.add((ImageView)v.findViewById(R.id.imageView2_4));
        listImage.add((ImageView)v.findViewById(R.id.imageView2_5));
        listText.add((TextView)v.findViewById(R.id.textView1_1));
        listText.add((TextView)v.findViewById(R.id.textView1_2));
        listText.add((TextView)v.findViewById(R.id.textView1_3));
        listText.add((TextView)v.findViewById(R.id.textView1_4));
        listText.add((TextView)v.findViewById(R.id.textView1_5));
        listText.add((TextView)v.findViewById(R.id.textView2_1));
        listText.add((TextView)v.findViewById(R.id.textView2_2));
        listText.add((TextView)v.findViewById(R.id.textView2_3));
        listText.add((TextView)v.findViewById(R.id.textView2_4));
        listText.add((TextView)v.findViewById(R.id.textView2_5));
        List<String> listStr = null;
        switch (spendType_status){
            case "收入":
                listStr = SpentTypeEnum.getIncomeType(position);
                setListView(listStr,listImage,listText);
                break;
            case "支出":
                listStr = SpentTypeEnum.getSpentType(position);
                setListView(listStr,listImage,listText);
                break;
            case "内部转账":
                break;
        }

        return v;
    }
    private boolean setListView(List<String> listname,List<ImageView> listImage,List<TextView> listText){
        for(int i=0;i<listname.size();i++){
            String name = listname.get(i);
            listImage.get(i).setImageResource(SpentTypeEnum.getDrawableIndex(name));
            listText.get(i).setText(name);
            listImage.get(i).setVisibility(View.VISIBLE);
            listText.get(i).setVisibility(View.VISIBLE);
        }
        for(int i=listname.size();i<listImage.size();i++){
            listImage.get(i).setVisibility(View.INVISIBLE);
        }
        for(int i=listname.size();i<listText.size();i++){
            listText.get(i).setVisibility(View.INVISIBLE);
        }
        return true;
    }
    private void settingToolBar(CheckbookEntity checkbook) {
        toolbar = (Toolbar) findViewById(R.id.checkbook_main_toolbar);
        //toolbar.setTitle(spendType_status);
        setSupportActionBar(toolbar);//设置导航图标要在setSupportActionBar方法之后
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.checkbook_main_toolbar);

        TextView text = (TextView) findViewById(R.id.spend_type_TextView);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSpendTypeChoise();
            }
        });
    }
    private void ShowSpendTypeChoise(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddOneDetailActivity.this,android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择收入类型");
        //    指定下拉列表的显示数据
        final String[] cities = {"收入", "支出", "内部转账"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                spendType_status=cities[which];
                spendType_View.setText(spendType_status);
                m_position=which;
                updateViewPager();
            }
        });
        builder.show();
    }

    private void updateViewPager(){
        AList = new ArrayList<View>();
        for (int i = 0; i < SpentTypeEnum.getLengthType(spendType_status); i++) {
            AList.add(getGuidePage(i));
        }
        for(int i=0;i<SpentTypeEnum.getLengthType(spendType_status);i++){
            lltPageIndicator.getChildAt(i).setVisibility(View.VISIBLE);
        }
        for(int i=SpentTypeEnum.getLengthType(spendType_status);i<4;i++){
            lltPageIndicator.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        lltPageIndicator.getChildAt(m_position).setVisibility(View.INVISIBLE);
        mAdapter = new AdapterViewpager(AList);
        vpager_one.setAdapter(mAdapter);
        vpager_one.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动过程中实现
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //滚动成功后实现
            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<SpentTypeEnum.getLengthType(spendType_status);i++){
                    ((ImageView)lltPageIndicator.getChildAt(i)).setImageResource(R.drawable.cicle_unactive);
                }
                ((ImageView)lltPageIndicator.getChildAt(position)).setImageResource(R.drawable.circle_active);
            }
            //滚动成功前，即手指按下屏幕时
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
