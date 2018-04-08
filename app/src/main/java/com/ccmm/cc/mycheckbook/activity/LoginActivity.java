package com.ccmm.cc.mycheckbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.utils.LoginTools;

/**
 * Created by cc on 2018/4/5.
 */

public class LoginActivity extends Activity {
    Button logininButton=null;
    EditText editText_username=null;
    EditText editText_password=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //2.绑定login按钮的处理方法
        logininButton=(Button)this.findViewById(R.id.login_button);
        editText_username=(EditText)this.findViewById(R.id.editText_username);
        editText_password=(EditText)this.findViewById(R.id.editText_password);

        View.OnClickListener logininButtonHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1.获得用户名,密码
                String username =  editText_username.getText().toString();
                String password =  editText_password.getText().toString();
                //2.检查用户名密码是否正确
                if(LoginTools.checkPassword(username,password)){
                    //跳转到选择记账本界面
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, ChooseCheckbookActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();//如果不关闭当前的会出现好多个页面
                }else{
                    //弹出出错信息
                    String errorInfo="登陆失败！用户名或密码错误";
                    Toast toast = Toast.makeText(v.getContext(), errorInfo, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);  //设置显示位置
                    TextView vi = (TextView) toast.getView().findViewById(android.R.id.message);
                    vi.setTextColor(Color.RED);     //设置字体颜色
                    toast.show();
                }

            }
        };
        logininButton.setOnClickListener(logininButtonHandler);
    }




}
