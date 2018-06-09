package com.qgg.practice.download;

import android.content.Context;

import com.qgg.practice.http.db.DaoSupportFactory;
import com.qgg.practice.http.db.IDaoSupport;

import java.util.List;

final class DaoManagerHelper {

    private final static DaoManagerHelper sManager = new DaoManagerHelper();
    IDaoSupport<DownloadEntity> mDaoSupport;

    private DaoManagerHelper() {
    }

    public static DaoManagerHelper getManager() {
        return sManager;
    }

    public void init(Context context) {
        DaoSupportFactory.getFactory().init(context);
        mDaoSupport = DaoSupportFactory.getFactory().getDao(DownloadEntity.class);
    }

    public void addEntity(DownloadEntity entity) {
        long delete = mDaoSupport.delete("url = ? and threadId = ?", entity.getUrl(), entity.getThreadId() + "");
        long size = mDaoSupport.insert(entity);
    }

    public List<DownloadEntity> queryAll(String url) {
        return mDaoSupport.querySupport().selection("url = ?").selectionArgs(url).query();
    }

    public void remove(String url) {
        mDaoSupport.delete("url = ?", url);
    }
}
