package com.ccmm.cc.mycheckbook.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cc on 2018/4/7.
 */

public class AdapterViewpager extends PagerAdapter {

    private List<View> views;

    public AdapterViewpager(List<View> mViewList) {
        this.views = mViewList;
    }

    @Override
    public int getCount() {//必须实现
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {//必须实现
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化

        ViewGroup parent = (ViewGroup) views.get(position).getParent();
        if (parent != null)
            parent.removeAllViews();

        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
        container.removeView(views.get(position));
    }
}