package com.ccmm.cc.mycheckbook.utils;

/**
 * Created by cc on 2018/4/6.
 */

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import android.graphics.Bitmap;
import android.util.Log;
public class ImageCache {
    private static final int MAX_CAPACITY = 50;// 一级缓存的最大空间
    private static final long DELAY_BEFORE_PURGE = 10 * 1000;// 定时清理缓存
    // 0.75是加载因子为经验值，true则表示按照最近访问量的高低排序，false则表示按照插入顺序排序
    private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(
            MAX_CAPACITY / 2, 0.75f, true) {
        protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
            if (size() > MAX_CAPACITY) {// 当超过一级缓存阈值的时候，将老的值从一级缓存搬到二级缓存
                mSecondLevelCache.put(eldest.getKey(),
                        new SoftReference<Bitmap>(eldest.getValue()));
                return true;
            }
            return false;
        };
    };
    // 二级缓存，采用的是软应用，只有在内存吃紧的时候软应用才会被回收，有效的避免了oom
    private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
            MAX_CAPACITY / 2);
    /**
     * 放入缓存
     *
     * @param url
     * @param value
     */
    public void addBitmap2Cache(String url, Bitmap value) {
        if (value == null || url == null) {
            return;
        }
        synchronized (mFirstLevelCache) {
            mFirstLevelCache.put(url, value);
        }
    }
    /**
     * 从缓存中获取
     *
     * @param url
     */
    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap = null;
        bitmap = getFromFirstLevelCache(url);// 从一级缓存中拿
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = getFromSecondLevelCache(url);// 从二级缓存中拿
        return bitmap;
    }
    /**
     * 从二级缓存中拿
     *
     * @param url
     * @return
     */
    private Bitmap getFromSecondLevelCache(String url) {
        Bitmap bitmap = null;
        SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
        if (softReference != null) {
            bitmap = softReference.get();
            if (bitmap == null) {// 由于内存吃紧，软引用已经被gc回收了
                mSecondLevelCache.remove(url);
            }
        }
        return bitmap;
    }
    /**
     * 从一级缓存中拿
     *
     * @param url
     * @return
     */
    private Bitmap getFromFirstLevelCache(String url) {
        Bitmap bitmap = null;
        synchronized (mFirstLevelCache) {
            bitmap = mFirstLevelCache.get(url);
            if (bitmap != null) {// 将最近访问的元素放到链的头部，提高下一次访问该元素的检索速度（LRU算法）
                mFirstLevelCache.remove(url);
                mFirstLevelCache.put(url, bitmap);
            }
        }
        return bitmap;
    }
    public void clear() {
        mFirstLevelCache.clear();
        mSecondLevelCache.clear();
    }
}
