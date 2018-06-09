package com.qgg.practice.http.db;

import android.database.sqlite.SQLiteDatabase;

import com.qgg.practice.http.db.curd.QuerySupport;

import java.util.List;


/**
 * @author :qingguoguo
 * @datetime ：2018/4/4
 * @describe :
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase SQLiteDatabase, Class<T> clazz);

    /**
     * 插入数据
     *
     * @param t
     * @return
     */
    long insert(T t);

    /**
     * 插入数据
     *
     * @param datas
     */
    void insert(List<T> datas);

    /**
     * 获取专门查询的支持类
     */
    QuerySupport<T> querySupport();


    /**
     * delete
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    int delete(String whereClause, String... whereArgs);

    /**
     * update
     *
     * @param obj
     * @param whereClause
     * @param whereArgs
     * @return
     */
    int update(T obj, String whereClause, String... whereArgs);
}
