package com.lhn.yourstory.lhnnote.db;

import android.content.Context;

import com.lhn.yourstory.lhnnote.bean.MyNote;
import com.lhn.yourstory.lhnnote.constant.Constant;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by lenovo on 2018/8/10.
 * db操作的封装类
 */

public class MyDBDao {

    private Dao<MyNote, Integer> dbDao;

    private DatabaseHelper dbHelper;


    public MyDBDao(Context c) {
        dbHelper = DatabaseHelper.getInstance(c);
        try {
            dbDao = dbHelper.getDao(MyNote.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库插入
     *
     * @param title
     * @param time
     * @param content
     * @return
     */
    public int insert(String title, String time, String content) {
        int maxId;
        MyNote newNote = new MyNote(title, time, content);
        try {
            List<MyNote> list = dbDao.queryForAll();
            if (list.size() == 0) {
                //当第一次存储时，可以直接插入，id为0
                return dbDao.create(newNote);
            } else {
                //找寻最大的id
                maxId = list.get(0).getNote_id();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getNote_id() > maxId) {
                        maxId = list.get(i).getNote_id();
                    }
                }
                newNote.setNote_id(maxId+1);
                return dbDao.create(newNote);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public void insertOrUpdate(MyNote newNote) throws SQLException {
        dbDao.createOrUpdate(newNote);
    }


    public int update(int id, String title, String time, String content) {
        int i = 0;
        UpdateBuilder update = dbDao.updateBuilder();
        try {
            update.setWhere(update.where().eq(Constant.DB_ID, id));
            update.updateColumnValue(Constant.DB_TITLE, title);
            update.updateColumnValue(Constant.DB_MESSAGE, content);
            update.updateColumnValue(Constant.DB_TIME, time);
            i = update.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;

    }


    /**
     * 删除
     *
     * @param dID 根据ID删除
     * @return 返回1表示删除成功
     */
    public int delete(int dID) {
        int i = 0;
        try {
            i = dbDao.deleteById(dID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 数据库查询所有
     *
     * @return 数据库中不存在数据则返回空
     */
    public List<MyNote> queryAll() {
        try {
            return dbDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteAll() {
        List<MyNote> list = queryAll();
        for (int i = 0; i < list.size(); i++) {
            try {
                dbDao.deleteById(list.get(i).getNote_id());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
