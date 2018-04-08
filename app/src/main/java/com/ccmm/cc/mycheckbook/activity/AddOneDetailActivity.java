package com.ccmm.cc.mycheckbook.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.TwoTuple;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/4/7.
 */

public class AddOneDetailActivity extends AppCompatActivity {
    private final String[] lis = {"收入","支出","内部转账"};
    private CheckDetailBean status = new CheckDetailBean();
    private CategoriesChoice categoriesChoice ;
    private Map<String,CategoriesChoice> categoryMap=new HashMap<>();

    private Toolbar toolbar;
    ViewPager vpager_one;
    AdapterViewpager mAdapter;
    Button button_selectData;
    Button button_account;
    Button button_Note;
    TextView spendType_View;
    TextView money_textView11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String selectDate=sdf.format(dt);
        status.setDate(selectDate);
        setContentView(R.layout.activity_add_one_detail);
        for(String name:lis){
            CategoriesChoice cc = new CategoriesChoice(this,SpentTypeEnum.getAllCategoryNames(name));
            categoryMap.put(name,cc);
            if(name.equals(status.getIncomeType())){
                categoriesChoice = cc;
            }
        }

        //1.设置ViewPage页面
        vpager_one = (ViewPager) findViewById(R.id.viewpager_type);
        mAdapter = new AdapterViewpager(categoriesChoice.getAList());
        vpager_one.setAdapter(mAdapter);
        vpager_one.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动过程中实现
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //滚动成功后实现
            @Override
            public void onPageSelected(int position) {
                categoriesChoice.setPage_posion(position);
            }
            //滚动成功前，即手指按下屏幕时
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //2.添加选择日期按钮
        button_selectData = (Button) findViewById(R.id.select_date);
        button_selectData.setText(status.getDate());
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
                showAccountDialog();
            }
        });

        //5.备注按钮被点击
        button_Note = (Button) findViewById(R.id.button_description);
        button_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescriptionDialog();
            }
        });

        //6.设置计算用按钮
        money_textView11=(TextView) findViewById(R.id.textView11);
        settingMoneyButton();
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
                showSpendTypeChoise();
            }
        });
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

                if(newMoney.length()-money.indexOf(".")>10){
                    newMoney=money;
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
                    if(money.length()<1) money="0";
                    money_textView11.setText(money);
                }catch(Exception e){
                    money_textView11.setText("0");
                }
            }
        };
        View.OnClickListener button__finish_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.获得money，保存到状态数据中
                String money=money_textView11.getText().toString();
                status.setMoneyStr(money);

                //2. 显示记录数据
                Toast.makeText(getApplicationContext(),  status.toString(), Toast.LENGTH_SHORT).show();
                //TODO 保存一条明细记录
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

    /**
     * 展示日期选择对话框
     */
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(AddOneDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                status.setDate(date);
                button_selectData.setText(status.getDate());
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showSpendTypeChoise(){
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
                status.setIncomeType(cities[which]);
                spendType_View.setText(status.getIncomeType());
                //updateViewPager();
                categoriesChoice=categoryMap.get(status.getIncomeType());
                mAdapter = new AdapterViewpager(categoriesChoice.getAList());
                vpager_one.setAdapter(mAdapter);
            }
        });
        builder.show();
    }

    private void showAccountDialog(){
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
                status.setAccount(cities[which]);
                button_account.setText(status.getAccount());
            }
        });
        builder.show();
    }

    private void showDescriptionDialog(){
        final EditText et = new EditText(this);
        et.setText(status.getDescription());
        new AlertDialog.Builder(this).setTitle("备注")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        status.setDescription(input);
                        Toast.makeText(getApplicationContext(),  status.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    /***
     * 内部类，类别的选择，居家，零食，娱乐等
     */
    private class CategoriesChoice{
        private final int[] All_Image_ID={R.id.imageView1_1,
                                              R.id.imageView1_2,
                                              R.id.imageView1_3,
                                              R.id.imageView1_4,
                                              R.id.imageView1_5,
                                              R.id.imageView2_1,
                                              R.id.imageView2_2,
                                              R.id.imageView2_3,
                                                R.id.imageView2_4,
                                                R.id.imageView2_5};
        private final int[] All_Text_ID={R.id.textView1_1,
                                            R.id.textView1_2,
                                            R.id.textView1_3,
                                            R.id.textView1_4,
                                            R.id.textView1_5,
                                            R.id.textView2_1,
                                            R.id.textView2_2,
                                            R.id.textView2_3,
                                            R.id.textView2_4,
                                            R.id.textView2_5};
        private List<View> AList=new LinkedList<>(); //每个滑动的界面
        private List<ImageView> allImageView=new LinkedList<>();
        private Map<View,List<TwoTuple<ImageView,TextView>>> ViewMap=new HashMap<>();
        private LinearLayout lltPageIndicator;
        private int page_posion=0; //滑动到了第几个页码上了
        private int page_total_num=0; //总共有多少个页

        public CategoriesChoice(Context context,List<List<String>> typeNames){
            page_total_num=typeNames.size();
            for(List<String> level_one:typeNames){
                //1.创建View
                View v = View.inflate(context, R.layout.circle_page, null);
                AList.add(v);
                List<TwoTuple<ImageView,TextView>> listTuple = new LinkedList<>();
                for(int i=0;i<10;i++){
                    ImageView im = (ImageView)v.findViewById(All_Image_ID[i]);
                    TextView tv = (TextView)v.findViewById(All_Text_ID[i]);
                    TwoTuple<ImageView,TextView> tuple = new TwoTuple<ImageView, TextView>(im,tv);
                    listTuple.add(tuple);
                }
                ViewMap.put(v,listTuple);
                //2.设置状态
                setListView(level_one,listTuple);
            }
            //3.设置状态指示器
            lltPageIndicator = (LinearLayout) findViewById(R.id.llt_page_indicator);
            for(int i=0;i<page_total_num;i++){
                lltPageIndicator.getChildAt(i).setVisibility(View.VISIBLE);
            }
            for(int i=page_total_num;i<4;i++){
                lltPageIndicator.getChildAt(i).setVisibility(View.INVISIBLE);
            }
            setIndicatorPostion(0);
        }

        /***
         * 设置指示器的位置值
         * @param position
         */
        private void setIndicatorPostion(int position){
            for(int i=0;i<page_total_num;i++){
                ((ImageView)lltPageIndicator.getChildAt(i)).setImageResource(R.drawable.cicle_unactive);
            }
            ((ImageView)lltPageIndicator.getChildAt(position)).setImageResource(R.drawable.circle_active);
        }

        /***
         * 设置ListView
         * @param listname
         * @param tuple
         * @return
         */
        private boolean setListView(List<String> listname,final List<TwoTuple<ImageView,TextView>> tuple){
            for(int i=0;i<listname.size();i++){
                String name = listname.get(i);
                ImageView imageView = tuple.get(i).first;
                final TextView textView = tuple.get(i).second;

                imageView.setImageResource(SpentTypeEnum.getDrawableIndex(name));
                textView.setText(name);
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                allImageView.add(imageView);
                //设置绑定函数
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        for(ImageView v : allImageView){
                            Drawable drawable =  v.getDrawable();
                            drawable.clearColorFilter();
                            v.setImageDrawable(drawable);
                        }
                        Drawable drawable = ((ImageView)view).getDrawable().mutate();
                        switch (status.getIncomeType()){
                            case "收入":
                                drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                ((ImageView)view).setImageDrawable(drawable);
                                break;
                            case "支出":
                                drawable.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                ((ImageView)view).setImageDrawable(drawable);
                                break;
                        }
                        status.setBuyType(textView.getText().toString());
                    }
                });
            }
            for(int i=listname.size();i<tuple.size();i++){
                tuple.get(i).first.setVisibility(View.INVISIBLE);
                tuple.get(i).second.setVisibility(View.INVISIBLE);
            }
            return true;
        }

        public int getPage_posion() {
            return page_posion;
        }

        public void setPage_posion(int page_posion) {
            this.page_posion = page_posion;
            setIndicatorPostion(page_posion);
        }

        public int getPage_total_num() {
            return page_total_num;
        }

        public void setPage_total_num(int page_total_num) {
            this.page_total_num = page_total_num;
        }

        public List<View> getAList() {
            return AList;
        }

    }
}
