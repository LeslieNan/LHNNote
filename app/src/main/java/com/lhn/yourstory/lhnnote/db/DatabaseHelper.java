package com.lhn.yourstory.lhnnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lhn.yourstory.lhnnote.bean.MyNote;
import com.lhn.yourstory.lhnnote.constant.Constant;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by lenovo on 2018/8/10.
 * 数据库帮助类
 * 要做成单例模式
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static DatabaseHelper mHelper=null;


    private DatabaseHelper(Context context) {
        super(context, Constant.DB_NAME, null, 1);
    }


    /**
     * 单例模式
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getInstance(Context context){
        if (mHelper==null){
            mHelper=new DatabaseHelper(context);
        }
        return mHelper;
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MyNote.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,MyNote.class,false);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
