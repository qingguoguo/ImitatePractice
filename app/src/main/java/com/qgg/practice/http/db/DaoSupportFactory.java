package com.qgg.practice.http.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/4
 * @describe :要持有外部数据库的引用
 */

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;
    /**
     * 持有外部数据库的引用
     */
    private SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory() {
    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport = new DaoSupport<T>();
        daoSupport.init(mSqLiteDatabase, clazz);
        return daoSupport;
    }

    public void init(Context context) {
        //数据库放在外部存储卡
        //判断是否有存储卡，6.0+需要动态申请权限
        // 把数据库放到内存卡里面  判断是否有存储卡 6.0要动态申请权限
        File dbRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "qingguoguo" + File.separator + "database");
        //File dbRoot = new File(context.getCacheDir() + File.separator + "database");
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot, "download.db");

        // 打开或者创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }
}
