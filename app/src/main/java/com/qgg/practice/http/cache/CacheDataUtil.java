package com.qgg.practice.http.cache;


import com.qgg.practice.LogUtils;
import com.qgg.practice.http.MD5Util;
import com.qgg.practice.http.db.DaoSupportFactory;
import com.qgg.practice.http.db.IDaoSupport;
import com.qgg.practice.http.db.curd.QuerySupport;

import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/4 21:45
 * @Describe :获取缓存数据工具类
 */
public class CacheDataUtil {
    public static final String TAG = "CacheDataUtil";

    /**
     * 获取缓存数据
     */
    public static String getCacheResultJson(String finalUrl) {
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        // 需要缓存，从数据库拿缓存
        QuerySupport<CacheData> cacheDataQuerySupport = dataDaoSupport.querySupport();
        List<CacheData> cacheDatas =cacheDataQuerySupport.selection("mUrlKey=?").selectionArgs(MD5Util.string2MD5(finalUrl)).query();

        if (cacheDatas.size() != 0) {
            //有缓存数据
            CacheData cacheData = cacheDatas.get(0);
            String resultJson = cacheData.getJsonResult();
            return resultJson;
        }
        return null;
    }

    /**
     * 缓存数据
     */
    public static long cacheData(String finalUrl, String resultJson) {
        IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        dataDaoSupport.delete("mUrlKey=?", MD5Util.string2MD5(finalUrl));
        long number = dataDaoSupport.insert(new CacheData(MD5Util.string2MD5(finalUrl), resultJson));
        LogUtils.i(TAG, "number --> " + number);
        return number;
    }
}
