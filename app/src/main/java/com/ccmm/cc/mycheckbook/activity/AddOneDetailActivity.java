package com.ccmm.cc.mycheckbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.DecimalFormat;
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
    private String accountName="";
    private Toolbar toolbar;
    ViewPager vpager_one;
    List<View> AList;
    List<ImageView> allImageView=new LinkedList<>();
    List<TextView> allTextView = new LinkedList<>();
    int m_circle_which=0;
    LinearLayout lltPageIndicator;
    AdapterViewpager mAdapter;
    Button button_selectData;
    Button button_account;
    Button button_Note;
    TextView spendType_View;
    TextView money_textView11;
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

        //4.选择账户按钮
        button_account = (Button) findViewById(R.id.button_account);
        button_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAccountDialog();
            }
        });

        //5.备注按钮被点击
        button_Note = (Button) findViewById(R.id.button_description);
        button_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDescriptionDialog();
            }
        });

        //6.设置计算用按钮
        money_textView11=(TextView) findViewById(R.id.textView11);
        settingMoneyButton();
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
        allImageView = new LinkedList<>();
        allTextView = new LinkedList<>();
        allImageView.add((ImageView)v.findViewById(R.id.imageView1_1));
        allImageView.add((ImageView)v.findViewById(R.id.imageView1_2));
        allImageView.add((ImageView)v.findViewById(R.id.imageView1_3));
        allImageView.add((ImageView)v.findViewById(R.id.imageView1_4));
        allImageView.add((ImageView)v.findViewById(R.id.imageView1_5));
        allImageView.add((ImageView)v.findViewById(R.id.imageView2_1));
        allImageView.add((ImageView)v.findViewById(R.id.imageView2_2));
        allImageView.add((ImageView)v.findViewById(R.id.imageView2_3));
        allImageView.add((ImageView)v.findViewById(R.id.imageView2_4));
        allImageView.add((ImageView)v.findViewById(R.id.imageView2_5));
        allTextView.add((TextView)v.findViewById(R.id.textView1_1));
        allTextView.add((TextView)v.findViewById(R.id.textView1_2));
        allTextView.add((TextView)v.findViewById(R.id.textView1_3));
        allTextView.add((TextView)v.findViewById(R.id.textView1_4));
        allTextView.add((TextView)v.findViewById(R.id.textView1_5));
        allTextView.add((TextView)v.findViewById(R.id.textView2_1));
        allTextView.add((TextView)v.findViewById(R.id.textView2_2));
        allTextView.add((TextView)v.findViewById(R.id.textView2_3));
        allTextView.add((TextView)v.findViewById(R.id.textView2_4));
        allTextView.add((TextView)v.findViewById(R.id.textView2_5));
        List<String> listStr = null;
        switch (spendType_status){
            case "收入":
                listStr = SpentTypeEnum.getIncomeType(position);
                setListView(listStr,allImageView,allTextView);
                break;
            case "支出":
                listStr = SpentTypeEnum.getSpentType(position);
                setListView(listStr,allImageView,allTextView);
                break;
            case "内部转账":
                break;
        }

        return v;
    }
    private boolean setListView(List<String> listname,final List<ImageView> listImage,List<TextView> listText){
        for(int i=0;i<listname.size();i++){
            String name = listname.get(i);
            listImage.get(i).setImageResource(SpentTypeEnum.getDrawableIndex(name));
            listText.get(i).setText(name);
            listImage.get(i).setVisibility(View.VISIBLE);
            listText.get(i).setVisibility(View.VISIBLE);
            //设置绑定函数
            listImage.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for(ImageView v : listImage){
                        Drawable drawable =  v.getDrawable();
                        drawable.clearColorFilter();
                        v.setImageDrawable(drawable);
                    }
                    Drawable drawable = ((ImageView)view).getDrawable().mutate();
                    switch (spendType_status){
                        case "收入":
                            drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                            ((ImageView)view).setImageDrawable(drawable);
                            break;
                        case "支出":
                            drawable.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                            ((ImageView)view).setImageDrawable(drawable);
                            break;
                    }
                }
            });
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
                updateViewPager();
            }
        });
        builder.show();
    }

    private void ShowAccountDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddOneDetailActivity.this,android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择账户");
        //    指定下拉列表的显示数据
        final String[] cities = {"Inbox",
                                    "花销-生活费-现金",
                                   "花销-doodads-现金",
                                    "花销-学习-现金",
                                    "花销-风险备付金-现金",
                                    "花销-生活费-信用卡",
                                    "花销-doodads-信用卡",
                                    "花销-学习-信用卡",
                                    "花销-风险备付金-现金",
                                    "投资-股票",
                                    "投资-货币基金"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                accountName=cities[which];
                button_account.setText(accountName);
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
                m_circle_which=position;
            }
            //滚动成功前，即手指按下屏幕时
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for(int i=0;i<SpentTypeEnum.getLengthType(spendType_status);i++){
            ((ImageView)lltPageIndicator.getChildAt(i)).setImageResource(R.drawable.cicle_unactive);
        }
        ((ImageView)lltPageIndicator.getChildAt(0)).setImageResource(R.drawable.circle_active);
    }
    private void ShowDescriptionDialog(){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("备注")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        Toast.makeText(getApplicationContext(),  input, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void settingMoneyButton(){
        Button button_1 = (Button) findViewById(R.id.button7);
        Button button_2 = (Button) findViewById(R.id.button8);
        Button button_3 = (Button) findViewById(R.id.button9);
        Button button_4 = (Button) findViewById(R.id.button10);
        Button button_5 = (Button) findViewById(R.id.button11);
        Button button_6 = (Button) findViewById(R.id.button12);
        Button button_7 = (Button) findViewById(R.id.button13);
        Button button_8 = (Button) findViewById(R.id.button14);
        Button button_9 = (Button) findViewById(R.id.button15);
        Button button_0 = (Button) findViewById(R.id.button19);
        Button button_back = (Button) findViewById(R.id.button16);
        Button button_finish = (Button) findViewById(R.id.button17);
        Button button_point = (Button) findViewById(R.id.button18);
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
        View.OnClickListener button__num_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String money = money_textView11.getText().toString();
                String num=((Button)view).getText().toString();
                String newMoney="";
                if(money.equals("0")){
                    if(num.equals(".")){
                        newMoney=money+num;
                    }else{
                        newMoney=num;
                    }
                }else{
                    if(num.equals(".")){
                        if(money.contains(".")){
                            newMoney=money;
                        }else{
                            newMoney=money+num;
                        }
                    }else{
                        if(money.contains(".")){
                            if(money.length()-money.indexOf(".")>2){
                                newMoney=money;
                            }else{
                                newMoney=money+num;
                            }

                        }else{
                            newMoney=money+num;
                        }
                    }
                }
                money_textView11.setText(newMoney);
            }
        };
        View.OnClickListener button__back_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = money_textView11.getText().toString();

                try{
                    money=money.substring(0,money.length()-1);
                    money_textView11.setText(money);
                }catch(Exception e){
                    money_textView11.setText("0");
                }
            }
        };
        View.OnClickListener button__finish_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 保存一条明细记录
                //money_textView11.setText("0");
            }
        };
        View.OnClickListener button__delete_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money_textView11.setText("0");
            }
        };
        button_1.setOnClickListener(button__num_listener);
        button_2.setOnClickListener(button__num_listener);
        button_3.setOnClickListener(button__num_listener);
        button_4.setOnClickListener(button__num_listener);
        button_5.setOnClickListener(button__num_listener);
        button_6.setOnClickListener(button__num_listener);
        button_7.setOnClickListener(button__num_listener);
        button_8.setOnClickListener(button__num_listener);
        button_9.setOnClickListener(button__num_listener);
        button_0.setOnClickListener(button__num_listener);
        button_point.setOnClickListener(button__num_listener);
        button_back.setOnClickListener(button__back_listener);
        imageView2.setOnClickListener(button__delete_listener);
        button_finish.setOnClickListener(button__finish_listener);

    }
}
