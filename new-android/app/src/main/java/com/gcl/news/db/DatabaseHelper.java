package com.gcl.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gcl.news.bean.NewsDraft;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{  
  
    private static final String TABLE_NAME = "sqlite-test.db";  
    /** 
     * userDao ，每张表对于一个 
     */  
    private Dao<NewsDraft, Integer> newsDraftsDao;
  
    private DatabaseHelper(Context context)
    {  
        super(context, TABLE_NAME, null, 2);  
    }

    /**
     * 数据库创建
     */
    @Override  
    public void onCreate(SQLiteDatabase database,
            ConnectionSource connectionSource)
    {  
        try  
        {
            // 创建表
            TableUtils.createTable(connectionSource, NewsDraft.class);
        } catch (SQLException e)
        {  
            e.printStackTrace();  
        }
    }

    /**
     * 数据库更新
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override  
    public void onUpgrade(SQLiteDatabase database,  
            ConnectionSource connectionSource, int oldVersion, int newVersion)  
    {  
        try  
        {  
            TableUtils.dropTable(connectionSource, NewsDraft.class, true);
            onCreate(database, connectionSource);  
        } catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
    }  
  
    private static DatabaseHelper instance;  
  
    /** 
     * 单例获取该Helper 
     *  
     * @param context 
     * @return 
     */  
    public static synchronized DatabaseHelper getHelper(Context context)  
    {  
        if (instance == null)  
        {  
            synchronized (DatabaseHelper.class)  
            {  
                if (instance == null)  
                    instance = new DatabaseHelper(context);  
            }  
        }  
  
        return instance;  
    }  
  
    /** 
     * 获得newsDraftsDao
     *  
     * @return 
     * @throws SQLException 
     */  
    public Dao<NewsDraft, Integer> getNewsDraftsDao()
    {  
        if (newsDraftsDao == null)
        {
            try {
                newsDraftsDao = getDao(NewsDraft.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }  
        return newsDraftsDao;
    }  
  
    /** 
     * 释放资源 
     */  
    @Override  
    public void close()  
    {  
        super.close();
        newsDraftsDao = null;
    }  
  
}  