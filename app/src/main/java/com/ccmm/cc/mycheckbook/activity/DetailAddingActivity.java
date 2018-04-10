package com.ccmm.cc.mycheckbook.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Adapter.CategoryViewPagerAdapter;
import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.utils.CategoriesIconTool;
import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.utils.CheckDetailsTools;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.TwoTuple;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/4/7.
 */

public class DetailAddingActivity extends AppCompatActivity {
    private final List<String> lis = BalanceName.getALlNames();
    private CheckDetailBean status = null;
    private CategoriesChoice m_categoriesChoice;
    private Map<String,CategoriesChoice> categoryMap=new HashMap<>(); //
    private boolean newOrModefy=true; //false--新建，true==修改

    private Toolbar toolbar;
    private ViewPager vpager_one;
    private CategoryViewPagerAdapter mAdapter;
    private Button button_selectData;
    private Button button_account;
    private Button button_Note;
    private TextView balanceType_View;
    private TextView money_textView11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_one_detail);

        //0.获得数据
        Intent intent = getIntent();
        status = (CheckDetailBean)intent.getSerializableExtra("detailBean");
        if(status ==null){
            status = new CheckDetailBean();
            newOrModefy=false;
        }else{
            newOrModefy=true;
        }

        for(String name:lis){
            CategoriesChoice cc = new CategoriesChoice(this, CategoriesIconTool.getAllCategoryNames(name));
            categoryMap.put(name,cc);
        }

        //2.添加选择日期按钮
        button_selectData = findViewById(R.id.select_date);
        button_selectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //3.设置toolbar
        settingToolBar(CheckbookTools.getSelectedCheckbook());
        balanceType_View = findViewById(R.id.spend_type_TextView);

        //4.选择账户按钮
        button_account =  findViewById(R.id.button_account);
        button_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountDialog();
            }
        });

        //5.备注按钮被点击
        button_Note =  findViewById(R.id.button_description);
        button_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescriptionDialog();
            }
        });

        //6.设置计算用按钮
        money_textView11= findViewById(R.id.textView11);
        settingMoneyButtons();

        // 根据status显示数据
        money_textView11.setText(status.getMoneyStr());
        button_selectData.setText(status.getDate());
        if(status.getAccount()==null || status.getAccount().isEmpty()){
            button_account.setText("选择账户");
        }else{
            button_account.setText(status.getAccount());
        }
        for(String name:categoryMap.keySet()){
            CategoriesChoice cc =categoryMap.get(name);
            if(name.equals(status.getBalanceType())){
                setM_categoriesChoice(cc);
                m_categoriesChoice.setSelectCategory(status.getCategory());
                balanceType_View.setText(status.getBalanceType());
            }
        }
        //TODO ListView显示有问题
        //TODO BUG 支出，收入，图标同时选中的问题
        //1.设置ViewPage页面
        vpager_one =  findViewById(R.id.viewpager_type);
        mAdapter = new CategoryViewPagerAdapter(m_categoriesChoice.getAList());
        vpager_one.setAdapter(mAdapter);
        vpager_one.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动过程中实现
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //滚动成功后实现
            @Override
            public void onPageSelected(int position) {
                m_categoriesChoice.setIndicatorPostion(position);
            }
            //滚动成功前，即手指按下屏幕时
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /***
     * 设置工具栏
     * @param checkbook
     */
    private void settingToolBar(CheckbookEntity checkbook) {
        toolbar =  findViewById(R.id.checkbook_main_toolbar);
        setSupportActionBar(toolbar);//设置导航图标要在setSupportActionBar方法之后
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar =  findViewById(R.id.checkbook_main_toolbar);
        TextView text = findViewById(R.id.spend_type_TextView);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBalanceTypeOptions();
            }
        });
    }

    /***
     * 设置数字选择按钮群
     */
    private void settingMoneyButtons(){
        Button button_1 =  findViewById(R.id.button7);
        Button button_2 =  findViewById(R.id.button8);
        Button button_3 =  findViewById(R.id.button9);
        Button button_4 =  findViewById(R.id.button10);
        Button button_5 = findViewById(R.id.button11);
        Button button_6 =  findViewById(R.id.button12);
        Button button_7 =  findViewById(R.id.button13);
        Button button_8 = findViewById(R.id.button14);
        Button button_9 =  findViewById(R.id.button15);
        Button button_0 =  findViewById(R.id.button19);
        Button button_back =  findViewById(R.id.button16);
        Button button_finish =  findViewById(R.id.button17);
        Button button_point =  findViewById(R.id.button18);
        ImageView imageView2 = findViewById(R.id.imageView2);
        View.OnClickListener button__num_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String money = money_textView11.getText().toString();
                String num=((Button)view).getText().toString();
                String newMoney;
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
                if(status.getCategory()==null || status.getCategory().isEmpty()){
                    status.setCategory("一般");
                }
                if(status.getAccount()==null || status.getAccount().isEmpty()){
                    status.setAccount("Inbox");
                }
                //2. 显示记录数据
                //Toast.makeText(getApplicationContext(),  status.toString(), Toast.LENGTH_SHORT).show();

                //3.保存一条明细记录
                if(!newOrModefy){
                    //新建一条记录
                    if(status.getMoney()>0){
                        CheckDetailsTools.addOneCheckDetails(status);
                    }
                }else{
                    //修改一条记录
                    if(status.getMoney()>0){
                        CheckDetailsTools.modifyOneCheckDetails(status);
                    }else{
                        CheckDetailsTools.deleteCheckDetail(status);
                    }
                }

                //4.退回上一个Activity

                Intent intent = new Intent();
                intent.putExtra("detailBean",status);
                DetailAddingActivity.this.setResult(RESULT_OK, intent);
                finish();

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
        new DatePickerDialog(DetailAddingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monthStr=(monthOfYear+1)+"";
                String dayStr = dayOfMonth+"";
                if(monthStr.length()==1) monthStr="0"+monthStr;
                if(dayStr.length()==1) dayStr="0"+dayStr;
                String date = year+"-"+monthStr+"-"+dayStr;
                status.setDate(date);
                button_selectData.setText(status.getDate());
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    /***
     * 选择账单类型
     */
    private void showBalanceTypeOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailAddingActivity.this,android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择账单类型");
        //    指定下拉列表的显示数据
        final String[] balances = {BalanceName.Income,
                BalanceName.Expend,
                BalanceName.Inner};
        //    设置一个下拉的列表选择项
        builder.setItems(balances, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                status.setBalanceType(balances[which]);
                balanceType_View.setText(status.getBalanceType());
                setM_categoriesChoice(categoryMap.get(status.getBalanceType()));
                mAdapter = new CategoryViewPagerAdapter(m_categoriesChoice.getAList());
                vpager_one.setAdapter(mAdapter);
            }
        });
        builder.show();
    }

    /***
     * 展示选择账户的界面
     */
    private void showAccountDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailAddingActivity.this,android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);
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

    /***
     * 填写备注信息
     */
    private void showDescriptionDialog(){
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
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


    public void setM_categoriesChoice(CategoriesChoice m_categoriesChoice) {
        //1.之前的清零
        if(this.m_categoriesChoice!=null){
            this.m_categoriesChoice.setSelectCategory(null);
        }
        this.m_categoriesChoice = m_categoriesChoice;
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
        private int page_total_num; //总共有多少个页

        private CategoriesChoice(Context context,List<List<String>> typeNames){
            page_total_num=typeNames.size();
            for(List<String> level_one:typeNames){
                //1.创建View
                View v = View.inflate(context, R.layout.circle_page, null);
                AList.add(v);
                List<TwoTuple<ImageView,TextView>> listTuple = new LinkedList<>();
                for(int i=0;i<10;i++){
                    ImageView im = v.findViewById(All_Image_ID[i]);
                    TextView tv = v.findViewById(All_Text_ID[i]);
                    TwoTuple<ImageView,TextView> tuple = new TwoTuple<>(im,tv);
                    listTuple.add(tuple);
                }
                ViewMap.put(v,listTuple);
                //2.设置状态
                setListView(level_one,listTuple);
            }
            //3.设置状态指示器
            lltPageIndicator =  findViewById(R.id.llt_page_indicator);
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
         * @param position 位置值
         */
        private void setIndicatorPostion(int position){
            for(int i=0;i<page_total_num;i++){
                ((ImageView)lltPageIndicator.getChildAt(i)).setImageResource(R.drawable.cicle_unactive);
                lltPageIndicator.getChildAt(i).setVisibility(View.VISIBLE);
            }
            ((ImageView)lltPageIndicator.getChildAt(position)).setImageResource(R.drawable.circle_active);
        }

        /***
         * 设置ListView
         * @param listname 所有类别的名称
         * @param tuple view的颜色
         * @return
         */
        private boolean setListView(List<String> listname,final List<TwoTuple<ImageView,TextView>> tuple){
            for(int i=0;i<listname.size();i++){
                String name = listname.get(i);
                ImageView imageView = tuple.get(i).first;
                final TextView textView = tuple.get(i).second;

                imageView.setImageResource(CategoriesIconTool.getDrawableIndex(name));
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
                            CategoriesIconTool.tintDrawable(drawable, ColorStateList.valueOf(Color.GRAY));
                            v.setImageDrawable(drawable);
                        }
                        Drawable drawable = ((ImageView)view).getDrawable().mutate();
                        drawable=CategoriesIconTool.changeDrawableByBalanceType(drawable,status.getBalanceType());
                        ((ImageView)view).setImageDrawable(drawable);
                        status.setCategory(textView.getText().toString());
                    }
                });
            }
            for(int i=listname.size();i<tuple.size();i++){
                tuple.get(i).first.setVisibility(View.INVISIBLE);
                tuple.get(i).second.setVisibility(View.INVISIBLE);
            }
            return true;
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

        public void clearStatus(){

        }

        public void setSelectCategory(String category){
            if(category==null){
                category="";
            }
            int p=0;
            for(View v :AList){
                p++;
                List<TwoTuple<ImageView,TextView>> listview = ViewMap.get(v);
                for(TwoTuple<ImageView,TextView> tump :listview){
                    if(tump.getSecond().getText().toString().equals(category)){
                        // 更改Image状态
                        ImageView view = tump.getFirst();
                        Drawable drawable = ((ImageView)view).getDrawable().mutate();
                        drawable=CategoriesIconTool.changeDrawableByBalanceType(drawable,status.getBalanceType());
                        ((ImageView)view).setImageDrawable(drawable);
                        setIndicatorPostion(p);

                    }else{
                        ImageView view = tump.getFirst();
                        Drawable drawable = ((ImageView)view).getDrawable().mutate();
                        drawable=CategoriesIconTool.changeDrawableByBalanceType(drawable,"default");
                        ((ImageView)view).setImageDrawable(drawable);
                    }
                }
            }
        }
    }
}
