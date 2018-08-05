package com.ccmm.cc.mycheckbook.Enum;

import java.util.LinkedList;
import java.util.List;

public class BalanceName {
    public static final String Income="收入";
    public static final String Expend="支出";
    public static final String Inflow="流入";
    public static final String Outflow="流出";

    public static List<String> getALlNames(){
        LinkedList<String> result=new LinkedList<>();
        result.add(Income);
        result.add(Expend);
        result.add(Inflow);
        result.add(Outflow);
        return result;
    }
}
