package com.ccmm.cc.mycheckbook.MyControl;

/**
 * Created by cc on 2018/4/7.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ccmm.cc.mycheckbook.R;

/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class SelfDialog extends Dialog {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView year_messageTv;//消息提示文本
    private TextView month_messageTv;//消息提示文本


    private String yearStr;//从外界设置的title文本
    private String monthStr;//从外界设置的消息文本
    private Button year_add;//年-增加按钮
    private Button year_sub;//年-减少按钮
    private Button month_add;//月-增加按钮
    private Button month_sub;//月-减少按钮

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器


    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }

    public SelfDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_month_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
        year_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearStr = year_messageTv.getText().toString();
                int year = Integer.parseInt(yearStr);
                year=year+1;
                yearStr=year+"";
                year_messageTv.setText(yearStr);
            }
        });
        year_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearStr = year_messageTv.getText().toString();
                int year = Integer.parseInt(yearStr);
                year=year-1;
                yearStr=year+"";
                year_messageTv.setText(yearStr);
            }
        });
        month_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthStr = month_messageTv.getText().toString();
                int month = Integer.parseInt(monthStr);
                month=month+1;
                if(month<1)month=1;
                if(month>12) month=12;
                monthStr=month+"";
                month_messageTv.setText(monthStr);
            }
        });
        month_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthStr = month_messageTv.getText().toString();
                int month = Integer.parseInt(monthStr);
                month=month-1;
                if(month<1)month=1;
                if(month>12) month=12;
                monthStr=month+"";
                month_messageTv.setText(monthStr);
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (yearStr != null) {
            year_messageTv.setText(yearStr);
        }
        if (monthStr != null) {
            month_messageTv.setText(monthStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        year_add = (Button) findViewById(R.id.button3);
        year_sub = (Button) findViewById(R.id.button4);
        month_add = (Button) findViewById(R.id.button5);
        month_sub = (Button) findViewById(R.id.button6);

        year_messageTv = (TextView) findViewById(R.id.year_message);
        month_messageTv = (TextView) findViewById(R.id.month_message);
    }

    public String getYearStr() {
        return yearStr;
    }

    public void setYearStr(String yearStr) {
        this.yearStr = yearStr;
    }

    public String getMonthStr() {
        return monthStr;
    }

    public void setMonthStr(String monthStr) {
        this.monthStr = monthStr;
    }
    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
}