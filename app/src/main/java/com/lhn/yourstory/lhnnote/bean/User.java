package com.lhn.yourstory.lhnnote.bean;

/**
 * Created by lenovo on 2018/10/9.
 * 用户类
 */

public class User {

    private String user_id;
    private String user_name;
    private String user_psw;

    public User(String user_id, String user_name, String user_psw) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_psw = user_psw;
    }

    public User() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_psw() {
        return user_psw;
    }

    public void setUser_psw(String user_psw) {
        this.user_psw = user_psw;
    }
}
