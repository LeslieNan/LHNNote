package com.lhn.yourstory.lhnnote.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lhn.yourstory.lhnnote.MainActivity;
import com.lhn.yourstory.lhnnote.MyApplication;
import com.lhn.yourstory.lhnnote.bean.MyNote;
import com.lhn.yourstory.lhnnote.constant.Constant;
import com.lhn.yourstory.lhnnote.db.MyDBDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/10/6.
 * 工具类
 */

public class NetUtil {


    private static String responseData;


    /**
     * 检测当的网络（WLAN、3G/2G）状态,是否是可用的
     * 是否连接和是否可用两个概念
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 解析json数据
     *
     * @param jsonStr 要解析的String
     * @return 返回MyNoteTable的list
     */
    public static List<MyNote> parsingJsonString(String jsonStr) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, new TypeToken<List<MyNote>>() {
        }.getType());
    }


    /**
     * 上传数据
     *
     * @param activity
     * @param jsonStr
     */
    public static void uploadData(final AppCompatActivity activity, final String jsonStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建requestBody的post请求对象,这是向服务器传一般数据
                    RequestBody requestBody = new FormBody.Builder()
                            .add("message", jsonStr)
                            .build();
                    Log.d("我的", jsonStr);
                    //以下为获取数据步骤
                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(Constant.URL_UPLOAD);
                    builder.post(requestBody);
                    Request request = builder.build();
                    Call call = client.newCall(request);
                    //response是服务器返回的数据
                    Response response = call.execute();
                    final String str = response.body().string();
                    Log.d("我的", String.valueOf(response.body()));
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (str.equals("1")) {
//                                Toast.makeText(activity, "上传成功", Toast.LENGTH_SHORT).show();
                                downloadData(activity);
                            } else {
                                Toast.makeText(activity, "错误：" + str, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 下载note数据
     *
     * @param activity 传来的activity
     */
    public static void downloadData(final AppCompatActivity activity) {
        //创建requestBody的post请求对象,这是向服务器传一般数据
        RequestBody requestBody = new FormBody.Builder()
                .add("id",
                        Util.getLoginInfo())
                .build();
        Log.d("NetUtil", Util.getLoginInfo());
        //利用回调接口实现
        NetUtil.sendOkhttpRequest(Constant.URL_DOWNLOAD, requestBody, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //当前仍在子线程中
                String jsonString = response.body().string();
                Log.d("我的1", jsonString);
                //解析json数据
                List<MyNote> serverList = NetUtil.parsingJsonString(jsonString);
                MyDBDao dao = new MyDBDao(activity);
                try {
                    for (int i = 0; i < serverList.size(); i++) {
                        dao.insertOrUpdate(serverList.get(i));
                    }
                    /*因为Toast的初始化函数中，
                    自己开了个线程new Handler();
                    所以使得当前的Toast要是不在主线程就会报错。
                    所以要在子线程中使用Toast，要在前后加上Looper.prepare()和Looper.loop()*/
                    Looper.prepare();
                    Toast.makeText(activity, "同步成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
//                    //刷新主界面list
//                    MainActivity.refreshList();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(activity, "数据插入出错", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
        });
    }


    /**
     * 执行获取网络数据的请求
     *
     * @param address
     * @param requestBody
     * @param callback
     */
    public static void sendOkhttpRequest(String address, RequestBody requestBody, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * 从服务器端删除note
     */
    public static void deleteOnServer(int noteID) {
        final Context context = MyApplication.getContext();
        //获取登录信息
        String loginID = Util.getLoginInfo();
        //创建requestBody的post请求对象,这是向服务器传一般数据
        Gson gson = new Gson();
        MyNote note = new MyNote();
        note.setNote_id(noteID);
        note.setUser_id(loginID);
        RequestBody requestBody = new FormBody.Builder()
                .add("message", gson.toJson(note))
                .build();


        NetUtil.sendOkhttpRequest(Constant.URL_DELETE_NOTE, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String serverStr = response.body().string();
                Looper.prepare();
                Toast.makeText(context, serverStr, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

}
