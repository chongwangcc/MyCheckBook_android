package com.ccmm.cc.mycheckbook.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.utils.CheckbookTools;
import com.ccmm.cc.mycheckbook.utils.LoginTools;

/**
 * 添加记账本的Fragment
 */
public class CheckbookAddingFragment extends Fragment implements View.OnClickListener{

    public static final String ARG_PAGE = "ARG_PAGE";
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    private int mPage;
    View view_join;
    View view_add;
    public static CheckbookAddingFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CheckbookAddingFragment pageFragment = new CheckbookAddingFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view ;
        switch(mPage){
            case 1: //加入
                view = inflater.inflate(R.layout.fragment_join_checkbook, container, false);
                //2.设置按钮的点击事件处理方法
                Button join_checkbook_button=view.findViewById(R.id.join_checkbook_button);
                join_checkbook_button.setOnClickListener(this);


                view_join=view;
                break;
            default:
            case 0:  //新建
                view = inflater.inflate(R.layout.fragment_add_checkbook, container, false);
                Button new_checkbook_button=view.findViewById(R.id.new_checkbook_button);
                ImageView pic_imageview=view.findViewById(R.id.checkbook_pic);
                pic_imageview.setOnClickListener(this);
                new_checkbook_button.setOnClickListener(this);
                view_add=view;
                break;

        }
        return view;

    }

    @Override
    public void onClick(View v) {
        CheckbookEntity checkbook;
        switch (v.getId()){
            case R.id.new_checkbook_button:
                //1.获得控件
                EditText  name_editText = view_add.findViewById(R.id.checkbook_title_editText);
                EditText  descrption_editText = view_add.findViewById(R.id.checkbook_description_editText);
                CheckBox islock_checkBox = view_add.findViewById(R.id.checkbook_islocal);
                ImageView pic_ImageView = view_add.findViewById(R.id.checkbook_pic);

                //2.生成checkbook
                String name = name_editText.getText().toString();
                String description = descrption_editText.getText().toString();
                boolean islocal = islock_checkBox.isChecked();
                pic_ImageView.buildDrawingCache();
                Bitmap pic =pic_ImageView.getDrawingCache();

                checkbook = CheckbookTools.newChechbook(LoginTools.getLoginUser(),name,description,islocal,pic);
                CheckbookTools.addOneCheckbook(LoginTools.getLoginUser(),checkbook);
                getActivity().finish();
                break;
            case R.id.join_checkbook_button:
                //1.获得控件
                EditText invitation_editText = view_join.findViewById(R.id.invitation_editText);
                String invation = invitation_editText.getText().toString();
                checkbook = CheckbookTools.checkInvitation(LoginTools.getLoginUser(),invation);
                if(checkbook==null){
                    //弹出报错信息
                    String errorInfo="加入失败！！邀请码错误！！";
                    Toast toast = Toast.makeText(v.getContext(), errorInfo, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);  //设置显示位置
                    TextView vi =  toast.getView().findViewById(android.R.id.message);
                    vi.setTextColor(Color.RED);     //设置字体颜色
                    toast.show();
                }else{
                    //加入到记账本列表中
                    CheckbookTools.addOneCheckbook(LoginTools.getLoginUser(),checkbook);
                    getActivity().finish();
                }
                break;
            case R.id.checkbook_pic:
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ((ImageView)view_add.findViewById(R.id.checkbook_pic)).setImageURI(selectedImage);
        }
    }
}
