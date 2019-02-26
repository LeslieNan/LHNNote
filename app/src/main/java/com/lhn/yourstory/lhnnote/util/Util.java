package com.lhn.yourstory.lhnnote.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.lhn.yourstory.lhnnote.MyApplication;
import com.lhn.yourstory.lhnnote.R;
import com.lhn.yourstory.lhnnote.constant.Constant;
import com.lhn.yourstory.lhnnote.db.MyDBDao;

/**
 * Created by lenovo on 2018/10/7.
 * 普通工具类
 */

public class Util {
    /**
     * 判断字符串是否是email的格式
     *
     * @param address 检测的字符串
     * @return true 是email格式
     */
    public static boolean isEmailAddress(String address) {
        String regex = "\\w+@\\w+\\.\\w+";
        return address.matches(regex);
    }


    /**
     * 删除所有笔记
     */
    public static void deleteAllNote(){
        MyDBDao myDao=new MyDBDao(MyApplication.getContext());
        myDao.deleteAll();
    }

    /**
     * 获取登录信息，通过preferences
     * @return
     */
    public static String getLoginInfo(){
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(Constant.LOGIN_SHAREDPREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(Constant.LOGIN_SHARE_ID, "未登录");
    }

    /**
     * 设置登录信息
     * @param loginID
     */
    public static void setSharedPreferences(String loginID){
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences(Constant.LOGIN_SHAREDPREFERENCES, Context.MODE_PRIVATE)
                .edit();
        editor.putString(Constant.LOGIN_SHARE_ID, loginID);
        editor.apply();
    }


    public static void setWindowsColor(AppCompatActivity activity){
        Window window = activity.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
        }
    }

}
