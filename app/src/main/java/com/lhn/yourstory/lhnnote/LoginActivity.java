package com.lhn.yourstory.lhnnote;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lhn.yourstory.lhnnote.constant.Constant;
import com.lhn.yourstory.lhnnote.util.NetUtil;
import com.lhn.yourstory.lhnnote.util.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etID;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private LoginActivity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initWindows();
    }

    /**
     * 设置状态栏透明
     */
    private void initWindows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置透明导航栏
        }
    }


    private void initView() {
        activity=this;
        etID=findViewById(R.id.et_login_id);
        etPassword=findViewById(R.id.et_login_psw);
        btnLogin=findViewById(R.id.btn_login_login);
        tvRegister=findViewById(R.id.tv_login_register);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }


    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_login:
                //往服务器端传数据前的用户名邮箱格式检验
                if (etID.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    Toast.makeText(this, "账号和密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!Util.isEmailAddress(etID.getText().toString())) {
                    Toast.makeText(this, "邮箱格式有误", Toast.LENGTH_SHORT).show();
                } else {
                    login();
                }
                break;
            case R.id.tv_login_register:
                Intent intentMyInfo=new Intent(activity, RegisterActivity.class);
                startActivity(intentMyInfo);
                break;
        }

    }

    /**
     * 从服务器检查账号密码
     */
    private void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建requestBody的post请求对象,这是向服务器传一般数据
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id", etID.getText().toString())
                            .add("psw", etPassword.getText().toString())
                            .build();
                    //或者传入一般固定格式的
//                    RequestBody requestBody1=RequestBody.create();

                    //以下为获取数据步骤
                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(Constant.URL_LOGIN);
                    builder.post(requestBody);
                    Request request = builder.build();
                    Call call = client.newCall(request);
                    //response是服务器返回的数据
                    Response response = call.execute();

                    Log.d("我的", response + "");

                    final String str = response.body().string();
                    Log.d("我的", str);
                    Log.d("我的", String.valueOf(response.code()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (str.equals("0")) {
                                Toast.makeText(activity, "密码错误", Toast.LENGTH_SHORT).show();
                            } else if (str.equals("1")){
                                Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show();
                                //往SharedPreferences中添加登录信息
                                Util.setSharedPreferences(etID.getText().toString());
                                //删除本地所有note
                                Util.deleteAllNote();
                                //同步note
                                NetUtil.downloadData(activity);
                                //启动主activity
                                Intent intent=new Intent(activity,MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
