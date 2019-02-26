package com.lhn.yourstory.lhnnote;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lhn.yourstory.lhnnote.bean.User;
import com.lhn.yourstory.lhnnote.constant.Constant;
import com.lhn.yourstory.lhnnote.util.Util;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {


    private EditText etID;
    private EditText etName;
    private EditText etPsw;
    private EditText etAgain;
    private Button btnRegister;
    private RegisterActivity activity;

    //解析数据时用的变量
    private String jsonCode;
    private String jsonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        activity = this;
        etID = findViewById(R.id.et_register_id);
        etName=findViewById(R.id.et_register_name);
        etPsw = findViewById(R.id.et_register_psw);
        etAgain = findViewById(R.id.et_register_again);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etID.getText().toString();
                String psw = etPsw.getText().toString();
                String again = etAgain.getText().toString();
                if (!Util.isEmailAddress(id)) {
                    Toast.makeText(activity, "账号邮箱格式错误", Toast.LENGTH_SHORT).show();
                } else if (id.equals("") || psw.equals("") || again.equals("")) {
                    Toast.makeText(activity, "账号和密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!psw.equals(again)) {
                    Toast.makeText(activity, "两次密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    register();
                }
            }
        });
    }

    /**
     * 执行注册逻辑
     */
    private void register() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //生成注册json数据
                    Gson gson = new Gson();
                    User user = new User(etID.getText().toString(), etName.getText().toString(), etPsw.getText().toString());
                    String jsonData = gson.toJson(user);
                    //创建requestBody的post请求对象,这是向服务器传一般数据
                    RequestBody requestBody = new FormBody.Builder()
                            .add("message", jsonData)
                            .build();
                    Log.d("我的", jsonData);
                    //以下为获取数据步骤
                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(Constant.URL_REGISTER);
                    builder.post(requestBody);
                    Request request = builder.build();
                    Call call = client.newCall(request);
                    //response是服务器返回的数据
                    Response response = call.execute();
                    String str = response.body().string();
                    //解析json数据
                    try {
                        JSONObject object = new JSONObject(str);
                        jsonCode = object.getString("code");
                        jsonText = object.getString("text");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("我的", String.valueOf(response.body()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonCode.equals("0")) {
                                Toast.makeText(activity, jsonText, Toast.LENGTH_SHORT).show();
                            } else if (jsonCode.equals("1")) {
                                Toast.makeText(activity, "注册成功", Toast.LENGTH_SHORT).show();
                                //返回到登录界面
                                finish();
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
