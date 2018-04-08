package com.ccmm.cc.mycheckbook.utils;

import com.ccmm.cc.mycheckbook.models.DetailBean;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;

import java.util.LinkedList;
import java.util.List;

/** 记账本明细的工具类
 * Created by cc on 2018/4/6.
 */

public class DetailsTools {

    /**
     * 获得明细的分组结果，按天获得组
     * @return
     */
    public static List<DetailGroupBean> getDetailGroupByDay(){
        List<DetailGroupBean> list = new LinkedList<DetailGroupBean>();
        //TODO 随机生成几个假数据
        for(int j=0;j<3;j++){
            DetailGroupBean groupBean = new DetailGroupBean();

            List<DetailBean> DetailBeanList = new LinkedList<>();
            for(int i=0;i<10;i++){
                DetailBean bean = new DetailBean();
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
}
