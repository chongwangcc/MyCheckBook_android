package com.ccmm.cc.mycheckbook.Enum;

import android.graphics.Color;

import com.ccmm.cc.mycheckbook.R;
import com.ccmm.cc.mycheckbook.utils.ZaTools;

public class CategoryColorEnum {
    private static final int mColors_expend_hex[] ={0xFFE4E1,0xFF4500,0xFFA07A,0xF4A460,0xFA8072,0xCD5C5C,0x8B0000,0x800000};
    private static final int mColors_income_hex[] ={0x7FFF00,0x006400,0x9ACD32,0x90EE90,0x2E8B57,0x00FF7F,0x32CD32,0xADFF2F};
    private static final int mColors_inner_hex[] = {0x0000FF,0x4169E1,0x1E90FF,0x00BFFF,0x0000CD,0x8A2BE2,0x800080,0x8A2BE2};
    private static final int mColors_out_hex[] = {0xBBFFFF,0xAEEEEE,0x96CDCD,	0x668B8B,0x98F5FF,0x8EE5EE,0x7AC5CD,0x53868B};
    public static final int default_null=Color.GRAY;
    public static final int mColors_expend[] ;
    public static final int mColors_income[] ;
    public static final int mColors_inFlow[] ;
    public static final int mColors_outFlow[] ;

    public static final Integer rank_ICON_ID[]={R.mipmap.num_1,R.mipmap.num_2,R.mipmap.num_3,R.mipmap.num_4,R.mipmap.num_5};

    static {
        int temp[] =new int[mColors_expend_hex.length];
        for(int i=0;i<temp.length;i++){
            String color_str="#"+ ZaTools.toHexString(mColors_expend_hex[i],6);
            temp[i]= Color.parseColor(color_str);
        }
        mColors_expend=temp;

        temp =new int[mColors_income_hex.length];
        for(int i=0;i<temp.length;i++){
            String color_str="#"+ ZaTools.toHexString(mColors_income_hex[i],6);
            temp[i]= Color.parseColor(color_str);
        }
        mColors_income=temp;

        temp =new int[mColors_inner_hex.length];
        for(int i=0;i<temp.length;i++){
            String color_str="#"+ ZaTools.toHexString(mColors_inner_hex[i],6);
            temp[i]= Color.parseColor(color_str);
        }
        mColors_inFlow =temp;

        temp =new int[mColors_out_hex.length];
        for(int i=0;i<temp.length;i++){
            String color_str="#"+ ZaTools.toHexString(mColors_out_hex[i],6);
            temp[i]= Color.parseColor(color_str);
        }
        mColors_outFlow = temp;
    }
}
