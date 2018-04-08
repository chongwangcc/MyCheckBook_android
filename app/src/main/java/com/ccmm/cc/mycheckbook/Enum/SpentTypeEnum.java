package com.ccmm.cc.mycheckbook.Enum;

import android.graphics.drawable.Drawable;

import com.ccmm.cc.mycheckbook.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/4/7.
 */

public class SpentTypeEnum {
    private static Map<String,Integer > drawableMap= new HashMap<>();
    static{
        drawableMap.put("一般", R.mipmap.u210);
        drawableMap.put("餐饮",R.mipmap.u211);
        drawableMap.put("购物",R.mipmap.u212);
        drawableMap.put("服饰",R.mipmap.u213);
        drawableMap.put("交通",R.mipmap.u214);
        drawableMap.put("娱乐",R.mipmap.u215);
        drawableMap.put("社交",R.mipmap.u216);
        drawableMap.put("居家",R.mipmap.u217);
        drawableMap.put("通讯",R.mipmap.u218);
        drawableMap.put("零食",R.mipmap.u219);

        drawableMap.put("美容",R.mipmap.u220);
        drawableMap.put("运动",R.mipmap.u221);
        drawableMap.put("旅行",R.mipmap.u222);
        drawableMap.put("数码",R.mipmap.u223);
        drawableMap.put("学习",R.mipmap.u224);
        drawableMap.put("医疗",R.mipmap.u225);
        drawableMap.put("书籍",R.mipmap.u226);
        drawableMap.put("宠物",R.mipmap.u227);
        drawableMap.put("彩票",R.mipmap.u228);
        drawableMap.put("汽车",R.mipmap.u229);

        drawableMap.put("办公",R.mipmap.u230);
        drawableMap.put("住房",R.mipmap.u231);
        drawableMap.put("维修",R.mipmap.u232);
        drawableMap.put("孩子",R.mipmap.u233);
        drawableMap.put("长辈",R.mipmap.u234);
        drawableMap.put("礼物",R.mipmap.u235);
        drawableMap.put("礼金",R.mipmap.u236);
        drawableMap.put("还钱",R.mipmap.u237);
        drawableMap.put("捐赠",R.mipmap.u238);
        drawableMap.put("理财",R.mipmap.u239);

        drawableMap.put("垫付",R.mipmap.u300);
        drawableMap.put("生活-信",R.mipmap.u300);
        drawableMap.put("doo新",R.mipmap.u300);
        drawableMap.put("租房",R.mipmap.u300);
        drawableMap.put("套现",R.mipmap.u300);
        drawableMap.put("手续费",R.mipmap.u300);
        drawableMap.put("投资支出",R.mipmap.u300);
        drawableMap.put("支付",R.mipmap.u300);
        drawableMap.put("结婚相关",R.mipmap.u300);
        drawableMap.put("还钱",R.mipmap.u300);

        drawableMap.put("工资",R.mipmap.u250);
        drawableMap.put("兼职",R.mipmap.u251);
        drawableMap.put("理财收益",R.mipmap.u252);
        drawableMap.put("礼金",R.mipmap.u253);
        drawableMap.put("其他",R.mipmap.u300);

        drawableMap.put("投资账户",R.mipmap.u300);
        drawableMap.put("转账",R.mipmap.u300);
        drawableMap.put("股票",R.mipmap.u300);
        drawableMap.put("s象限",R.mipmap.u300);
        drawableMap.put("退押金",R.mipmap.u300);
        drawableMap.put("反现金",R.mipmap.u300);



    }

    public static List<String> getSpentType(int position){
        List<String> spendType = new LinkedList<String>();
        switch(position){
            case 0:
                spendType.add("一般");
                spendType.add("餐饮");
                spendType.add("购物");
                spendType.add("服饰");
                spendType.add("交通");
                spendType.add("娱乐");
                spendType.add("社交");
                spendType.add("居家");
                spendType.add("通讯");
                spendType.add("零食");

                break;
            case 1:
                spendType.add("美容");
                spendType.add("运动");
                spendType.add("旅行");
                spendType.add("数码");
                spendType.add("学习");
                spendType.add("医疗");
                spendType.add("书籍");
                spendType.add("宠物");
                spendType.add("彩票");
                spendType.add("汽车");

                break;
            case 2:
                spendType.add("办公");
                spendType.add("住房");
                spendType.add("维修");
                spendType.add("孩子");
                spendType.add("长辈");
                spendType.add("礼物");
                spendType.add("礼金");
                spendType.add("还钱");
                spendType.add("捐赠");
                spendType.add("理财");

                break;
            case 3:
                spendType.add("垫付");
                spendType.add("生活-信");
                spendType.add("doo新");
                spendType.add("租房");
                spendType.add("套现");
                spendType.add("手续费");
                spendType.add("投资支出");
                spendType.add("支付");
                spendType.add("结婚相关");
                spendType.add("还钱");
                break;

        }
        return spendType;
    }

    public static List<String> getIncomeType(int position){
        List<String> spendType = new LinkedList<String>();
        switch(position) {
            case 0:
                spendType.add("工资");
                spendType.add("兼职");
                spendType.add("理财收益");
                spendType.add("礼金");
                spendType.add("其他");
                break;
            case 1:
                spendType.add("投资账户");
                spendType.add("转账");
                spendType.add("股票");
                spendType.add("s象限");
                spendType.add("退押金");
                spendType.add("反现金");
                break;
        }
        return spendType;
    }

    public static Integer getDrawableIndex(String name){
       return drawableMap.get(name);
    }

    public static Integer getLengthType(String type){
        int Len=0;
        switch(type){
            case "支出":
                Len=4;
                break;
            case "收入":
                Len=2;
                break;
            case "内部转账":
                break;
        }
        return Len;
    }

    public static List<List<String>> getAllCategoryNames(String type){
        List<List<String>> namesList = new LinkedList<>();
        for(int i=0;i<getLengthType(type);i++){
            List<String> names=new LinkedList<>();
            switch (type){
                case "收入":
                    names =getIncomeType(i);
                    break;
                case "支出":
                    names =getSpentType(i);
                    break;
                case "内部转账":
                    break;
            }
            namesList.add(names);
        }
        return namesList;
    }

    public static void clearAllDrawableColor(){
        for(String str : drawableMap.keySet()){

        }
    }

    public class SpendTypeEntity{
        String name;
        Drawable pic;
    }
}
