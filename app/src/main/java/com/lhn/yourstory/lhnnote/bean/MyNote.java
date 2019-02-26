package com.lhn.yourstory.lhnnote.bean;

import com.lhn.yourstory.lhnnote.constant.Constant;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by lenovo on 2018/8/10.
 * 实体类
 */

@DatabaseTable(tableName = Constant.DB_TABLE_NAME)
public class MyNote {

    @DatabaseField(id = true,columnName = "note_id")
    private int note_id;

    @DatabaseField(columnName = Constant.DB_TITLE,dataType = DataType.STRING,canBeNull = false)
    private String note_title;

    @DatabaseField(canBeNull = false)
    private String note_time;

    @DatabaseField
    private String note_message;

    private String user_id;

    public MyNote(String note_title, String note_time, String note_message) {
        this.note_title = note_title;
        this.note_time = note_time;
        this.note_message = note_message;
    }

    public MyNote(int note_id, String note_title, String note_time, String note_message) {
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_time = note_time;
        this.note_message = note_message;
    }

    public MyNote() {
    }


    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_time() {
        return note_time;
    }

    public void setNote_time(String note_time) {
        this.note_time = note_time;
    }

    public String getNote_message() {
        return note_message;
    }

    public void setNote_message(String note_message) {
        this.note_message = note_message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
