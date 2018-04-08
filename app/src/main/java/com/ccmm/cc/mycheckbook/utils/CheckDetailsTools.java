package com.ccmm.cc.mycheckbook.utils;

import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/** 记账本明细的工具类
 * Created by cc on 2018/4/6.
 */

public class CheckDetailsTools {
    public static String details_year="";
    public static String detals_month="";
    static{
        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        details_year=sdf1.format(dt);
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        detals_month=sdf2.format(dt);
    }
    /**
     * 获得明细的分组结果，按天获得组
     * @return
     */
    public static List<DetailGroupBean> getDetailGroupByDay(){
        List<DetailGroupBean> list = new LinkedList<DetailGroupBean>();
        //TODO 随机生成几个假数据
        for(int j=0;j<3;j++){
            DetailGroupBean groupBean = new DetailGroupBean();

            List<CheckDetailBean> DetailBeanList = new LinkedList<>();
            for(int i=0;i<10;i++){
                CheckDetailBean bean = new CheckDetailBean();
                bean.setIncomeType("零食");
                bean.setMoney((float)10.24);
                bean.setDescription("随便买点小吃的");
                DetailBeanList.add(bean);
            }
            groupBean.setDay("04");
            groupBean.setWeek("星期一");
            groupBean.setData(DetailBeanList);
            groupBean.setTotal_spent((float)100.34);
            groupBean.setTotal_income((float)19018.13);

            list.add(groupBean);
        }

        return list;

    }


    public static boolean addOneCheckDetails(CheckDetailBean checkDetailsBean){
        boolean b=true;

        return b;
    }

    public static String getDetails_year() {
        return details_year;
    }

    public static void setDetails_year(String details_year) {
        CheckDetailsTools.details_year = details_year;
    }

    public static String getDetals_month() {
        return detals_month;
    }

    public static void setDetals_month(String detals_month) {
        CheckDetailsTools.detals_month = detals_month;
    }
}
